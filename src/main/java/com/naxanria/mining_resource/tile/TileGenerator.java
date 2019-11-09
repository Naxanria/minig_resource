package com.naxanria.mining_resource.tile;

import com.naxanria.mining_resource.MR;
import com.naxanria.mining_resource.gui.container.GeneratorContainer;
import com.naxanria.mining_resource.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/*
  @author: Naxanria
*/
public class TileGenerator extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inputHandler = LazyOptional.of(this::createInputHandler);

  private int generating = 60;
  private int burnTime = 0;
  private int newBurnTime = 1600;
  private int maxExtract = 500;
  private int totalBurnTime = 0;
  
  public TileGenerator()
  {
    super(ModTiles.GENERATOR);
  }
  
  private IEnergyStorage createEnergy()
  {
    return new CustomEnergyStorage(250000, 0, maxExtract);
  }
  
  private IItemHandler createInputHandler()
  {
    return new ItemStackHandler(1)
    {
      @Nonnull
      @Override
      public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
      {
        return isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
      }
  
      @Nonnull
      @Override
      public ItemStack extractItem(int slot, int amount, boolean simulate)
      {
        return ItemStack.EMPTY;
      }
  
      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack)
      {
        // todo: accept all burnables.
        return slot == 0 && stack.getItem() == Items.COAL || stack.getItem() == Items.COAL_BLOCK || stack.getItem() == Items.CHARCOAL;
      }
  
      @Override
      protected void onContentsChanged(int slot)
      {
        markDirty();
      }
    };
  }
  
  @Override
  public void tick()
  {
    if (world.isRemote)
    {
      return;
    }
    
    generatePower();
    spreadPower();
  }
  
  private void generatePower()
  {
    if (burnTime > 0)
    {
      burnTime--;
      addEnergy(generating);
      markDirty();
    }
    
    if (burnTime <= 0)
    {
//      totalBurnTime = 0;
      if (!isFull())
      {
        startBurn();
      }
    }
  }
  
  private void startBurn()
  {
    inputHandler.ifPresent
    (
      handler ->
      {
        ItemStackHandler stackHandler = (ItemStackHandler) handler;
        ItemStack stack = handler.getStackInSlot(0);
        if (!stack.isEmpty())
        {
          // todo: accept all burnables.
          
          Item toBurn = stack.getItem();
          int itemBurnTime = stack.getBurnTime();
          if (itemBurnTime <= 0)
          {
            if (toBurn == Items.COAL || toBurn == Items.CHARCOAL)
            {
              itemBurnTime = newBurnTime;
            }
            else if (toBurn == Items.COAL_BLOCK)
            {
              itemBurnTime = newBurnTime * 9;
            }
          }
          
          burnTime = itemBurnTime;
          totalBurnTime = burnTime;
          
          markDirty();
          
          stack.shrink(1);
          if (stack.getCount() <= 0)
          {
            stackHandler.setStackInSlot(0, ItemStack.EMPTY);
          }
        }
      }
    );
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityEnergy.ENERGY)
    {
      return energy.cast();
    }
    
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
    {
      return inputHandler.cast();
    }
    
    return super.getCapability(cap, side);
  }
  
  private boolean isFull()
  {
    AtomicBoolean full = new AtomicBoolean(false);
    
    energy.ifPresent(energy -> full.set(energy.getEnergyStored() == energy.getMaxEnergyStored()));
    
    return full.get();
  }
  
  private void addEnergy(int generating)
  {
    energy.ifPresent(energy -> ((CustomEnergyStorage) energy).insertInternal(generating));
  }
  
  public int getEnergy()
  {
    AtomicInteger amount = new AtomicInteger(0);
    
    energy.ifPresent(energy -> amount.set(energy.getEnergyStored()));
    
    return amount.get();
  }
  
  
  public int getCapacity()
  {
    AtomicInteger amount = new AtomicInteger();
    
    energy.ifPresent(energy -> amount.set(energy.getMaxEnergyStored()));
    
    return amount.get();
  }
  
  private void spreadPower()
  {
    int toSpread = Math.min(getEnergy(), maxExtract);
    if (toSpread <= 0)
    {
      return;
    }
    
    List<LazyOptional<IEnergyStorage>> toSupply = new ArrayList<>();
  
    for (Direction direction : Direction.values())
    {
      TileEntity te = world.getTileEntity(pos.offset(direction));
      if (te != null)
      {
        LazyOptional<IEnergyStorage> capability = te.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite());
        
        if (capability.isPresent())
        {
          toSupply.add(capability);
        }
      }
    }
    
    if (toSupply.size() == 0)
    {
      return;
    }
    
    int per = toSpread / toSupply.size();
    int bonus = toSpread % toSupply.size();
    AtomicInteger supplied = new AtomicInteger();

    for (LazyOptional<IEnergyStorage> neighbour : toSupply)
    {
      int supply = per + (bonus > 0 ? 1 : 0);

      bonus--;
      neighbour.ifPresent(energy -> supplied.addAndGet(energy.receiveEnergy(supply, false)));
    }

    energy.ifPresent(energy -> ((CustomEnergyStorage) energy).extractInternal(supplied.get()));
  }
  
  public int getBurnTime()
  {
    return burnTime;
  }
  
  @Override
  public void read(CompoundNBT compound)
  {
    burnTime = compound.getInt("burnTime");
    totalBurnTime = compound.getInt("totalBurnTime");
    inputHandler.ifPresent(handler -> ((ItemStackHandler) handler).deserializeNBT(compound.getCompound("inv")));
    energy.ifPresent(energy -> ((CustomEnergyStorage) energy).setEnergy(compound.getInt("energy")));
    
    super.read(compound);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT compound)
  {
    compound.putInt("burnTime", burnTime);
    compound.putInt("totalBurnTime", totalBurnTime);
    inputHandler.ifPresent(handler -> compound.put("inv", ((ItemStackHandler) handler).serializeNBT()));
    compound.putInt("energy", getEnergy());
    
    return super.write(compound);
  }
  
  @Override
  public ITextComponent getDisplayName()
  {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }
  
  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
  {
    return new GeneratorContainer(id, world, pos, playerInventory);
  }
  
  public void setBurnTime(int burnTime)
  {
    this.burnTime = burnTime;
  }
  
  public int getTotalBurnTime()
  {
    return totalBurnTime;
  }
  
  public void setTotalBurnTime(int totalBurnTime)
  {
    this.totalBurnTime = totalBurnTime;
  }
  
  public int getGenerating()
  {
    return generating;
  }
}

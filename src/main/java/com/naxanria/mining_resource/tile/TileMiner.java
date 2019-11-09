package com.naxanria.mining_resource.tile;

import com.naxanria.mining_resource.gui.container.MinerContainer;
import com.naxanria.mining_resource.item.ModItems;
import com.naxanria.mining_resource.item.TargetInfo;
import com.naxanria.mining_resource.item.TargetingManager;
import com.naxanria.mining_resource.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TileMiner extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> outputHandler = LazyOptional.of(this::createOutputHandler);
  
  public ItemStackHandler targetSlot = new ItemStackHandler(1)
  {
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
      return slot == 0 && stack.getItem() == ModItems.TARGETING_MODULE;
    }
  
    @Override
    protected void onContentsChanged(int slot)
    {
      checkTargetInfo();
      markDirty();
    }
  };
  
  private TargetInfo targetInfo;
  
  private int speedBonus = 0;
  
  private int currentWork = 0;
  private int baseWorkProgress = 7;
  private int workComplete = 5000;
  private int workCost = 72;
  
  public TileMiner()
  {
    super(ModTiles.MINER_IRON);
  }
  
  @Override
  public void tick()
  {
    if (targetInfo == null)
    {
      checkTargetInfo();
    }
    
    if (world.isRemote)
    {
      return;
    }
    
    if (targetInfo != null)
    {
      baseWorkProgress = targetInfo.progressPerWork;
      workCost = targetInfo.powerCost;
      
      if (canGenerate())
      {
        if (hasEnoughEnergy(workCost))
        {
          currentWork += baseWorkProgress;
          useEnergy(workCost);
          if (currentWork >= workComplete)
          {
            currentWork = 0;
            generate();
        
            markDirty();
          }
        }
      }
    }
  
//    MR.LOGGER.info("Energy {}/{}", getEnergy(), getCapacity());
  }
  
  private void checkTargetInfo()
  {
    ItemStack targetModule = targetSlot.getStackInSlot(0);
    if (!targetModule.isEmpty())
    {
      targetInfo = TargetingManager.getInfo(targetModule);
    }
    else
    {
      targetInfo = null;
    }
    
    resetProgress();
  }
  
  private void resetProgress()
  {
    currentWork = 0;
  }
  
  private boolean canGenerate()
  {
    AtomicBoolean generate = new AtomicBoolean(false);
    
    outputHandler.ifPresent
    (
      handler ->
      {
        ItemStack current = handler.getStackInSlot(0);
        ItemStack target = getTarget();
        if (current.isEmpty())
        {
          generate.set(true);
        }
        else
        {
          int maxSize = current.getMaxStackSize();
          int size = current.getCount() + target.getCount();
          generate.set(current.getItem() == target.getItem() && (size <= maxSize));
        }
      }
    );
    
    return generate.get();
  }
  
  private boolean hasEnoughEnergy(int amount)
  {
    AtomicBoolean enough = new AtomicBoolean(false);
    
    energy.ifPresent(energy -> enough.set(energy.getEnergyStored() >= amount));
    
    return enough.get();
  }
  
  private void useEnergy(int amount)
  {
    energy.ifPresent(energy -> ((CustomEnergyStorage) energy).extractInternal(amount));
  }
  
  private void generate()
  {
    outputHandler.ifPresent
    (
      handler ->
      {
        ItemStackHandler stackHandler = (ItemStackHandler) handler;
        ItemStack current = stackHandler.getStackInSlot(0);
        ItemStack target = getTarget();
        if (current == ItemStack.EMPTY)
        {
          stackHandler.setStackInSlot(0, target.copy());
        }
        else if (current.getItem() == target.getItem())
        {
          int count = Math.min(current.getCount() + target.getCount(), current.getMaxStackSize());
          current.setCount(count);
        }
      }
    );
  }
  
  private IEnergyStorage createEnergy()
  {
    return new CustomEnergyStorage(100000, 5000, 0).setCanExtract(false);
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
  
  private IItemHandler createOutputHandler()
  {
    return new ItemStackHandler(1)
    {
      @Nonnull
      @Override
      public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
      {
        return stack;
      }
  
      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack)
      {
        return false;
      }
    };
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
    {
      return outputHandler.cast();
    }
    
    if (cap == CapabilityEnergy.ENERGY)
    {
      return energy.cast();
    }
    
    return super.getCapability(cap, side);
  }
  
  @Override
  public void read(CompoundNBT nbt)
  {
    currentWork = nbt.getInt("currentWork");
    baseWorkProgress = nbt.getInt("baseWorkProgress");
    workComplete = nbt.getInt("workComplete");
    workCost = nbt.getInt("workCost");
    
    outputHandler.ifPresent(handler -> ((ItemStackHandler) handler).deserializeNBT(nbt.getCompound("inv")));
    energy.ifPresent(energy -> ((CustomEnergyStorage) energy).setEnergy(nbt.getInt("energy")));
    
    if (nbt.contains("targetModule"))
    {
      targetSlot.deserializeNBT(nbt.getCompound("targetModule"));
    }
    
    super.read(nbt);
  }
  
  @Override
  public CompoundNBT write(CompoundNBT nbt)
  {
    nbt.putInt("currentWork", currentWork);
    nbt.putInt("baseWorkProgress", baseWorkProgress);
    nbt.putInt("workComplete", workComplete);
    nbt.putInt("workCost", workCost);
    
    outputHandler.ifPresent(handler -> nbt.put("inv", ((ItemStackHandler) handler).serializeNBT()));
    energy.ifPresent(energy -> nbt.putInt("energy", getEnergy()));
    
    nbt.put("targetModule", targetSlot.serializeNBT());
    
    return super.write(nbt);
  }
  
  public int getCurrentWork()
  {
    return currentWork;
  }
  
  public TileMiner setCurrentWork(int currentWork)
  {
    this.currentWork = currentWork;
    return this;
  }
  
  public int getBaseWorkProgress()
  {
    return baseWorkProgress;
  }
  
  public TileMiner setBaseWorkProgress(int baseWorkProgress)
  {
    this.baseWorkProgress = baseWorkProgress;
    return this;
  }
  
  public int getWorkComplete()
  {
    return workComplete;
  }
  
  public TileMiner setWorkComplete(int workComplete)
  {
    this.workComplete = workComplete;
    return this;
  }
  
  public int getWorkCost()
  {
    return workCost;
  }
  
  public TileMiner setWorkCost(int workCost)
  {
    this.workCost = workCost;
    return this;
  }
  
  @Override
  public ITextComponent getDisplayName()
  {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }
  
  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity)
  {
    return new MinerContainer(i, world, pos, playerInventory);
  }
  
  public ItemStack getTarget()
  {
    if (targetInfo != null)
    {
      return targetInfo.target;
    }
    return ItemStack.EMPTY;
  }
}

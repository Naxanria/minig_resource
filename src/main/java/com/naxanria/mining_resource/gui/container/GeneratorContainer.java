package com.naxanria.mining_resource.gui.container;

import com.naxanria.mining_resource.block.ModBlocks;
import com.naxanria.mining_resource.tile.TileGenerator;
import com.naxanria.mining_resource.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class GeneratorContainer extends BaseContainer<TileGenerator>
{
  public GeneratorContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.GENERATOR, id, world, pos, playerInventory);
    
    
    tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent
    (
      inv ->
      {
        addSlot(new SlotItemHandler(inv, 0, 80, 36)
        {
          @Override
          public void putStack(@Nonnull ItemStack stack)
          {
            super.putStack(stack);
          }
  
          @Nonnull
          @Override
          public ItemStack decrStackSize(int amount)
          {
            ItemStack stackInSlot = inv.getStackInSlot(slotNumber);
            if (amount > stackInSlot.getCount())
            {
              ((ItemStackHandler) inv).setStackInSlot(slotNumber, ItemStack.EMPTY);
              return stackInSlot;
            }
            
            ItemStack toReturn = stackInSlot.copy();
            toReturn.setCount(amount);
            stackInSlot.setCount(stackInSlot.getCount() - amount);
            ((ItemStackHandler) inv).setStackInSlot(slotNumber, stackInSlot.getCount() == 0 ? ItemStack.EMPTY : stackInSlot);
  
            return toReturn;
          }
  
          @Override
          public boolean canTakeStack(PlayerEntity playerIn)
          {
            return true;
          }
        });
      }
    );
    
    trackInt(new IntReferenceHolder()
    {
      @Override
      public int get()
      {
        return tileEntity.getEnergy();
      }

      @Override
      public void set(int value)
      {
        tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> ((CustomEnergyStorage) e).setEnergy(value));
      }
    });

    trackInt(new IntReferenceHolder()
    {
      @Override
      public int get()
      {
        return tileEntity.getBurnTime();
      }

      @Override
      public void set(int value)
      {
        tileEntity.setBurnTime(value);
      }
    });

    trackInt(new IntReferenceHolder()
    {
      @Override
      public int get()
      {
        return tileEntity.getTotalBurnTime();
      }

      @Override
      public void set(int value)
      {
        tileEntity.setTotalBurnTime(value);
      }
    });
    
    layoutPlayerInventorySlots(8, 84);
  }
  
  @Override
  public ItemStack transferStackInSlot(PlayerEntity p_82846_1_, int p_82846_2_)
  {
    return super.transferStackInSlot(p_82846_1_, p_82846_2_);
  }
  
  @Override
  public boolean canInteractWith(PlayerEntity playerIn)
  {
    return canInteractWith(playerIn, ModBlocks.GENERATOR);
  }
}

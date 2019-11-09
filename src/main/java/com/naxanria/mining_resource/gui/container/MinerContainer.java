package com.naxanria.mining_resource.gui.container;

import com.naxanria.mining_resource.block.ModBlocks;
import com.naxanria.mining_resource.tile.TileMiner;
import com.naxanria.mining_resource.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/*
  @author: Naxanria
*/
public class MinerContainer extends BaseContainer<TileMiner>
{
  public MinerContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(ModContainers.MINER, id, world, pos, playerInventory);
    
    tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent
    (
      inv ->
      {
        addSlot(new SlotItemHandler(inv, 0, 134, 36));
      }
    );
    
    addSlot(new SlotItemHandler(tileEntity.targetSlot, 0, 43, 12));
    
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
        return tileEntity.getCurrentWork();
      }
  
      @Override
      public void set(int value)
      {
        tileEntity.setCurrentWork(value);
      }
    });
    
    trackInt(new IntReferenceHolder()
    {
      @Override
      public int get()
      {
        return tileEntity.getWorkComplete();
      }
  
      @Override
      public void set(int i)
      {
        tileEntity.setWorkComplete(i);
      }
    });
    
    trackInt(new IntReferenceHolder()
    {
      @Override
      public int get()
      {
        return tileEntity.getWorkCost();
      }
  
      @Override
      public void set(int i)
      {
        tileEntity.setWorkCost(i);
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
    return canInteractWith(playerIn, ModBlocks.MINER_IRON);
  }
}

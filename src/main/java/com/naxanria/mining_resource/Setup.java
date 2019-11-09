package com.naxanria.mining_resource;

import com.naxanria.mining_resource.block.ModBlocks;
import com.naxanria.mining_resource.item.TargetingManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/*
  @author: Naxanria
*/
public class Setup
{
  public static ItemGroup group = new ItemGroup("mining_resource")
  {
    @Override
    public ItemStack createIcon()
    {
      return new ItemStack(ModBlocks.MINER_IRON);
    }
  };
  
  public static void init()
  {
    TargetingManager.generateDefaults();
    // todo load from file etc
  }
}

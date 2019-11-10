package com.naxanria.mining_resource.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/*
  @author: Naxanria
*/
public class TargetInfo
{
  public final int id;
  public final ItemStack target;
  public final int powerCost;
  public final int progressPerWork;
  public final Block supplierBlock;
  public final ItemStack catalyst;
  public final int catalystPower;
  
  public TargetInfo(int id, ItemStack target, int powerCost, int progressPerWork)
  {
    this(id, target, powerCost, progressPerWork, null, null, 0);
  }
  
  public TargetInfo(int id, ItemStack target, int powerCost, int progressPerWork, Block supplierBlock, ItemStack catalyst, int catalystPower)
  {
    this.id = id;
    this.target = target;
    this.powerCost = powerCost;
    this.progressPerWork = progressPerWork;
    this.supplierBlock = supplierBlock;
    this.catalyst = catalyst;
    this.catalystPower = catalystPower;
  }
}

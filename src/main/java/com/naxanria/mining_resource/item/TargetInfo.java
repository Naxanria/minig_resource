package com.naxanria.mining_resource.item;

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
  
  public TargetInfo(int id, ItemStack target, int powerCost, int progressPerWork)
  {
    this.id = id;
    this.target = target;
    this.powerCost = powerCost;
    this.progressPerWork = progressPerWork;
  }
}

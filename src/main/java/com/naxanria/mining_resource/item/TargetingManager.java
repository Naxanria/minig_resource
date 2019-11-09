package com.naxanria.mining_resource.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
  @author: Naxanria
*/
public class TargetingManager
{
  private static Map<Integer, TargetInfo> infoMap = new HashMap<>();
  
  public static void register(TargetInfo targetInfo)
  {
    infoMap.put(targetInfo.id, targetInfo);
  }
  
  public static void load(File file)
  {
  
  }
  
  public static void generateDefaults()
  {
    int id = 1;
    TargetInfo ironTarget = new TargetInfo(id++, new ItemStack(Items.IRON_INGOT), 300, 30);
    TargetInfo goldTarget = new TargetInfo(id++, new ItemStack(Items.GOLD_INGOT), 500, 50);
    TargetInfo cobbleTarget = new TargetInfo(id++, new ItemStack(Items.COBBLESTONE, 3), 50, 100);
    TargetInfo netherQuartzTarget = new TargetInfo(id++, new ItemStack(Items.QUARTZ, 2), 150, 30);
    
    register(ironTarget);
    register(goldTarget);
    register(cobbleTarget);
    register(netherQuartzTarget);
  }
  
  public static TargetInfo getInfo(int id)
  {
    return infoMap.getOrDefault(id, null);
  }
  
  public static TargetInfo getInfo(ItemStack stack)
  {
    if (stack.getItem() == ModItems.TARGETING_MODULE)
    {
      return getInfo(TargetingModule.getTarget(stack));
    }
    else
    {
      return null;
    }
  }
  
  public static ItemStack getTargetItem(int id)
  {
    ItemStack stack = new ItemStack(ModItems.TARGETING_MODULE);
    TargetingModule.setTarget(stack, id);
    
    return stack;
  }
  
  public static Iterator<TargetInfo> getIterator()
  {
    return infoMap.values().iterator();
  }
}

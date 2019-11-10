package com.naxanria.mining_resource.util.json;

import com.google.gson.JsonObject;
import com.naxanria.mining_resource.MR;
import com.naxanria.mining_resource.item.TargetInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

/*
  @author: Naxanria
*/
public class TargetInfoSerializer implements IJsonSerializer<TargetInfo>
{
  @Override
  public JsonObject serialize(TargetInfo info)
  {
    JsonObject object = new JsonObject();
    
    object.addProperty("id", info.id);
    ItemStack stack = info.target;
    String itemId = stack.getItem().getRegistryName().toString();
    int count = stack.getCount();
//    CompoundNBT nbt = stack.getTag();
    object.addProperty("target", itemId);
    object.addProperty("count", count);
    
    object.addProperty("powerCost", info.powerCost);
    object.addProperty("progressPerWork", info.progressPerWork);
    if (info.supplierBlock != null)
    {
      object.addProperty("supplierBlock", info.supplierBlock.getRegistryName().toString());
    }
    if (info.catalyst != null)
    {
      object.addProperty("catalyst", info.catalyst.getItem().getRegistryName().toString());
      object.addProperty("catalystPower", info.catalystPower);
    }
    
    return object;
  }
  
  @Override
  public TargetInfo deserialize(JsonObject json)
  {
    int id = JSONUtils.getInt(json, "id", -1);
    if (id < 0)
    {
      MR.LOGGER.error("Cant have an id that is lower than 0 in the target json!");
      return null;
    }
    
    String targetID = JSONUtils.getString(json, "target", "minecraft:poppy");
    int count = JSONUtils.getInt(json, "count", 1);
    int powerCost = JSONUtils.getInt(json, "powerCost", 300);
    int progressPerWork = JSONUtils.getInt(json, "progressPerWork", 10);
    String supplierBlockID = JSONUtils.getString(json, "supplierBlock", "");
    String catalystID = JSONUtils.getString(json, "catalyst", "");
    int catalystPower = JSONUtils.getInt(json, "catalystPower", 10);
  
    Item targetItem = Registry.ITEM.getOrDefault(new ResourceLocation(targetID));
    if (targetItem == Items.AIR)
    {
      MR.LOGGER.error("Could not find item for " + targetID);
      return null;
    }
    
    
    ItemStack target = new ItemStack(targetItem, Math.max(count, 1));
    if (target.getCount() > target.getMaxStackSize())
    {
      target.setCount(target.getMaxStackSize());
    }
    
    return new TargetInfo(id, target, powerCost, progressPerWork, null, null, 0);
  }
}

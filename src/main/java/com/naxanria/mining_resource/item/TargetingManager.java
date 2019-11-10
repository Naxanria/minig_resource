package com.naxanria.mining_resource.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.naxanria.mining_resource.MR;
import com.naxanria.mining_resource.util.ConfigUtil;
import com.naxanria.mining_resource.util.json.JsonProvider;
import com.naxanria.mining_resource.util.json.Serializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    JsonObject targets = JsonProvider.readFromDisk(file);
    if (!targets.has("targets"))
    {
      MR.LOGGER.error("No targets found in the targets file.");
      return;
    }
    
    JsonArray array = JSONUtils.getJsonArray(targets, "targets", null);
    if (array == null)
    {
      MR.LOGGER.error("'targets' needs to be an array.");
      return;
    }
  
    for (int i = 0; i < array.size(); i++)
    {
      if (!array.get(i).isJsonObject())
      {
        MR.LOGGER.error("Incorrect format for target info. Element {} is not correct", i);
        return;
      }
      
      JsonObject obj = array.get(i).getAsJsonObject();
      TargetInfo info = Serializers.TARGET_INFO_SERIALIZER.deserialize(obj);
      if (info != null)
      {
        register(info);
      }
    }
    
    MR.LOGGER.info("Loaded {} targets", infoMap.size());
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
  
    JsonObject targets = new JsonObject();
    JsonArray jsonElements = new JsonArray();
    
    for (TargetInfo info : infoMap.values())
    {
      JsonObject json = Serializers.TARGET_INFO_SERIALIZER.serialize(info);
      if (json != null)
      {
        jsonElements.add(json);
      }
    }
    
    targets.add("targets", jsonElements);
  
    JsonProvider.writeToDisk(ConfigUtil.getConfigSubFile("example_targets.json"), targets);
  
    File targetsFile = ConfigUtil.getConfigSubFile("targets.json");
    if (!targetsFile.exists())
    {
      JsonProvider.writeToDisk(ConfigUtil.getConfigSubFile("targets.json"), targets);
    }
    else
    {
      infoMap.clear();
      load(targetsFile);
    }
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

package com.naxanria.mining_resource;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import com.naxanria.mining_resource.block.BaseMinerBlock;
import com.naxanria.mining_resource.block.Generator;
import com.naxanria.mining_resource.block.ModBlocks;
import com.naxanria.mining_resource.gui.container.GeneratorContainer;
import com.naxanria.mining_resource.gui.container.MinerContainer;
import com.naxanria.mining_resource.item.TargetingModule;
import com.naxanria.mining_resource.tile.TileGenerator;
import com.naxanria.mining_resource.tile.TileMiner;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

/*
  @author: Naxanria
*/
public class Registrator
{
  private static List<BlockItem> blockItems = new ArrayList<>();
  private static IForgeRegistry<Block> blockRegistry;
  private static IForgeRegistry<Item> itemRegistry;
  
  public static void registerBlocks(RegistryEvent.Register<Block> event)
  {
    blockRegistry = event.getRegistry();
    
    Block.Properties machineProperties = Block.Properties.create(Material.IRON).hardnessAndResistance(2).harvestTool(ToolType.PICKAXE).harvestLevel(1);
    
    register(new BaseMinerBlock(machineProperties), "miner_iron", true, new Item.Properties().group(Setup.group));
    register(new Generator(machineProperties), "generator", true, new Item.Properties().group(Setup.group));
  }
  
  public static void registerItems(RegistryEvent.Register<Item> event)
  {
    itemRegistry = event.getRegistry();
    for (BlockItem blockItem : blockItems)
    {
      itemRegistry.register(blockItem);
    }
    
    blockItems.clear();
    
    register(new TargetingModule(new Item.Properties().maxStackSize(1)), "targeting_module");
  }
  
  public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event)
  {
//    Type<?> type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getVersion().getWorldVersion())).getChoiceType(TypeReferences.BLOCK_ENTITY, "miner_iron");
    event.getRegistry().register(TileEntityType.Builder.create(TileMiner::new, ModBlocks.MINER_IRON).build(null).setRegistryName(MR.MODID, "miner_iron"));
    event.getRegistry().register(TileEntityType.Builder.create(TileGenerator::new, ModBlocks.GENERATOR).build(null).setRegistryName(MR.MODID, "generator"));
  }
  
  public static void registerContainer(RegistryEvent.Register<ContainerType<?>> event)
  {
    IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
    
    registry.register(IForgeContainerType.create((windowId, inv, data) ->
      {
        BlockPos pos = data.readBlockPos();
        return new GeneratorContainer(windowId, Minecraft.getInstance().world, pos, inv);
      }
    ).setRegistryName(MR.MODID, "generator"));
    
    registry.register(IForgeContainerType.create((windowId, inv, data) ->
    {
      BlockPos pos = data.readBlockPos();
      return new MinerContainer(windowId, Minecraft.getInstance().world, pos, inv);
    }).setRegistryName(MR.MODID, "miner"));
  }
  
  private static <T extends Block> T register(T block, String registryName)
  {
    return register(block, registryName, true, null);
  }
  
  private static <T extends Block> T register(T block, String registryName, boolean createBlockItem, Item.Properties blockItemProperties)
  {
    block.setRegistryName(MR.MODID, registryName);
    if (createBlockItem)
    {
      if (blockItemProperties == null)
      {
        blockItemProperties = new Item.Properties();
      }
      blockItems.add((BlockItem) new BlockItem(block, blockItemProperties).setRegistryName(MR.MODID, registryName));
    }
    
    blockRegistry.register(block);
    
    return block;
  }
  
  private static <T extends Item> T register(T item, String name)
  {
    itemRegistry.register(item.setRegistryName(MR.MODID, name));
    return item;
  }
}

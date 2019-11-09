package com.naxanria.mining_resource.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BaseTileBlock<TE extends TileEntity> extends Block
{
  protected boolean hasGui = true;
  
  public interface TileEntityProvider<TE extends TileEntity>
  {
    default TE get()
    {
      return get(null, null);
    }
    
    TE get(IBlockReader world, BlockState state);
  }
  
  private final TileEntityProvider<TE> tileEntityProvider;
  
  
  public BaseTileBlock(Properties properties, TileEntityProvider<TE> tileEntityProvider)
  {
    super(properties);
  
    this.tileEntityProvider = tileEntityProvider;
  }
  
  @Override
  public boolean hasTileEntity(BlockState state)
  {
    return true;
  }
  
  @Nullable
  @Override
  public TE createTileEntity(BlockState state, IBlockReader world)
  {
    return tileEntityProvider.get(world, state);
  }
  
  @Override
  public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult hit)
  {
    if (!world.isRemote && hasGui)
    {
      if (!playerEntity.isSneaking())
      {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof INamedContainerProvider)
        {
          NetworkHooks.openGui((ServerPlayerEntity) playerEntity, (INamedContainerProvider) tileEntity, tileEntity.getPos());
          
          return true;
        }
      }
    }
    
    return super.onBlockActivated(state, world, pos, playerEntity, hand, hit);
  }
}

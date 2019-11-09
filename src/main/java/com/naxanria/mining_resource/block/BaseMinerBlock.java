package com.naxanria.mining_resource.block;

import com.naxanria.mining_resource.tile.TileMiner;

public class BaseMinerBlock extends BaseTileBlock<TileMiner>
{
  public BaseMinerBlock(Properties properties)
  {
    super(properties, (world, state) -> new TileMiner());
  }
}

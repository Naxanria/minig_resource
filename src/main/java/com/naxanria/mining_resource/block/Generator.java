package com.naxanria.mining_resource.block;

import com.naxanria.mining_resource.tile.TileGenerator;

/*
  @author: Naxanria
*/
public class Generator extends BaseTileBlock<TileGenerator>
{
  public Generator(Properties properties)
  {
    super(properties, (world, state) -> new TileGenerator());
  }
  

}

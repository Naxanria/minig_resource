package com.naxanria.mining_resource;

/*
  @author: Naxanria
*/

import com.naxanria.mining_resource.gui.container.ModContainers;
import com.naxanria.mining_resource.gui.screen.GeneratorScreen;
import com.naxanria.mining_resource.gui.screen.MinerScreen;
import net.minecraft.client.gui.ScreenManager;

public class ClientSetup
{
  public static void init()
  {
    ScreenManager.registerFactory(ModContainers.GENERATOR, GeneratorScreen::new );
    ScreenManager.registerFactory(ModContainers.MINER, MinerScreen::new );
  }
}

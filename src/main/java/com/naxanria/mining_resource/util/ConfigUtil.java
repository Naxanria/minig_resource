package com.naxanria.mining_resource.util;

import com.naxanria.mining_resource.MR;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.nio.file.Path;

/*
  @author: Naxanria
*/
public class ConfigUtil
{
  private static Path configFolder;
  
  public static Path createConfigFolder()
  {
    configFolder = FMLPaths.CONFIGDIR.get().resolve(MR.MODID + "/");
    File configFolderFile = configFolder.toFile();
    if (!configFolderFile.exists())
    {
      if (configFolderFile.mkdir())
      {
        MR.LOGGER.info("Created config folder");
      }
      else
      {
        MR.LOGGER.warn("failed to create config folder");
      }
    }
    
    return configFolder;
  }
  
  public static File getConfigSubFile(String fileName)
  {
    if (configFolder == null)
    {
      createConfigFolder();
    }
    return new File(configFolder.toFile(), fileName);
  }
}

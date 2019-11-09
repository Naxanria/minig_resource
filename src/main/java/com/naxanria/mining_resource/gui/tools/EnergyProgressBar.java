package com.naxanria.mining_resource.gui.tools;

import net.minecraftforge.energy.IEnergyStorage;

import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class EnergyProgressBar extends ProgressBar
{
  public EnergyProgressBar(int x, int y, int width, int height, IEnergyStorage storage)
  {
    super(x, y, width, height, storage::getMaxEnergyStored, storage::getEnergyStored);
  }
}

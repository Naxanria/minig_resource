package com.naxanria.mining_resource.tools;

/*
  @author: Naxanria
*/

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage
{
  protected boolean canExtract = true;
  protected boolean canReceive = true;
  
  public CustomEnergyStorage(int capacity)
  {
    super(capacity);
  }
  
  public CustomEnergyStorage(int capacity, int maxTransfer)
  {
    super(capacity, maxTransfer);
  }
  
  public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract)
  {
    super(capacity, maxReceive, maxExtract);
  }
  
  public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
  {
    super(capacity, maxReceive, maxExtract, energy);
  }
  
  @Override
  public boolean canExtract()
  {
    return canExtract;
  }
  
  @Override
  public boolean canReceive()
  {
    return canReceive;
  }
  
  public CustomEnergyStorage setCanExtract(boolean canExtract)
  {
    this.canExtract = canExtract;
    return this;
  }
  
  public CustomEnergyStorage setCanReceive(boolean canReceive)
  {
    this.canReceive = canReceive;
    return this;
  }
  
  protected int limitEnergy()
  {
    return energy = MathHelper.clamp(energy, 0, capacity);
  }
  
  public void insertInternal(int amount)
  {
    energy = MathHelper.clamp(energy + amount, 0, capacity);
  }
  
  public void extractInternal(int amount)
  {
    insertInternal(-amount);
  }
  
  public float getFullness()
  {
    return energy / (float) capacity;
  }
  
  public void setEnergy(int energy)
  {
    this.energy = energy;
  }
}

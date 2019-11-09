package com.naxanria.mining_resource.gui.tools;

import net.minecraft.util.IntReferenceHolder;

import java.util.concurrent.atomic.AtomicInteger;

/*
  @author: Naxanria
*/
public class AtomicReferenceHolder extends IntReferenceHolder
{
  private final AtomicInteger integer;
  
  public AtomicReferenceHolder(AtomicInteger integer)
  {
    this.integer = integer;
  }
  
  @Override
  public int get()
  {
    return integer.get();
  }
  
  @Override
  public void set(int value)
  {
    integer.set(value);
  }
}

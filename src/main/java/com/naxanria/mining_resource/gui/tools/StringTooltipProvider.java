package com.naxanria.mining_resource.gui.tools;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
  @author: Naxanria
*/
public class StringTooltipProvider implements ITooltipProvider
{
  public Region region;
  public List<String> strings;
  
  public StringTooltipProvider(Region region, String... strings)
  {
    this.region = region;
    this.strings = Arrays.asList(strings);
  }
  
  public StringTooltipProvider(int x, int y, int width, int height, String... strings)
  {
    this(new Region(x, y, width, height), strings);
  }
  
  @Override
  public boolean isHover(int mouseX, int mouseY)
  {
    return region.isInside(mouseX, mouseY);
  }
  
  @Override
  public List<String> getTooltip()
  {
    return strings;
  }
}

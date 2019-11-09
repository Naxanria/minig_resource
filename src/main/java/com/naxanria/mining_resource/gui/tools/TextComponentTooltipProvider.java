package com.naxanria.mining_resource.gui.tools;

import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
  @author: Naxanria
*/
public class TextComponentTooltipProvider implements ITooltipProvider
{
  public Region region;
  public List<ITextComponent> components;
  
  public TextComponentTooltipProvider(int x, int y, int width, int height, ITextComponent... textComponents)
  {
    region = new Region(x, y, width, height);
    this.components = Arrays.asList(textComponents);
  }
  
  @Override
  public boolean isHover(int mouseX, int mouseY)
  {
    return region.isInside(mouseX, mouseY);
  }
  
  @Override
  public List<String> getTooltip()
  {
    List<String> strings = new ArrayList<>();
    for(ITextComponent itextcomponent : components) {
      strings.add(itextcomponent.getFormattedText());
    }
    return strings;
  }
}

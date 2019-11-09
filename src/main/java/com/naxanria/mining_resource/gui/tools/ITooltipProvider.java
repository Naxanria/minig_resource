package com.naxanria.mining_resource.gui.tools;

import net.minecraft.util.text.ITextComponent;

import java.util.List;

/*
  @author: Naxanria
*/
public interface ITooltipProvider
{
  boolean isHover(int mouseX, int mouseY);

  List<String> getTooltip();
}

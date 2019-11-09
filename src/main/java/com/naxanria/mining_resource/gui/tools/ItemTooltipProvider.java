package com.naxanria.mining_resource.gui.tools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class ItemTooltipProvider implements ITooltipProvider
{
  public ItemStack stack;
  public Region region;
  private Supplier<ItemStack> itemStackSupplier;
  public boolean onlyShowWithValidStack = true;
  
  public ItemTooltipProvider(Supplier<ItemStack> stackSupplier, int x, int y, int width, int height)
  {
    region = new Region(x, y, width, height);
    itemStackSupplier = stackSupplier;
  }
  
  public ItemTooltipProvider(ItemStack stack, int x, int y, int width, int height)
  {
    this(() -> stack, x, y, width, height);
  }
  
  @Override
  public boolean isHover(int mouseX, int mouseY)
  {
    if (onlyShowWithValidStack)
    {
      if (getStack().isEmpty())
      {
        return false;
      }
    }
    return region.isInside(mouseX, mouseY);
  }
  
  protected ItemStack getStack()
  {
    ItemStack properStack = stack;
    if (properStack == null)
    {
      if (itemStackSupplier != null)
      {
        properStack = itemStackSupplier.get();
      }
      else
      {
        properStack = ItemStack.EMPTY;
      }
    }
    
    return properStack;
  }
  
  public ItemTooltipProvider onlyShowWithValidStack(boolean onlyShowWithValidStack)
  {
    this.onlyShowWithValidStack = onlyShowWithValidStack;
    return this;
  }
  
  @Override
  public List<String> getTooltip()
  {

  
    List<ITextComponent> tooltip = getStack().getTooltip(Minecraft.getInstance().player, Minecraft.getInstance().gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
    List<String> strings = new ArrayList<>();
  
    for (ITextComponent component : tooltip)
    {
      strings.add(component.getFormattedText());
    }
    
    return strings;
  }
}

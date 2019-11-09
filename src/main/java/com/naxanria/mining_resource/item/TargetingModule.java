package com.naxanria.mining_resource.item;

import com.naxanria.mining_resource.Setup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

/*
  @author: Naxanria
*/
public class TargetingModule extends Item
{
  public TargetingModule(Properties properties)
  {
    super(properties);
  }
  
  @Override
  public boolean isEnchantable(ItemStack stack)
  {
    return false;
  }
  
  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
  {
    super.addInformation(stack, world, tooltip, flag);
    TargetInfo info = TargetingManager.getInfo(getTarget(stack));
    if (info != null)
    {
      tooltip.add(new TranslationTextComponent(info.target.getTranslationKey()).setStyle(new Style().setBold(true).setColor(TextFormatting.AQUA)));
      
      if (flag == ITooltipFlag.TooltipFlags.ADVANCED)
      {
        tooltip.add(new StringTextComponent("ID: " + info.id).setStyle(new Style().setColor(TextFormatting.DARK_GRAY)));
      }
    }
  }
  
  public static void setTarget(ItemStack stack, int targetId)
  {
    stack.getOrCreateTag().putInt("targetID", targetId);
  }
  
  public static int getTarget(ItemStack stack)
  {
    CompoundNBT tag = stack.getTag();
    if (tag != null)
    {
      if (tag.contains("targetID"))
      {
        return tag.getInt("targetID");
      }
    }
    
    return -1;
  }
  
  @Override
  public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> stacks)
  {
    if (group == ItemGroup.SEARCH || group == Setup.group)
    {
      Iterator<TargetInfo> iterator = TargetingManager.getIterator();
      while (iterator.hasNext())
      {
        TargetInfo targetInfo = iterator.next();
        stacks.add(TargetingManager.getTargetItem(targetInfo.id));
      }
    }
  }
}

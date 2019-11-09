package com.naxanria.mining_resource.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.naxanria.mining_resource.MR;
import com.naxanria.mining_resource.gui.container.GeneratorContainer;
import com.naxanria.mining_resource.gui.container.MinerContainer;
import com.naxanria.mining_resource.gui.tools.*;
import com.naxanria.mining_resource.tile.TileGenerator;
import com.naxanria.mining_resource.tile.TileMiner;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
  @author: Naxanria
*/
public class MinerScreen extends BaseContainerScreen<MinerContainer>
{
  private static final ResourceLocation GUI = new ResourceLocation(MR.MODID, "textures/gui/container/miner.png");
  
  private ProgressBar energyBar;
  private ProgressBar progress;
  
  public MinerScreen(MinerContainer container, PlayerInventory playerInventory, ITextComponent title)
  {
    super(container, playerInventory, title);
  
    TileMiner tileEntity = container.tileEntity;
    tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> energyBar = new EnergyProgressBar(10, 10, 25, 60, energy));
    progress = new ProgressBar(38, 42, 88, 5, tileEntity::getWorkComplete, tileEntity::getCurrentWork).setFillDirection(ProgressBar.FillDirection.LEFT_RIGHT).setColor(0xffffffff);
  
    if (energyBar != null)
    {
      addTooltipProvider(new ITooltipProvider()
      {
        @Override
        public boolean isHover(int mouseX, int mouseY)
        {
          return energyBar.mouseOver(mouseX, mouseY);
        }
  
        @Override
        public List<String> getTooltip()
        {
          List<String> tooltip = new ArrayList<>();
          tooltip.add("Energy");
          tooltip.add(energyBar.getCurrStorage() + "/" + energyBar.getCurrCapacity() + " RF");
          tooltip.add(tileEntity.getWorkCost() + " RF/t");
          
          return tooltip;
        }
      });
    }
    
    addTooltipProvider(new ITooltipProvider()
    {
      @Override
      public boolean isHover(int mouseX, int mouseY)
      {
        return progress.mouseOver(mouseX, mouseY);
      }
  
      @Override
      public List<String> getTooltip()
      {
        return Arrays.asList("Progress", progress.getCurrStorage() + "/" + progress.getCurrCapacity() + " Work");
      }
    });
    
    addTooltipProvider(new ItemTooltipProvider(tileEntity::getTarget, 72, 12, 18, 18).onlyShowWithValidStack(true));
  }
  
  @Override
  public void tick()
  {
    if (energyBar != null)
    {
      energyBar.update();
    }
    progress.update();
    
    super.tick();
  }
  
  @Override
  public void render(int mouseX, int mouseY, float partialTicks)
  {
    renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    renderTooltip(mouseX, mouseY);
  }
//
//  protected void renderTooltip(int mouseX, int mouseY)
//  {
//    if (this.minecraft.player.inventory.getItemStack().isEmpty())
//    {
//      if (this.hoveredSlot != null && this.hoveredSlot.getHasStack())
//      {
//        this.renderTooltip(this.hoveredSlot.getStack(), mouseX, mouseY);
//      }
//      else
//      {
//        int i = (this.width - this.xSize) / 2;
//        int j = (this.height - this.ySize) / 2;
//        TileMiner tileEntity = container.tileEntity;
//
//        if (energyBar.mouseOver(mouseX - i, mouseY - j))
//        {
//          // energy tooltip
//          renderTooltip
//          (
//            Arrays.asList
//            (
//              "Energy",
//              tileEntity.getEnergy() + "/" + tileEntity.getCapacity() + " RF",
//              tileEntity.getWorkCost() + " RF/t"
//            )
//            ,mouseX, mouseY
//          );
//        }
//        else if (inRegion(mouseX, mouseY, 79 + i, 12 + j, 18, 18))
//        {
//          // target stack tooltip
//          ItemStack target = tileEntity.getTarget();
//          if (target != null && !target.isEmpty())
//          {
//            renderTooltip(target, mouseX, mouseY);
//          }
//        }
//      }
//    }
//  }
  
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.minecraft.getTextureManager().bindTexture(GUI);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    blit(i, j, 0, 0, this.xSize, this.ySize);
    
    if (energyBar != null)
    {
      energyBar.draw(i, j);
    }
    progress.draw(i, j);
  
    ItemStack target = container.tileEntity.getTarget();
    if (target != null && !target.isEmpty())
    {
      //79,12
      drawStack(target, 79 + i, 12 + j, target.getCount() > 1 ? target.getCount() + "" : "");
    }

  }
  
  public void drawStack(ItemStack stack, int x, int y, String altText)
  {
    GlStateManager.translatef(0.0F, 0.0F, 32.0F);
    this.blitOffset = 200;
    this.itemRenderer.zLevel = 200.0F;
    net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
    if (font == null) font = this.font;
    this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
    this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y, altText);
    this.blitOffset = 0;
    this.itemRenderer.zLevel = 0.0F;
  }
  
  @Override
  protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
  {
//    drawString(font, "Energy: " + container.tileEntity.getEnergy() + "/" + container.tileEntity.getCapacity() , 8, 6, 0xffffffff);
//    drawString(font, "Burntime: " + container.tileEntity.getBurnTime() + "/" + container.tileEntity.getTotalBurnTime() , 8, 20, 0xffffffff);
  }
}

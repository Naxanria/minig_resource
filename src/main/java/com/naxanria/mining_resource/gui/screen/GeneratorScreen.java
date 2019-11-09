package com.naxanria.mining_resource.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.naxanria.mining_resource.MR;
import com.naxanria.mining_resource.gui.container.GeneratorContainer;
import com.naxanria.mining_resource.gui.tools.EnergyProgressBar;
import com.naxanria.mining_resource.gui.tools.ITooltipProvider;
import com.naxanria.mining_resource.gui.tools.ProgressBar;
import com.naxanria.mining_resource.tile.TileGenerator;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.Arrays;
import java.util.List;

/*
  @author: Naxanria
*/
public class GeneratorScreen extends BaseContainerScreen<GeneratorContainer>
{
  private static final ResourceLocation GUI = new ResourceLocation(MR.MODID, "textures/gui/container/generator.png");
  
  private ProgressBar energyBar;
  private ProgressBar burnTimer;
  
  public GeneratorScreen(GeneratorContainer container, PlayerInventory playerInventory, ITextComponent title)
  {
    super(container, playerInventory, title);
  
    TileGenerator tileEntity = container.tileEntity;
    tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> energyBar = new EnergyProgressBar(10, 10, 25, 60, energy).setEmptyColor(0xffff0000).setFullColor(0xff00ff00).setGradient(true));
    burnTimer = new ProgressBar(36, 10, 5, 60).setColor(0xffffffff);//.useReverseProgress(true);
    
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
          return Arrays.asList("Energy", energyBar.getCurrStorage() + "/" + energyBar.getCurrCapacity() + " RF", "Generating: " + tileEntity.getGenerating() + " RF/t");
        }
      });
    }
    
    addTooltipProvider(new ITooltipProvider()
    {
      @Override
      public boolean isHover(int mouseX, int mouseY)
      {
        return burnTimer.mouseOver(mouseX, mouseY);
      }
  
      @Override
      public List<String> getTooltip()
      {
        return Arrays.asList("Fuel", burnTimer.getCurrStorage() + "/" + burnTimer.getCurrCapacity());
      }
    });
  }
  
  @Override
  public void tick()
  {
    if (energyBar != null)
    {
      energyBar.update();
    }
    
    int burnTime = container.tileEntity.getBurnTime();
    
    if (burnTime == 0)
    {
      burnTimer.setProgress(0, 0);
    }
    else
    {
      burnTimer.setProgress(burnTime, container.tileEntity.getTotalBurnTime());
    }
    
    burnTimer.update();
    
    super.tick();
  }
  
  @Override
  public void render(int mouseX, int mouseY, float partialTicks)
  {
    renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    renderTooltip(mouseX, mouseY);
  }
  
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
    burnTimer.draw(i, j);
  }
  
  @Override
  protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
  {
//    drawString(font, "Energy: " + container.tileEntity.getEnergy() + "/" + container.tileEntity.getCapacity() , 8, 6, 0xffffffff);
//    drawString(font, "Burntime: " + container.tileEntity.getBurnTime() + "/" + container.tileEntity.getTotalBurnTime() , 8, 20, 0xffffffff);
  }
}

package com.naxanria.mining_resource.gui.tools;

import com.naxanria.mining_resource.util.ColorUtil;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class ProgressBar extends AbstractGui
{
  protected int x;
  protected int y;
  protected int width;
  protected int height;
  protected FillDirection fillDirection = FillDirection.BOTTOM_TOP;
  
  protected boolean border = true;
  protected int borderThickness = 1;
  protected int borderColor = 0xff000000;
  
  protected boolean background = true;
  protected int backgroundColor = 0x88000000;
  
  protected boolean gradient = false;
  protected int fullColor = 0xff00ff00;
  protected int emptyColor = 0xffff0000;
  
  protected int color = 0xff00ff00;
  
  protected boolean useReverseProgress = false;
  
  protected Supplier<Integer> capacitySupplier;
  protected Supplier<Integer> storageSupplier;
  
  protected int currCapacity = 0;
  protected int currStorage = 0;
  protected int currentColor = color;
  protected float currentProgress = 0;
  
  
  public ProgressBar(int x, int y, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public ProgressBar(int x, int y, int width, int height, Supplier<Integer> capacitySupplier, Supplier<Integer> storageSupplier)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.capacitySupplier = capacitySupplier;
    this.storageSupplier = storageSupplier;
  }
  
  public ProgressBar setProgress(int storage)
  {
    return setProgress(storage, currCapacity);
  }
  
  public ProgressBar setProgress(int storage, int capacity)
  {
    currStorage = storage;
    currCapacity = capacity;
    
    return this;
  }
  
  public void update()
  {
    if (capacitySupplier != null)
    {
      currCapacity = capacitySupplier.get();
    }
    if (storageSupplier != null)
    {
      currStorage = storageSupplier.get();
    }
    
    currentProgress = progress();
    if (useReverseProgress)
    {
      currentProgress = 1f - progress();
    }
    
    if (gradient)
    {
      currentColor = ColorUtil.lerp(currentProgress, emptyColor, fullColor);
    }
    else
    {
      currentColor = color;
    }
  }
  
  public float progress()
  {
    if (currCapacity == 0)
    {
      return 0;
    }
  
    float val = MathHelper.clamp(currStorage / (float) currCapacity, 0f, 1f);
    
    return val;
  }
  
  public void draw()
  {
    draw(0, 0);
  }
  
  public void draw(int xOffset, int yOffset)
  {
    int x = this.x + xOffset;
    int y = this.y + yOffset;
    
    int fillHeight = height;
    int fillWidth = width;
    int fillX = x;
    int fillY = y;
    
    switch (fillDirection)
    {
      case LEFT_RIGHT:
        fillWidth = (int) (fillWidth * currentProgress);
        break;
      case RIGHT_LEFT:
        fillWidth = (int) (fillWidth * currentProgress);
        fillX = x + width - fillWidth;
        break;
      case BOTTOM_TOP:
        fillHeight = (int) (fillHeight * currentProgress);
        fillY = y + height - fillHeight;
        break;
      case TOP_BOTTOM:
        fillHeight = (int) (fillHeight * currentProgress);
        break;
    }
    
    if (background)
    {
      rect(x, y, width, height, backgroundColor);
    }
    
    rect(fillX, fillY, fillWidth, fillHeight, currentColor);
    
    if (border)
    {
      // top
      rect(x, y, width, borderThickness, borderColor);
      // left
      rect(x, y, borderThickness, height, borderColor);
      // right
      rect(x + width - borderThickness, y, borderThickness, height, borderColor);
      // bottom
      rect(x, y + height - borderThickness, width, borderThickness, borderColor);
    }
  }
  
  protected void rect(int x, int y, int width, int height, int color)
  {
    fill(x, y, x + width, y + height, color);
  }
  
  public ProgressBar setX(int x)
  {
    this.x = x;
    return this;
  }
  
  public ProgressBar setY(int y)
  {
    this.y = y;
    return this;
  }
  
  public ProgressBar setLocation(int x, int y)
  {
    this.x = x;
    this.y = y;
    
    return this;
  }
  
  public ProgressBar setWidth(int width)
  {
    this.width = width;
    return this;
  }
  
  public ProgressBar setHeight(int height)
  {
    this.height = height;
    return this;
  }
  
  public ProgressBar setDimensions(int width, int height)
  {
    this.width = width;
    this.height = height;
    
    return this;
  }
  
  public ProgressBar setFillDirection(FillDirection fillDirection)
  {
    this.fillDirection = fillDirection;
    return this;
  }
  
  public ProgressBar setBorder(boolean border)
  {
    this.border = border;
    return this;
  }
  
  public ProgressBar setBorderThickness(int borderThickness)
  {
    this.borderThickness = borderThickness;
    return this;
  }
  
  public ProgressBar setBorderColor(int borderColor)
  {
    this.borderColor = borderColor;
    return this;
  }
  
  public ProgressBar setBackground(boolean background)
  {
    this.background = background;
    return this;
  }
  
  public ProgressBar setBackgroundColor(int backgroundColor)
  {
    this.backgroundColor = backgroundColor;
    return this;
  }
  
  public ProgressBar setGradient(boolean gradient)
  {
    this.gradient = gradient;
    return this;
  }
  
  public ProgressBar setFullColor(int fullColor)
  {
    this.fullColor = fullColor;
    return this;
  }
  
  public ProgressBar setEmptyColor(int emptyColor)
  {
    this.emptyColor = emptyColor;
    return this;
  }
  
  public ProgressBar setColor(int color)
  {
    this.color = color;
    return this;
  }
  
  public ProgressBar useReverseProgress(boolean reverse)
  {
    useReverseProgress = reverse;
    
    return this;
  }
  
  public int getX()
  {
    return x;
  }
  
  public int getY()
  {
    return y;
  }
  
  public int getWidth()
  {
    return width;
  }
  
  public int getHeight()
  {
    return height;
  }
  
  public FillDirection getFillDirection()
  {
    return fillDirection;
  }
  
  public boolean isBorder()
  {
    return border;
  }
  
  public int getBorderThickness()
  {
    return borderThickness;
  }
  
  public int getBorderColor()
  {
    return borderColor;
  }
  
  public boolean isBackground()
  {
    return background;
  }
  
  public int getBackgroundColor()
  {
    return backgroundColor;
  }
  
  public boolean isGradient()
  {
    return gradient;
  }
  
  public int getFullColor()
  {
    return fullColor;
  }
  
  public int getEmptyColor()
  {
    return emptyColor;
  }
  
  public int getColor()
  {
    return color;
  }
  
  public boolean isUseReverseProgress()
  {
    return useReverseProgress;
  }
  
  public Supplier<Integer> getCapacitySupplier()
  {
    return capacitySupplier;
  }
  
  public Supplier<Integer> getStorageSupplier()
  {
    return storageSupplier;
  }
  
  public int getCurrCapacity()
  {
    return currCapacity;
  }
  
  public int getCurrStorage()
  {
    return currStorage;
  }
  
  public int getCurrentColor()
  {
    return currentColor;
  }
  
  public float getCurrentProgress()
  {
    return currentProgress;
  }
  
  public <PB extends ProgressBar> PB cast()
  {
    return (PB) this;
  }
  
  public boolean mouseOver(int mouseX, int mouseY)
  {
    return mouseX >= x && mouseX < x + width
      && mouseY >= y && mouseY < y + height;
  }
  
  public enum FillDirection
  {
    LEFT_RIGHT,
    RIGHT_LEFT,
    BOTTOM_TOP,
    TOP_BOTTOM
  }
}

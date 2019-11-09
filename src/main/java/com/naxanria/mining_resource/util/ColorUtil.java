package com.naxanria.mining_resource.util;

import net.minecraft.util.math.MathHelper;

/*
  @author: Naxanria
*/
public class ColorUtil
{
  public static int rgb(int r, int g, int b)
  {
    return 0xff000000 | r << 16 | g << 8 | b;
  }
  
  public static int argb(int a, int r, int g, int b)
  {
    return a << 24 | r << 16 | g << 8 | b;
  }
  
  public static int lerp(float t, int colorA, int colorB)
  {
    int[] aCols = toInts(colorA);
    int[] bCols = toInts(colorB);
  
    return argb
    (
      (int) MathHelper.lerp(t, aCols[0], bCols[0]),
      (int) MathHelper.lerp(t, aCols[1], bCols[1]),
      (int) MathHelper.lerp(t, aCols[2], bCols[2]),
      (int) MathHelper.lerp(t, aCols[3], bCols[3])
    );
  }
  
  public static int[] toInts(int color)
  {
    return new int[]
      {
        (color >> 24) & 0xff,
        (color >> 16) & 0xff,
        (color >> 8) & 0xff,
        color & 0xff
      };
  }
}

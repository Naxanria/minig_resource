package com.naxanria.mining_resource.util;

import java.util.List;

/*
  @author: Naxanria
*/
public class StringUtil
{
  public static String compact(List<String> list)
  {
    return compact(list, "\n");
  }
  
  private static String compact(List<String> list, String combine)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < list.size(); i++)
    {
      builder.append(list.get(i));
      if (list.size() - i > 1)
      {
        builder.append(combine);
      }
    }
    
    return builder.toString();
  }
}

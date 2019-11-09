package com.naxanria.mining_resource.upgrade;

import java.util.HashMap;
import java.util.Map;

public class Upgrade
{
  private Map<String, Modifier> modifiers = new HashMap<>();
  
  public Upgrade(Modifier... modifiers)
  {
    for (Modifier modifier : modifiers)
    {
      this.modifiers.put(modifier.name, modifier);
    }
  }
}

//package com.naxanria.mining_resource.item.upgrade;
//
//import com.naxanria.mining_resource.tile.TileMiner;
//import com.naxanria.mining_resource.upgrade.IUpgradeable;
//import com.naxanria.mining_resource.upgrade.UpgradeType;
//
//public class SpeedUpgrade extends BaseUpgradeItem
//{
//  private int speedValue;
//
//  public SpeedUpgrade(Properties properties, int speedValue)
//  {
//    super(properties);
//    this.speedValue = speedValue;
//  }
//
//  @Override
//  public void update(IUpgradeable upgradeable)
//  {
//
//  }
//
//  @Override
//  public UpgradeType getUpgradeType()
//  {
//    return UpgradeType.SPEED;
//  }
//
//  @Override
//  public int getBonusValue(IUpgradeable upgradeable, String name)
//  {
//    if (UpgradeType.SPEED.name.equals(name))
//    {
//      return speedValue;
//    }
//
//    return 0;
//  }
//}

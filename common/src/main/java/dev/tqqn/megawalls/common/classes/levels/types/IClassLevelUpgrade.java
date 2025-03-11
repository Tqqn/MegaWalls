package dev.tqqn.megawalls.common.classes.levels.types;

import java.util.List;

public interface IClassLevelUpgrade<T> {

    T getUpgrade(UpgradeLevel upgradeLevel);
    List<T> getUpgrades();
}

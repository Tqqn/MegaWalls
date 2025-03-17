package dev.tqqn.megawalls.common.classes.levels.types;

import dev.tqqn.megawalls.common.classes.levels.types.kits.object.KitUpgrade;

import java.util.List;

public interface IClassLevelUpgrade<T, B> {

    T getUpgrade(UpgradeLevel upgradeLevel);
    List<T> getUpgrades();

    void addUpgrade(B builder);
}

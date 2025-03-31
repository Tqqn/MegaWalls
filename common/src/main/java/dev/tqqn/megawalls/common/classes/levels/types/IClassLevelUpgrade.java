package dev.tqqn.megawalls.common.classes.levels.types;

import dev.tqqn.megawalls.common.classes.levels.ClassUpgrades;

import java.util.List;

public interface IClassLevelUpgrade<T, B> {

    T getUpgrade(ClassUpgrades.UpgradeLevel upgradeLevel);
    List<T> getUpgrades();

    void addUpgrade(B builder);
}

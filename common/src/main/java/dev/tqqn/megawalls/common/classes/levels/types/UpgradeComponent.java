package dev.tqqn.megawalls.common.classes.levels.types;

import dev.tqqn.megawalls.common.classes.levels.ClassUpgrades;
import lombok.Getter;

@Getter
public abstract class UpgradeComponent<T, O> {

    private final ClassUpgrades.UpgradeLevel upgradeLevel;

    public UpgradeComponent(ClassUpgrades.UpgradeLevel upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public abstract T getValue();
    public abstract void addValue(O object);
}

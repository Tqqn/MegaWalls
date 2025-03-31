package dev.tqqn.megawalls.common.classes;

import dev.tqqn.megawalls.common.classes.levels.ClassUpgrades;
import lombok.Getter;

@Getter
public abstract class UpgradeBuilder<T, O> {

    private final ClassUpgrades.UpgradeLevel upgradeLevel;

    public UpgradeBuilder(ClassUpgrades.UpgradeLevel upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public abstract UpgradeBuilder<T, O> addValue(O value);

    public abstract T build();

}

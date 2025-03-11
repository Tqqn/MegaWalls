package dev.tqqn.megawalls.common.classes.levels.types;

import lombok.Getter;

@Getter
public abstract class UpgradeComponent<T, O> {

    private final UpgradeLevel upgradeLevel;

    public UpgradeComponent(UpgradeLevel upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public abstract T getValue();
    public abstract void addValue(O object);
}

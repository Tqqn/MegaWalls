package dev.tqqn.megawalls.common.classes;

import dev.tqqn.megawalls.common.classes.levels.types.UpgradeLevel;
import lombok.Getter;

@Getter
public abstract class UpgradeBuilder<T, O> {

    private final UpgradeLevel upgradeLevel;

    public UpgradeBuilder(UpgradeLevel upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public abstract UpgradeBuilder<T, O> addValue(O value);

    public abstract T build();

}

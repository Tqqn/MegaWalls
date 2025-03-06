package dev.tqqn.megawalls.common.classes.levels;

import java.util.EnumMap;
import java.util.Map;

public abstract class UpgradeAbleComponent<T> {

    private final Map<LevelUpgrades, UpgradeValue<T>> upgrades = new EnumMap<>(LevelUpgrades.class);

    public UpgradeValue<T> getUpgrade(LevelUpgrades upgrade) {
        return upgrades.get(upgrade);
    }

    public void addUpgrade(LevelUpgrades upgrade, UpgradeValue<T> value) {
        upgrades.put(upgrade, value);
    }

    public enum LevelUpgrades {
        I,
        II,
        III,
        IV,
        V;
    }
}

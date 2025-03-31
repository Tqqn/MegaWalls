package dev.tqqn.megawalls.common.classes.levels;

import dev.tqqn.megawalls.common.classes.ClassDescriptions;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;

@Getter
@Setter
public final class ClassUpgrades {

    private final ClassDescriptions.ClassType type;

    private final Map<UpgradeType, UpgradeLevel> upgrades = new EnumMap<>(UpgradeType.class);

    private Prestige prestige = Prestige.NONE;

    public ClassUpgrades(ClassDescriptions.ClassType type) {
        this.type = type;

        for (UpgradeType upgradeType : UpgradeType.values()) {
            if (!upgrades.containsKey(upgradeType)) upgrades.put(upgradeType, UpgradeLevel.I);
        }
    }

    public void decreaseUpgrade(UpgradeType upgradeType) {
        final UpgradeLevel oldValue = upgrades.get(upgradeType);
        final UpgradeLevel newValue = oldValue.previous();
        if (oldValue == newValue) return;
        upgrades.put(upgradeType, newValue);
    }

    public void increaseUpgrade(UpgradeType upgradeType) {
        final UpgradeLevel oldValue = upgrades.get(upgradeType);
        final UpgradeLevel newValue = oldValue.next();
        if (oldValue == newValue) return;
        upgrades.put(upgradeType, newValue);
    }

    public boolean isPrestige() {
        return prestige != Prestige.NONE;
    }

    public enum UpgradeType {
        MAIN_ABILITY,
        PASSIVE_ONE,
        PASSIVE_TWO,
        GATHERING,
        KIT
    }

    public enum Prestige {
        NONE,
        ONE,
        TWO,
        THREE,
        FOUR
    }

    @Getter
    public enum UpgradeLevel {
        I(1),
        II(2),
        III(3),
        IV(4),
        V(5);

        private final int level;

        UpgradeLevel(int level) {
            this.level = level;
        }

        public UpgradeLevel next() {
            return ordinal() == values().length - 1 ? this : values()[ordinal() + 1];

        }

        public UpgradeLevel previous() {
            return ordinal() == 0 ? this : values()[ordinal() - 1];
        }
    }

}

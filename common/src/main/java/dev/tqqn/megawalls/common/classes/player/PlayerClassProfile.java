package dev.tqqn.megawalls.common.classes.player;

import dev.tqqn.megawalls.common.classes.ClassDescriptions;
import dev.tqqn.megawalls.common.classes.levels.ClassUpgrades;

import java.util.EnumMap;
import java.util.Map;

public final class PlayerClassProfile {

    private final Map<ClassDescriptions.ClassType, ClassUpgrades> upgrades = new EnumMap<>(ClassDescriptions.ClassType.class);

    public void addUpgrade(ClassDescriptions.ClassType classType) {
        upgrades.put(classType, new ClassUpgrades(classType));
    }

    public ClassUpgrades getUpgrade(ClassDescriptions.ClassType classType) {
        return upgrades.get(classType);
    }

}

package dev.tqqn.megawalls.common.classes.levels.types;

import lombok.Getter;

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
}

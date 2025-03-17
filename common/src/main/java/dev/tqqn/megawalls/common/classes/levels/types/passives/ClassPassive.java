package dev.tqqn.megawalls.common.classes.levels.types.passives;

import com.google.common.collect.ImmutableList;
import dev.tqqn.megawalls.common.classes.levels.types.IClassLevelUpgrade;
import dev.tqqn.megawalls.common.classes.levels.types.UpgradeLevel;
import dev.tqqn.megawalls.common.classes.levels.types.passives.object.PassiveUpgrade;

import java.util.EnumMap;
import java.util.List;

public final class ClassPassive implements IClassLevelUpgrade<PassiveUpgrade, PassiveUpgrade.PassiveUpgradeBuilder> {

    private final EnumMap<UpgradeLevel, PassiveUpgrade> passiveLevels = new EnumMap<>(UpgradeLevel.class);

    @Override
    public PassiveUpgrade getUpgrade(UpgradeLevel upgradeLevel) {
        return passiveLevels.get(upgradeLevel);
    }

    @Override
    public List<PassiveUpgrade> getUpgrades() {
        return ImmutableList.copyOf(passiveLevels.values());
    }

    @Override
    public void addUpgrade(PassiveUpgrade.PassiveUpgradeBuilder passiveUpgradeBuilder) {
        passiveLevels.put(passiveUpgradeBuilder.getUpgradeLevel(), passiveUpgradeBuilder.build());
    }
}

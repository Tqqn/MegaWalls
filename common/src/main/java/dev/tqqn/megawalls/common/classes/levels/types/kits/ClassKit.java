package dev.tqqn.megawalls.common.classes.levels.types.kits;

import com.google.common.collect.ImmutableList;
import dev.tqqn.megawalls.common.classes.levels.types.IClassLevelUpgrade;
import dev.tqqn.megawalls.common.classes.levels.types.UpgradeLevel;
import dev.tqqn.megawalls.common.classes.levels.types.kits.object.KitUpgrade;

import java.util.EnumMap;
import java.util.List;

public final class ClassKit implements IClassLevelUpgrade<KitUpgrade, KitUpgrade.KitUpgradeBuilder> {

    private final EnumMap<UpgradeLevel, KitUpgrade> kitLevels = new EnumMap<>(UpgradeLevel.class);

    @Override
    public KitUpgrade getUpgrade(UpgradeLevel upgradeLevel) {
        return kitLevels.get(upgradeLevel);
    }

    @Override
    public List<KitUpgrade> getUpgrades() {
        return ImmutableList.copyOf(kitLevels.values());
    }

    @Override
    public void addUpgrade(KitUpgrade.KitUpgradeBuilder kitUpgradeBuilder) {
        kitLevels.put(kitUpgradeBuilder.getUpgradeLevel(), kitUpgradeBuilder.build());
    }
}

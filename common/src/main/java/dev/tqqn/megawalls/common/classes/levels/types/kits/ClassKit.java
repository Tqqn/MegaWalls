package dev.tqqn.megawalls.common.classes.levels.types.kits;

import com.google.common.collect.ImmutableList;
import dev.tqqn.megawalls.common.classes.levels.types.IClassLevelUpgrade;
import dev.tqqn.megawalls.common.classes.levels.types.UpgradeLevel;
import dev.tqqn.megawalls.common.classes.levels.types.kits.object.KitUpgrade;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.List;

public final class ClassKit implements IClassLevelUpgrade<KitUpgrade> {

    private final EnumMap<UpgradeLevel, KitUpgrade> kitLevels = new EnumMap<>(UpgradeLevel.class);

    public ClassKit() {
        kitLevels.put(UpgradeLevel.I, KitUpgrade.getBuilder(UpgradeLevel.I).addValue(new ItemStack(Material.IRON_SWORD)).build());
    }

    @Override
    public KitUpgrade getUpgrade(UpgradeLevel upgradeLevel) {
        return kitLevels.get(upgradeLevel);
    }

    @Override
    public List<KitUpgrade> getUpgrades() {
        return ImmutableList.copyOf(kitLevels.values());
    }
}

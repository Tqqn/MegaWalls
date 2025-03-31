package dev.tqqn.megawalls.common.classes.levels.types.kits.object;

import dev.tqqn.megawalls.common.classes.UpgradeBuilder;
import dev.tqqn.megawalls.common.classes.levels.ClassUpgrades;
import dev.tqqn.megawalls.common.classes.levels.types.UpgradeComponent;
import dev.tqqn.megawalls.common.utils.ItemBuilder;
import org.bukkit.inventory.EquipmentSlot;

public final class KitUpgrade extends UpgradeComponent<KitValue, ItemBuilder> {

    private final KitValue kitValue;

    private KitUpgrade(ClassUpgrades.UpgradeLevel upgradeLevel) {
        super(upgradeLevel);
        this.kitValue = new KitValue();
    }

    @Override
    public KitValue getValue() {
        return kitValue;
    }

    @Override
    public void addValue(ItemBuilder itemBuilder) {
        kitValue.addValue(itemBuilder.build());
    }

    public void addValue(ItemBuilder itemBuilder, int place) {
        kitValue.addValue(itemBuilder.build(), place);
    }

    public void addValue(ItemBuilder itemBuilder, EquipmentSlot equipmentSlot) {
        kitValue.addValue(itemBuilder.build(), equipmentSlot);
    }

    public static KitUpgradeBuilder getBuilder(ClassUpgrades.UpgradeLevel upgradeLevel) {
        return new KitUpgradeBuilder(upgradeLevel);
    }

    public static class KitUpgradeBuilder extends UpgradeBuilder<KitUpgrade, ItemBuilder> {

        private final KitUpgrade kitUpgrade;

        private KitUpgradeBuilder(ClassUpgrades.UpgradeLevel upgradeLevel) {
            super(upgradeLevel);
            this.kitUpgrade = new KitUpgrade(upgradeLevel);
        }


        @Override
        public KitUpgradeBuilder addValue(ItemBuilder itemBuilder) {
            kitUpgrade.addValue(itemBuilder);
            return this;
        }

        public KitUpgradeBuilder addValue(ItemBuilder itemBuilder, int place) {
            kitUpgrade.addValue(itemBuilder, place);
            return this;
        }

        public KitUpgradeBuilder addValue(ItemBuilder itemBuilder, EquipmentSlot equipmentSlot) {
            kitUpgrade.addValue(itemBuilder, equipmentSlot);
            return this;
        }

        @Override
        public KitUpgrade build() {
            return kitUpgrade;
        }
    }
}

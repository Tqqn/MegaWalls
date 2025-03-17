package dev.tqqn.megawalls.common.classes.levels.types.kits.object;

import com.google.common.collect.ImmutableList;
import dev.tqqn.megawalls.common.classes.levels.types.UpgradeComponent;
import dev.tqqn.megawalls.common.classes.levels.types.UpgradeLevel;
import dev.tqqn.megawalls.common.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class KitUpgrade extends UpgradeComponent<List<ItemStack>, ItemBuilder> {

    private final List<ItemStack> kitItems = new ArrayList<>();

    private KitUpgrade(UpgradeLevel upgradeLevel) {
        super(upgradeLevel);
    }

    @Override
    public List<ItemStack> getValue() {
        return ImmutableList.copyOf(kitItems);
    }

    @Override
    public void addValue(ItemBuilder itemBuilder) {
        kitItems.add(itemBuilder.build());
    }

    public static KitUpgradeBuilder getBuilder(UpgradeLevel upgradeLevel) {
        return new KitUpgradeBuilder(upgradeLevel);
    }

    public static class KitUpgradeBuilder {

        private final KitUpgrade kitUpgrade;
        @Getter private final UpgradeLevel upgradeLevel;

        private KitUpgradeBuilder(UpgradeLevel upgradeLevel) {
            this.kitUpgrade = new KitUpgrade(upgradeLevel);
            this.upgradeLevel = upgradeLevel;
        }

        public KitUpgradeBuilder addValue(ItemBuilder itemBuilder) {
            kitUpgrade.addValue(itemBuilder);
            return this;
        }

        public KitUpgrade build() {
            return kitUpgrade;
        }
    }
}

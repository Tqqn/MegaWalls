package dev.tqqn.megawalls.common.classes.levels.types.passives.object;

import dev.tqqn.megawalls.common.classes.UpgradeBuilder;
import dev.tqqn.megawalls.common.classes.levels.types.UpgradeComponent;
import dev.tqqn.megawalls.common.classes.levels.types.UpgradeLevel;

public class PassiveUpgrade extends UpgradeComponent<Integer, Integer> {

    private int value;

    private PassiveUpgrade(UpgradeLevel upgradeLevel) {
        super(upgradeLevel);
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void addValue(Integer value) {
        this.value = value;
    }

    public static PassiveUpgradeBuilder getBuilder(UpgradeLevel upgradeLevel) {
        return new PassiveUpgradeBuilder(upgradeLevel);
    }

    public static class PassiveUpgradeBuilder extends UpgradeBuilder<PassiveUpgrade, Integer> {

        private final PassiveUpgrade passiveUpgrade;

        private PassiveUpgradeBuilder(UpgradeLevel upgradeLevel) {
            super(upgradeLevel);
            this.passiveUpgrade = new PassiveUpgrade(upgradeLevel);
        }

        @Override
        public PassiveUpgradeBuilder addValue(Integer integer) {
            passiveUpgrade.addValue(integer);
            return this;
        }

        @Override
        public PassiveUpgrade build() {
            return passiveUpgrade;
        }
    }
}

package dev.tqqn.megawalls.common.classes.levels.types;

import dev.tqqn.megawalls.common.classes.levels.UpgradeAbleComponent;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public final class ClassUpgradeValues {

    private final ClassKit classKit = new ClassKit();
    private final MainAbility mainAbility = new MainAbility();
    private final GatheringAbility gatheringAbility = new GatheringAbility();
    private final PassiveAbility passiveAbilityOne = new PassiveAbility();
    private final PassiveAbility passiveAbilityTwo = new PassiveAbility();

    public final class ClassKit extends UpgradeAbleComponent<ItemStack> {}
    public final class MainAbility extends UpgradeAbleComponent<Integer> {}
    public final class PassiveAbility extends UpgradeAbleComponent<Integer> {}
    public final class GatheringAbility extends UpgradeAbleComponent<Integer> {}

}

package dev.tqqn.kireiwalls.modules.classes;

import dev.tqqn.kireiwalls.framework.game.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.game.classes.ClassDescriptions;
import dev.tqqn.kireiwalls.framework.game.classes.ClassOptions;

import java.util.Arrays;

/**
 * The Zombie class extends AbstractClass and represents the Zombie class in the plugin.
 * It defines the characteristics and abilities specific to the Zombie class.
 */
public final class Zombie extends AbstractClass {

    public Zombie() {
        super("Zombie", "ZOM", new ClassOptions(ClassDescriptions.ClassEnergy.ZOMBIE, ClassDescriptions.ClassType.ZOMBIE, ClassDescriptions.ClassDifficulty.ZOMBIE, Arrays.asList(ClassDescriptions.ClassStyle.TANK, ClassDescriptions.ClassStyle.SUPPORT), ClassDescriptions.ClassDiamond.ZOMBIE, ClassDescriptions.ClassSkillDescription.ZOMBIE), 5);
    }

    public void onMainAbility() {
    }

    public void onAbilityOne() {
    }

    public void onAbilityTwo() {
    }

    public void onGatheringAbility() {
    }
}

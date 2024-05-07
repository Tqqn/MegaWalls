package dev.tqqn.kireiwalls.modules.classes;

import dev.tqqn.kireiwalls.framework.game.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.game.classes.ClassDescriptions;
import dev.tqqn.kireiwalls.framework.game.classes.ClassOptions;

import java.util.Arrays;

/**
 * The Skeleton class extends AbstractClass and represents the Skeleton class in the plugin.
 * It defines the characteristics and abilities specific to the Skeleton class.
 */
public final class Skeleton extends AbstractClass {

    public Skeleton() {
        super("Skeleton", "SKE", new ClassOptions(ClassDescriptions.ClassEnergy.SKELETON, ClassDescriptions.ClassType.SKELETON, ClassDescriptions.ClassDifficulty.SKELETON, Arrays.asList(ClassDescriptions.ClassStyle.RANGED, ClassDescriptions.ClassStyle.CONTROL), ClassDescriptions.ClassDiamond.SKELETON, ClassDescriptions.ClassSkillDescription.SKELETON), 4);
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

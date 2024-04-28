package dev.tqqn.kireiwalls.modules.classes;

import dev.tqqn.kireiwalls.framework.game.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.game.classes.ClassDescriptions;
import dev.tqqn.kireiwalls.framework.game.classes.ClassOptions;

import java.util.Arrays;

public class Skeleton extends AbstractClass {
    public Skeleton() {
        super("Skeleton", "SKE", new ClassOptions(ClassDescriptions.ClassType.SKELETON, ClassDescriptions.ClassDifficulty.SKELETON, Arrays.asList(ClassDescriptions.ClassStyle.RANGED, ClassDescriptions.ClassStyle.CONTROL), ClassDescriptions.ClassDiamond.SKELETON, ClassDescriptions.ClassSkillDescription.SKELETON), 4);
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

package dev.tqqn.kireiwalls.modules.classes;

import dev.tqqn.kireiwalls.framework.game.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.game.classes.ClassDescriptions;
import dev.tqqn.kireiwalls.framework.game.classes.ClassOptions;

import java.util.Arrays;

public class Zombie extends AbstractClass {
    public Zombie() {
        super("Zombie", "ZOM", new ClassOptions(ClassDescriptions.ClassType.ZOMBIE, ClassDescriptions.ClassDifficulty.ZOMBIE, Arrays.asList(ClassDescriptions.ClassStyle.TANK, ClassDescriptions.ClassStyle.SUPPORT), ClassDescriptions.ClassDiamond.ZOMBIE, ClassDescriptions.ClassSkillDescription.ZOMBIE), 5);
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

package dev.tqqn.kireiwalls.modules.classes;

import dev.tqqn.kireiwalls.framework.game.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.game.classes.ClassDescriptions;
import dev.tqqn.kireiwalls.framework.game.classes.ClassOptions;

import java.util.Arrays;

public class Herobrine extends AbstractClass {

    public Herobrine() {
        super("Herobrine", "HBR", new ClassOptions(ClassDescriptions.ClassType.HEROBRINE, ClassDescriptions.ClassDifficulty.HEROBRINE, Arrays.asList(ClassDescriptions.ClassStyle.DAMAGE, ClassDescriptions.ClassStyle.CONTROL), ClassDescriptions.ClassDiamond.HEROBRINE, ClassDescriptions.ClassSkillDescription.HEROBRINE), 3);
        setPrestigeFour(true);
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
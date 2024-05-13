package dev.tqqn.kireiwalls.modules.classes;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.classes.ClassDescriptions;
import dev.tqqn.kireiwalls.framework.classes.ClassOptions;

import java.util.Arrays;

/**
 * The Herobrine class extends AbstractClass and represents the Herobrine class in the plugin.
 * It defines the characteristics and abilities specific to the Herobrine class.
 */
public final class Herobrine extends AbstractClass {

    public Herobrine() {
        super("Herobrine", "HBR", new ClassOptions(ClassDescriptions.ClassEnergy.HEROBRINE, ClassDescriptions.ClassType.HEROBRINE, ClassDescriptions.ClassDifficulty.HEROBRINE, Arrays.asList(ClassDescriptions.ClassStyle.DAMAGE, ClassDescriptions.ClassStyle.CONTROL), ClassDescriptions.ClassDiamond.HEROBRINE, ClassDescriptions.ClassSkillDescription.HEROBRINE), 3);
        setPrestigeFour(true);
    }

    @Override
    public void initKitItems() {

    }

    @Override
    public void executeAbility(PlayerModel playerModel) {

    }

    @Override
    public String getActionBar(PlayerModel playerModel) {
        return " ";
    }
}
package dev.tqqn.kireiwalls.modules.classes;

import dev.tqqn.kireiwalls.modules.classes.framework.Skins;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.classes.framework.AbstractClass;
import dev.tqqn.kireiwalls.modules.classes.framework.ClassDescriptions;
import dev.tqqn.kireiwalls.modules.classes.framework.ClassOptions;

import java.util.Arrays;

/**
 * The Herobrine class extends AbstractClass and represents the Herobrine class in the plugin.
 * It defines the characteristics and abilities specific to the Herobrine class.
 */
public final class Herobrine extends AbstractClass {

    public Herobrine() {
        super("Herobrine", "HBR", new ClassOptions(ClassDescriptions.ClassEnergy.HEROBRINE, ClassDescriptions.ClassType.HEROBRINE, ClassDescriptions.ClassDifficulty.HEROBRINE, Arrays.asList(ClassDescriptions.ClassStyle.DAMAGE, ClassDescriptions.ClassStyle.CONTROL), ClassDescriptions.ClassDiamond.HEROBRINE, ClassDescriptions.ClassSkillDescription.HEROBRINE), 3, Skins.HEROBRINE);
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
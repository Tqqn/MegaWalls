package dev.tqqn.megawalls.modules.classes;

import dev.tqqn.megawalls.common.classes.ClassSkins;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.classes.framework.objects.AbstractClass;
import dev.tqqn.megawalls.common.classes.ClassDescriptions;
import dev.tqqn.megawalls.common.classes.ClassOptions;

import java.util.Arrays;

/**
 * The Herobrine class extends AbstractClass and represents the Herobrine class in the plugin.
 * It defines the characteristics and abilities specific to the Herobrine class.
 */
public final class Herobrine extends AbstractClass {

    public Herobrine() {
        super("Herobrine", "HBR", new ClassOptions(ClassDescriptions.ClassEnergy.HEROBRINE, ClassDescriptions.ClassType.HEROBRINE, ClassDescriptions.ClassDifficulty.HEROBRINE, Arrays.asList(ClassDescriptions.ClassStyle.DAMAGE, ClassDescriptions.ClassStyle.CONTROL), ClassDescriptions.ClassDiamond.HEROBRINE, ClassDescriptions.ClassSkillDescription.HEROBRINE), 3, ClassSkins.HEROBRINE);
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
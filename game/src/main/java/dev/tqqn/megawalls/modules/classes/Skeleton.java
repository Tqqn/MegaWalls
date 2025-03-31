package dev.tqqn.megawalls.modules.classes;

import dev.tqqn.megawalls.common.classes.ClassSkins;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.classes.framework.objects.AbstractClass;
import dev.tqqn.megawalls.common.classes.ClassDescriptions;
import dev.tqqn.megawalls.common.classes.ClassOptions;

import java.util.Arrays;

/**
 * The Skeleton class extends AbstractClass and represents the Skeleton class in the plugin.
 * It defines the characteristics and abilities specific to the Skeleton class.
 */
public final class Skeleton extends AbstractClass {

    public Skeleton() {
        super(ClassDescriptions.ClassType.SKELETON, "SKE", new ClassOptions(ClassDescriptions.ClassEnergy.SKELETON, ClassDescriptions.ClassType.SKELETON, ClassDescriptions.ClassDifficulty.SKELETON, Arrays.asList(ClassDescriptions.ClassStyle.RANGED, ClassDescriptions.ClassStyle.CONTROL), ClassDescriptions.ClassDiamond.SKELETON, ClassDescriptions.ClassSkillDescription.SKELETON), 4, ClassSkins.SKELETON, getClassModule().getClassUpgradeValueFromDB("SKELETON"));
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

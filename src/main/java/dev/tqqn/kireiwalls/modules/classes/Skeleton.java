package dev.tqqn.kireiwalls.modules.classes;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.classes.ClassDescriptions;
import dev.tqqn.kireiwalls.framework.classes.ClassOptions;

import java.util.Arrays;

/**
 * The Skeleton class extends AbstractClass and represents the Skeleton class in the plugin.
 * It defines the characteristics and abilities specific to the Skeleton class.
 */
public final class Skeleton extends AbstractClass {

    public Skeleton() {
        super("Skeleton", "SKE", new ClassOptions(ClassDescriptions.ClassEnergy.SKELETON, ClassDescriptions.ClassType.SKELETON, ClassDescriptions.ClassDifficulty.SKELETON, Arrays.asList(ClassDescriptions.ClassStyle.RANGED, ClassDescriptions.ClassStyle.CONTROL), ClassDescriptions.ClassDiamond.SKELETON, ClassDescriptions.ClassSkillDescription.SKELETON), 4);
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

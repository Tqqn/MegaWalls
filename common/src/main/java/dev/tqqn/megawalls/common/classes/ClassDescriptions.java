package dev.tqqn.megawalls.common.classes;

import lombok.Getter;
import org.bukkit.Material;

/**
 * The ClassDescriptions class contains enums representing various aspects of class descriptions,
 * including energy consumption, class types, difficulties, styles, diamond equipment, and skill descriptions.
 * Each enum provides information related to a specific aspect of a player class.
 */
public final class ClassDescriptions {

    /**
     * Enum representing energy consumption details for different classes.
     */
    @Getter
    public enum ClassEnergy {
        HEROBRINE(100, 100, 20, 0, 20, 0),
        SKELETON(100, 100, 0, 1, 20, 0),
        ZOMBIE(100, 100, 12, 0, 10, 1);

        private final int neededEnergyForAbility;
        private final int maxEnergy;
        private final int energyPerHit;
        private final int energyPerSecond;
        private final int energyPerBowShot;
        private final int energyPerGettingHit;

        ClassEnergy(int neededEnergyForAbility, int maxEnergy, int energyPerHit, int energyPerSecond, int energyPerBowShow, int energyPerGettingHit) {
            this.neededEnergyForAbility = neededEnergyForAbility;
            this.maxEnergy = maxEnergy;
            this.energyPerHit = energyPerHit;
            this.energyPerSecond = energyPerSecond;
            this.energyPerBowShot = energyPerBowShow;
            this.energyPerGettingHit = energyPerGettingHit;
        }
    }

    /**
     * Enum representing class types with their corresponding icons and types.
     */
    @Getter
    public enum ClassType {
        HEROBRINE("Herobrine", Material.DIAMOND_SWORD, "<dark_gray>Regular Class", "<yellow>"),
        SKELETON("Skeleton", Material.BOW, "<dark_gray>Regular Class", "<aqua>"),
        ZOMBIE("Zombie", Material.ROTTEN_FLESH, "<dark_gray>Regular Class", "<dark_green>");

        private final String name;
        private final Material icon;
        private final String type;
        private final String color;

        ClassType(String name, Material icon, String type, String color) {
            this.name = name;
            this.icon = icon;
            this.type = type;
            this.color = color;
        }
    }

    /**
     * Enum representing class difficulties.
     */
    @Getter
    public enum ClassDifficulty {
        HEROBRINE("<green>●<gray>●●●"),
        SKELETON("<red>●●●<gray>●"),
        ZOMBIE("<green>●<gray>●●●"),
        CREEPER("<dark_red>●●●●");

        private final String difficulty;
        ClassDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }
    }

    /**
     * Enum representing class styles.
     */
    @Getter
    public enum ClassStyle {
        DAMAGE("<red>Damage<gray>: Dealing a lot of damage."),
        CONTROL("<gold>Control<gray>: Fighting groups of enemies."),
        RANGED("<dark_aqua>Ranged<gray>: Dealing damage from distance."),
        TANK("<blue>Tank<gray>: Taking less damage."),
        SUPPORT("<light_purple>Support<gray>: Healing teammates.");

        private final String style;
        ClassStyle(String style) {
            this.style = style;
        }

    }

    /**
     * Enum representing diamond equipment for different classes.
     */
    @Getter
    public enum ClassDiamond {
        HEROBRINE("<aqua>Sword"),
        SKELETON("<aqua>Helmet"),
        ZOMBIE("<aqua>Chestplate");

        private final String diamond;

        ClassDiamond(String diamond) {
            this.diamond = diamond;
        }
    }

    /**
     * Enum representing skill descriptions for different classes.
     */
    @Getter
    public enum ClassSkillDescription {
        HEROBRINE("<gray>Wrath", new String[]{"<gray>･ Unleash the wrath of Herobrine striking all nearby", "<gray>enemies in a 5 block radius for <green>%s <gray>damage.", "<gray>･ How to activate: Left Click with your Bow Right Click", "<gray>with your Sword.", "<gray>･ Energy Per Hit (Melee & Bow): 25"}),
        SKELETON("<gray>Explosive Arrow", new String[]{"<gray>･ You will fire an explosive arrow that deals <green>%s<gray> damage in", "<gray> in a 6 block radius and breaks blocks", "<gray>･ Uncharged bow shots give 50% energy, and Medium", "<gray>charged bow shots give 75% Energy.", "<gray>･ How to activate: Left Click with your Bow", "<gray>･ Energy Per Hit (Melee): 0", "<gray>･ Energy Per Hit (Bow): 20", "<gray>･ Energy Per Second (Deathmatch): 1"}),
        ZOMBIE("<gray>Circle of Healing", new String[]{"<gray>･ Heal yourself for <green>%s HP <gray>and nearby teammates in a 5", " <gray>block radius for <green>%s HP<gray>.", "<gray>･ How to Activate: Left Click with your Bow or Right Click", "<gray>with your Sword", "<gray>･ Energy Per Hit (Melee & Bow): 12", "<gray>･ Energy When Hit (Melee): 1", "<gray>･ Energy When Hit (Bow): 2"}),;

        private final String name;
        private final String[] description;


        ClassSkillDescription(String name, String[] description) {
            this.name = name;
            this.description = description;
        }
    }
}

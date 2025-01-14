package dev.tqqn.megawalls.modules.classes.framework;

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
        HEROBRINE(Material.DIAMOND_SWORD, "&8Regular Class", "&e"),
        SKELETON(Material.BOW, "&8Regular Class", "&b"),
        ZOMBIE(Material.ROTTEN_FLESH, "&8Regular Class", "&2");

        private final Material icon;
        private final String type;
        private final String color;

        ClassType(Material icon, String type, String color) {
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
        HEROBRINE("&a●&7●●●"),
        SKELETON("&c●●●&7●"),
        ZOMBIE("&a●&7●●●"),
        CREEPER("&4●●●●");

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
        DAMAGE("&cDamage&7: Dealing a lot of damage."),
        CONTROL("&6Control&7: Fighting groups of enemies."),
        RANGED("&3Ranged&7: Dealing damage from distance."),
        TANK("&9Tank&7: Taking less damage."),
        SUPPORT("&dSupport&7: Healing teammates.");

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
        HEROBRINE("&bSword"),
        SKELETON("&bHelmet"),
        ZOMBIE("&bChestplate");

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
        HEROBRINE("&7Wrath", new String[]{"&7･ Unleash the wrath of Herobrine striking all nearby", "&7enemies in a 5 block radius for &a5 &7damage.", "&7･ How to activate: Left Click with your Bow Right Click", "&7with your Sword.", "&7･ Energy Per Hit (Melee & Bow): 25"}),
        SKELETON("&7Explosive Arrow", new String[]{"&7･ You will fire an explosive arrow that deals &a6&7 damage in", "&7 in a 6 block radius and breaks blocks", "&7･ Uncharged bow shots give 50% energy, and Medium", "&7charged bow shots give 75% Energy.", "&7･ How to activate: Left Click with your Bow", "&7･ Energy Per Hit (Melee): 0", "&7･ Energy Per Hit (Bow): 20", "&7･ Energy Per Second (Deathmatch): 1"}),
        ZOMBIE("&7Circle of Healing", new String[]{"&7･ Heal yourself for &a8 HP &7and nearby teammates in a 5", " &7block radius for &a5 HP&7.", "&7･ How to Activate: Left Click with your Bow or Right Click", "&7 with your Sword", "&7･ Energy Per Hit (Melee & Bow): 12", "&7･ Energy When Hit (Melee): 1", "&7･ Energy When Hit (Bow): 2"}),;

        private final String name;
        private final String[] description;


        ClassSkillDescription(String name, String[] description) {
            this.name = name;
            this.description = description;
        }
    }
}

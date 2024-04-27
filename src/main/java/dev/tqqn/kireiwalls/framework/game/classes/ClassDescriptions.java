package dev.tqqn.kireiwalls.framework.game.classes;

import lombok.Getter;
import org.bukkit.Material;

public class ClassDescriptions {

    @Getter
    public enum ClassType {
        HEROBRINE(Material.DIAMOND_SWORD, "&8Regular Class"),
        SKELETON(Material.BOW, "&8Regular Class"),
        ZOMBIE(Material.ROTTEN_FLESH, "&8Regular Class");

        private final Material icon;
        private final String type;

        ClassType(Material icon, String type) {
            this.icon = icon;
            this.type = type;
        }
    }

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

    @Getter
    public enum ClassSkillDescription {
        HEROBRINE("&7Wrath", new String[]{"&7･ Unleash the wrath of Herobrine striking all nearby", "&7enemies in a 5 block radius for &a5 &7damage.", "&7･ How to activate: Left Click with your Bow Right Click", "&7with your Sword.", "&7･ Energy Per Hit (Melee & Bow): 25"}),
        SKELETON("&7Explosive Arrow", new String[]{}),
        ZOMBIE("&7Circle of Healing", new String[]{});

        private final String name;
        private final String[] description;


        ClassSkillDescription(String name, String[] description) {
            this.name = name;
            this.description = description;
        }
    }
}

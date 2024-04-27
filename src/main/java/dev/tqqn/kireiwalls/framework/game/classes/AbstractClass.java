package dev.tqqn.kireiwalls.framework.game.classes;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class AbstractClass implements Listener {

    private final String name;
    private final String tag;
    private final ClassOptions classOptions;
    private final int inventorySlot;
    @Setter private boolean isPrestigeOne;
    @Setter private boolean isPrestigeTwo;
    @Setter private boolean isPrestigeThree;
    @Setter private boolean isPrestigeFour;

    public AbstractClass(String name, String tag, ClassOptions classOptions, int inventorySlot) {
        this.name = name;
        this.tag = "[" + tag + "]";
        this.classOptions = classOptions;
        this.inventorySlot = inventorySlot;
        this.isPrestigeOne = false;
        this.isPrestigeTwo = false;
        this.isPrestigeThree = false;
        this.isPrestigeFour = false;
    }

    public abstract void onMainAbility();
    public abstract void onAbilityOne();
    public abstract void onAbilityTwo();
    public abstract void onGatheringAbility();

    public ItemStack getKitIcon(PlayerModel playerModel) {
        List<String> lore = new ArrayList<>();

        lore.add(classOptions.getClassType().getType());
        lore.add("&7Play Styles:");
        for (ClassDescriptions.ClassStyle classStyle : classOptions.getClassStyles()) {
            lore.add("&7- " + classStyle.getStyle());
        }
        lore.add("&7Difficulty: " + classOptions.getClassDifficulty().getDifficulty());
        lore.add("&7Diamond: " + classOptions.getClassDiamond().getDiamond());
        lore.add(" ");
        lore.add("&eSkill &7- " + classOptions.getClassSkillDescription().getName());
        lore.addAll(Arrays.asList(classOptions.getClassSkillDescription().getDescription()));
        lore.add(" ");
        lore.add("&7Cooldown: &a&l1s");
        lore.add(" ");
        lore.add("&7Upgrades: &a&l100%");
        lore.add("&7Ender Chest: &a&l5 rows");
        lore.add(" ");
        if (playerModel.getCurrentClass() != null && playerModel.getCurrentClass().getClass() == this.getClass()) {
            lore.add("&a&lSelected!");
        } else {
            lore.add("&eClick to select!");
        }


        return new ItemBuilder(classOptions.getClassType().getIcon(), 1).setDisplayName("§a" + name).setLore(lore).hideAttributes().build();
    }

}

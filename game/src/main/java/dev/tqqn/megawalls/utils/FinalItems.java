package dev.tqqn.megawalls.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum FinalItems {

    CLASS_SELECTOR(Material.IRON_SWORD, "&aClass Selector &7(Right Click)", new String[]{""});

    private final Material item;
    private final String name;
    private final String[] lore;

    FinalItems(Material item, String name, String[] lore) {
        this.item = item;
        this.name = name;
        this.lore = lore;
    }

    public ItemStack getItem() {
        return ItemBuilder.getBuilder(item).setDisplayName(name).setLore(lore).build();
    }
}

package dev.tqqn.kireiwalls.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setDisplayName(String name) {
        this.itemMeta.setDisplayName(ChatUtil.translateLegacy(name));
        return this;
    }

    public ItemBuilder setLore(List<String> lines) {
        List<String> tempList = new ArrayList<>();
        for (String line : lines) {
            tempList.add(ChatUtil.translateLegacy(line));
        }
        itemMeta.setLore(tempList);
        return this;
    }

    public ItemBuilder setLore(String... lines) {
        setLore(Arrays.asList(lines));
        return this;
    }

    public ItemBuilder setGlow() {
        itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder hideAttributes() {
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemBuilder getBuilder(Material material) {
        return new ItemBuilder(new ItemStack(material, 1));
    }
}

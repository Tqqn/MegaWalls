package dev.tqqn.kireiwalls.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builder class for creating ItemStacks with custom attributes.
 */
public final class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    /**
     * Constructs an ItemBuilder with the given ItemStack.
     *
     * @param itemStack The ItemStack to be built upon.
     */
    private ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * Sets the display name of the ItemStack.
     *
     * @param name The display name to set.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setDisplayName(String name) {
        this.itemMeta.setDisplayName(ChatUtil.translateLegacy(name));
        return this;
    }

    /**
     * Sets the lore of the ItemStack.
     *
     * @param lines The lines of lore to set.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setLore(List<String> lines) {
        List<String> tempList = new ArrayList<>();
        for (String line : lines) {
            tempList.add(ChatUtil.translateLegacy(line));
        }
        itemMeta.setLore(tempList);
        return this;
    }

    /**
     * Sets the lore of the ItemStack from an array of strings.
     *
     * @param lines The lines of lore to set.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setLore(String... lines) {
        setLore(Arrays.asList(lines));
        return this;
    }

    /**
     * Adds the glow effect to the ItemStack.
     *
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setGlow() {
        itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Hides the attributes of the ItemStack.
     *
     * @return The ItemBuilder instance.
     */
    public ItemBuilder hideAttributes() {
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    /**
     * Builds the ItemStack with the specified attributes.
     *
     * @return The constructed ItemStack.
     */
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Returns a new ItemBuilder instance for the given material.
     *
     * @param material The material of the ItemStack.
     * @return A new ItemBuilder instance.
     */
    public static ItemBuilder getBuilder(Material material) {
        return new ItemBuilder(new ItemStack(material, 1));
    }
}

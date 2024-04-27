package dev.tqqn.kireiwalls.utils;

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

    /**
     * Constructs an ItemBuilder with the specified material and amount.
     *
     * @param material The material of the item.
     * @param amount   The amount of the item.
     */
    public ItemBuilder(Material material, int amount) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
    }

    /**
     * Updates the ItemMeta of the ItemStack.
     */
    private void updateItemMeta() {
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Sets the display name of the ItemStack.
     *
     * @param name The display name to set.
     * @return The ItemBuilder instance for method chaining.
     */
    public ItemBuilder setDisplayName(String name) {
        itemMeta.displayName(ChatUtil.format(name));
        return this;
    }

    /**
     * Sets the lore of the ItemStack.
     *
     * @param lines The lines of lore to set.
     * @return The ItemBuilder instance for method chaining.
     */
    public ItemBuilder setLore(String... lines) {
        setLore(Arrays.asList(lines));
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

    /**
     * Adds a glow effect to the ItemStack.
     */
    public void setGlow() {
        itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public ItemBuilder hideAttributes() {
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    /**
     * Makes the ItemStack unbreakable.
     */
    public void setUnbreakable() {
        itemMeta.setUnbreakable(true);
    }

    /**
     * Builds the ItemStack with the configured attributes.
     *
     * @return The built ItemStack.
     */
    public ItemStack build() {
        updateItemMeta();
        return itemStack;
    }
}

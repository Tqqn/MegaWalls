package dev.tqqn.megawalls.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public final class PotionBuilder {

    private final ItemStack itemStack;
    private final PotionMeta potionMeta;

    private PotionBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.potionMeta = (PotionMeta) itemStack.getItemMeta();
    }

    public PotionBuilder setPotionEffect(PotionEffect potionEffect) {
        this.potionMeta.addCustomEffect(potionEffect, true);
        return this;
    }

    public PotionBuilder setColor(Color color) {
        this.potionMeta.setColor(color);
        return this;
    }

    public PotionBuilder setItemFlag(ItemFlag itemFlag) {
        this.potionMeta.addItemFlags(itemFlag);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(potionMeta);
        return itemStack;
    }

    public static PotionBuilder getBuilder() {
        return new PotionBuilder(new ItemStack(Material.POTION, 1));
    }
}

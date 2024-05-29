package dev.tqqn.kireiwalls.modules.chest.framework;

import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public final class ChestItem {

    private final ItemStack itemStack;

    private final double chance;
    private final int minAmount;
    private final int maxAmount;

    public ChestItem(ItemStack itemStack, double chance, int minAmount, int maxAmount) {
        this.itemStack = itemStack;
        this.chance = chance;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public boolean shouldSpawn() {
        return ThreadLocalRandom.current().nextDouble() < chance;
    }

    public ItemStack getItem() {
        int amount;

        if (minAmount == maxAmount) {
            amount = maxAmount;
        } else {
            amount = ThreadLocalRandom.current().nextInt(minAmount, maxAmount + 1);
        }

        ItemStack returnItem = new ItemStack(itemStack);
        returnItem.setAmount(amount);
        return returnItem;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        ChestItem chestItem = (ChestItem) object;
        return chestItem.itemStack.getType() == this.itemStack.getType();
    }
}

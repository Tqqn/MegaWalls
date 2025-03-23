package dev.tqqn.megawalls.common.classes.levels.types.kits.object;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public final class KitItem {

    private final ItemStack itemStack;

    private final int place;

    public KitItem(ItemStack itemStack) {
        this(itemStack, -1);
    }

    public KitItem(ItemStack itemStack, int place) {
        this.itemStack = itemStack;
        this.place = place;
    }
}

package dev.tqqn.megawalls.common.classes.levels.types.kits.object;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class KitValue {

    private final List<KitItem> inventoryItems = new ArrayList<>();
    private final EnumMap<EquipmentSlot, ItemStack> armorItems = new EnumMap<>(EquipmentSlot.class);


    public void addValue(ItemStack itemStack) {
        inventoryItems.add(new KitItem(itemStack));
    }

    public void addValue(ItemStack itemStack, int place) {
        inventoryItems.add(new KitItem(itemStack, place));
    }

    public void addValue(ItemStack itemStack, EquipmentSlot equipmentSlot) {
        armorItems.put(equipmentSlot, itemStack);
    }

    public List<KitItem> getInventoryItems() {
        return ImmutableList.copyOf(inventoryItems);
    }

    public Map<EquipmentSlot, ItemStack> getArmorItems() {
        return ImmutableMap.copyOf(armorItems);
    }
}

package dev.tqqn.kireiwalls.modules.chest.framework.types;

import dev.tqqn.kireiwalls.modules.chest.framework.AbstractProtectedContainer;
import dev.tqqn.kireiwalls.modules.chest.framework.ChestItem;
import dev.tqqn.kireiwalls.modules.chest.framework.events.GatheringChestSpawnEvent;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter public final class GatheringChest extends AbstractProtectedContainer {

    private final Set<ChestItem> chestItems;
    private final Location chestLocation;

    public GatheringChest(Location chestLocation, Set<ChestItem> chestItems, UUID ownerUUID) {
        super(ownerUUID);
        this.chestLocation = chestLocation;
        this.chestItems = chestItems;
    }

    public void addChestItem(@NotNull ChestItem item) {
        chestItems.remove(item);
        chestItems.add(item);
    }

    public void spawn(Material brokenMaterial) {
        this.chestLocation.getBlock().setType(Material.TRAPPED_CHEST);
        Chest chest = (Chest) chestLocation.getBlock().getState();

        final Inventory chestInventory = chest.getBlockInventory();
        chestInventory.clear();

        GatheringChestSpawnEvent gatheringChestSpawnEvent = new GatheringChestSpawnEvent(PlayerModule.getPlayerModel(getOwnerUUID()), this);
        Bukkit.getPluginManager().callEvent(gatheringChestSpawnEvent);

        if (gatheringChestSpawnEvent.isCancelled()) return;

        for (ChestItem chestItem : gatheringChestSpawnEvent.getGatheringChest().getChestItems()) {
            if (!chestItem.shouldSpawn()) continue;
            int randomSlot = ThreadLocalRandom.current().nextInt(1, chestInventory.getSize()-1);

            chestInventory.setItem(0, new ItemStack(brokenMaterial));

            while (chestInventory.getItem(randomSlot) != null || randomSlot == chestInventory.getSize()) {
                randomSlot = randomSlot+1;
            }
            chestInventory.setItem(randomSlot, chestItem.getItem());
        }
    }
}

package dev.tqqn.kireiwalls.modules.game.disabledfunctions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class DisabledFunctionsListener implements Listener {

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();

        ItemMeta itemMeta = item.getItemMeta();

        if (!itemMeta.hasLocalizedName()) return;
        if (!itemMeta.getLocalizedName().equals("kit")) return;
        if (item.getType() != Material.POTION) {
            event.setCancelled(true);
            return;
        }

        event.getItemDrop().remove();
        final Player player = event.getPlayer();

        if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) == null) {
            event.setCancelled(true);
            return;
        }

        final ItemStack heldItem = player.getInventory().getItem(player.getInventory().getHeldItemSlot());

        if (heldItem.isSimilar(item)) {
            heldItem.setAmount(heldItem.getAmount()+1);
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getItemInHand().getType() == Material.AIR) return;
        ItemStack item = event.getItemInHand();
        ItemMeta itemMeta = item.getItemMeta();

        if (!itemMeta.hasLocalizedName()) return;
        if (itemMeta.getLocalizedName().equals("kit")) event.setCancelled(true);
    }
}

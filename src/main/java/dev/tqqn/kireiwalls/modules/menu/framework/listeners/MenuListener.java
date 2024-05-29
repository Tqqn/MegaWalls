package dev.tqqn.kireiwalls.modules.menu.framework.listeners;

import dev.tqqn.kireiwalls.modules.menu.framework.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * The MenuListener class represents a listener for menu-related events such as clicks and closures.
 */
public final class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder inventoryHolder = event.getView().getTopInventory().getHolder();

        if (inventoryHolder instanceof Menu) {
            ((Menu) inventoryHolder).handleClick(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (inventoryHolder instanceof Menu) {
            ((Menu) inventoryHolder).handleClose();
        }
    }
}

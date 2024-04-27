package dev.tqqn.kireiwalls.framework.menu;

import dev.tqqn.kireiwalls.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu implements InventoryHolder {

    private final Inventory inventory;
    private final Map<MenuButton, Integer> buttons;
    private final Player viewer;

    public Menu(String title, int rows, Player viewer) {
        if (rows > 6 || rows < 1 || title.length() > 32) {
            throw new IllegalArgumentException("Invalid arguments passed to menu constructor.");
        }
        this.inventory = Bukkit.createInventory(null, rows, ChatUtil.format(title));
        this.buttons = new HashMap<>();
        this.viewer = viewer;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void open() {
        reload();
        viewer.openInventory(inventory);
    }

    private void reload() {

    }

    public void handleClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        for (MenuButton menuButton : buttons.keySet()) {
            if (event.getCurrentItem().isSimilar(menuButton.getItemStack())) {
                event.setCancelled(true);
            }
        }
    }

    protected void registerButton(MenuButton button, int slot) {
        inventory.setItem(slot, button.getItemStack());
    }

    public void registerFillerItem(FillerType fillerType, ItemStack itemStack) {
        switch (fillerType) {
            case BORDER -> {
                // Top side
                for (int i = 0; i < 9; i++) {
                    registerButton(new MenuButton(itemStack), i);
                }
                // Right side
                for (int i = 8; i < inventory.getSize(); i += 9) {
                    registerButton(new MenuButton(itemStack), i);
                }
                // Left side
                for (int i = 0; i < inventory.getSize(); i += 9) {
                    registerButton(new MenuButton(itemStack), i);
                }
                // Bottom side
                for (int i = inventory.getSize() -9; i < inventory.getSize(); i++) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
            case BOTTOM -> {
                for (int i = inventory.getSize() -9; i < inventory.getSize(); i++) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
            case FULL -> {
                for (int i = 0; i < inventory.getSize(); i++) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
            case LEFT -> {
                for (int i = 0; i < inventory.getSize(); i += 9) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
            case RIGHT -> {
                for (int i = 8; i < inventory.getSize(); i += 9) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
            case TOP -> {
                for (int i = 0; i < 9; i++) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
        }
    }

    public enum FillerType {
        BORDER,
        BOTTOM,
        FULL,
        LEFT,
        RIGHT,
        TOP;
    }


}

package dev.tqqn.kireiwalls.framework.menu;

import dev.tqqn.kireiwalls.modules.menu.MenuModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Menu {

    private final MenuModule menuModule;
    private final Inventory inventory;
    private final Map<Integer, MenuButton> buttonMap;

    /**
     * Constructs a new Menu object with the specified title, number of rows, and MenuModule.
     *
     * @param title      The title of the menu.
     * @param rows       The number of rows in the menu.
     * @param menuModule The MenuModule instance.
     * @throws IllegalArgumentException if the number of rows is invalid or the title length exceeds 32 characters.
     */
    public Menu(String title, int rows, MenuModule menuModule) {
        this.menuModule = menuModule;
        if (rows > 6 || rows < 1 || title.length() > 32) {
            throw new IllegalArgumentException("Invalid arguments passed to menu constructor.");
        }
        this.inventory = Bukkit.createInventory(null, rows * 9, ChatUtil.translateLegacy(title));
        this.buttonMap = new HashMap<>();
    }

    /**
     * Registers a filler item in the menu.
     *
     * @param fillerType The type of filler item to register.
     * @param itemStack  The ItemStack representing the filler item.
     */
    public void registerFillerItem(FillerType fillerType, ItemStack itemStack) {
        switch (fillerType) {
            case BORDER -> {
                // Top side
                for (int i = 0; i < 9; i++) {
                    buttonMap.put(i, new MenuButton(itemStack));
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

    /**
     * Registers a button in the menu at the specified slot.
     *
     * @param button The MenuButton to register.
     * @param slot   The slot where the button should be placed.
     */
    public void registerButton(MenuButton button, int slot) {
        buttonMap.put(slot, button);
    }

    /**
     * Handles a click event in the menu.
     *
     * @param event The InventoryClickEvent to handle.
     */
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null) return;
        if (!buttonMap.containsKey(event.getRawSlot())) return;

        Consumer<Player> consumer = buttonMap.get(event.getRawSlot()).getWhoClicked();

        if (consumer == null) return;

        consumer.accept((Player) event.getWhoClicked());
    }

    /**
     * Opens the menu for the specified player.
     *
     * @param player The player to whom the menu should be opened.
     */
    public void open(Player player) {

        buttonMap.forEach((slot, button) -> inventory.setItem(slot, button.getItemStack()));

        player.openInventory(inventory);
        menuModule.registerMenu(player.getUniqueId(), this);
    }

    /**
     * Enums of different types of filler items for the menu.
     */
    public enum FillerType {
        BORDER,
        BOTTOM,
        FULL,
        LEFT,
        RIGHT,
        TOP;
    }
}

package dev.tqqn.megawalls.modules.menu.framework;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * The MenuButton class represents a button in a menu interface.
 * It contains an ItemStack and a clicker function to handle button clicks.
 */
@Getter
public final class MenuButton {

    private final ItemStack itemStack;

    private Consumer<Player> clicker;

    /**
     * Constructs a MenuButton with the specified ItemStack.
     *
     * @param itemStack The ItemStack representing the appearance of the button.
     */
    public MenuButton(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Sets the clicker function for the button.
     *
     * @param clicker The clicker function to handle button clicks.
     * @return The MenuButton instance with the clicker function set.
     */
    public MenuButton setClicker(Consumer<Player> clicker) {
        this.clicker = clicker;
        return this;
    }
}

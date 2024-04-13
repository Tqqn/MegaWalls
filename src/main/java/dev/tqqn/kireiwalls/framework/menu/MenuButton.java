package dev.tqqn.kireiwalls.framework.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Getter
public class MenuButton {

    private final ItemStack itemStack;
    /**
     *  Returns the Consumer (Player/whoClicked).
     */
    private Consumer<Player> whoClicked;

    /**
     * Creates a new MenuButton Object.
     *
     * @param itemStack ItemStack
     */
    public MenuButton(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Sets the Consumer (Player) and returns this MenuButton.
     *
     * @param whoClicked Consumer(Player)
     */
    public MenuButton setWhoClicked(Consumer<Player> whoClicked) {
        this.whoClicked = whoClicked;
        return this;
    }
}

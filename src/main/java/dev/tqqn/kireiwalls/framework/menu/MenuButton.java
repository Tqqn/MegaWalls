package dev.tqqn.kireiwalls.framework.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Getter
public class MenuButton {

    private final ItemStack itemStack;

    private Consumer<Player> clicker;

    public MenuButton(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public MenuButton setClicker(Consumer<Player> clicker) {
        this.clicker = clicker;
        return this;
    }



}

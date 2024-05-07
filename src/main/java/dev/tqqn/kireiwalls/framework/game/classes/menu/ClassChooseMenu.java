package dev.tqqn.kireiwalls.framework.game.classes.menu;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.menu.Menu;
import dev.tqqn.kireiwalls.framework.menu.MenuButton;
import dev.tqqn.kireiwalls.modules.classes.ClassModule;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import dev.tqqn.kireiwalls.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The ClassChooseMenu class represents a menu for choosing player classes.
 * It extends the Menu class and provides functionality to display class options
 * and select a class for the player.
 */
public final class ClassChooseMenu extends Menu {

    /**
     * Constructs a new Menu object with the specified title, number of rows, and MenuModule.
     *
     * @throws IllegalArgumentException if the number of rows is invalid or the title length exceeds 32 characters.
     */
    public ClassChooseMenu(PlayerModel viewer) {
        super("<red>Choose a Class!", 3, viewer.getPlayer());
        int slot = 0;
        for (AbstractClass abstractClass : ClassModule.getClasses()) {
            registerButton(new MenuButton(abstractClass.getKitIcon(viewer)).setClicker(clicked -> {
                PlayerModel playerModel = PlayerModule.getPlayerModel(clicked.getUniqueId());

                if (playerModel.getCurrentClass() == abstractClass) {
                    playerModel.getPlayer().sendMessage(ChatUtil.format("<red>You have already selected this class!"));
                    close();
                    return;
                }

                playerModel.setCurrentClass(abstractClass);
                playerModel.getPlayer().sendMessage(ChatUtil.format("<green>You have selected the <yellow>" + abstractClass.getName() + "<green> class!"));
                close();
            }), slot++);
        }

        registerCloseButton(getInventory().getSize() - 5);
        registerButton(getRandomButton(), getInventory().getSize() -4);
    }

    /**
     * Creates and returns a MenuButton for selecting a random class.
     *
     * @return The MenuButton for selecting a random class.
     */
    public MenuButton getRandomButton() {

        final PlayerModel playerModel = PlayerModule.getPlayerModel(getViewer().getUniqueId());
        List<String> lore = new ArrayList<>();

        lore.add("&7Picks a random class from your");
        lore.add("&7collection!");
        lore.add(" ");
        lore.add("&7You are rewarded &6+30% coins &7when");
        lore.add("&7picking this option!");
        lore.add("");

        if (PlayerModule.getPlayerModel(getViewer().getUniqueId()).getCurrentClass() == null) {
            lore.add("&a&lSelected!");
        } else {
            lore.add("&eClick to select!");
        }

        MenuButton randomButton = new MenuButton(ItemBuilder.getBuilder(Material.NETHER_STAR).hideAttributes().setDisplayName("&aRandom!").setLore(lore).build());
        randomButton.setClicker(player -> {
            if (playerModel.getCurrentClass() == null) {
                player.sendMessage(ChatUtil.format("<red>You have already selected the random class!"));
                close();
                return;
            }
            playerModel.setCurrentClass(null);
            player.sendMessage(ChatUtil.format("<green>You have selected the <yellow>Random <green>option!"));
            close();
        });
        return randomButton;
    }

    @Override
    public void reload() {

    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose(Player viewer) {

    }
}

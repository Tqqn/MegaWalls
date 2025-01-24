package dev.tqqn.megawalls.modules.classes.framework.menu;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.classes.framework.AbstractClass;
import dev.tqqn.megawalls.modules.menu.framework.Menu;
import dev.tqqn.megawalls.modules.menu.framework.MenuButton;
import dev.tqqn.megawalls.modules.classes.ClassModule;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.utils.ChatUtil;
import dev.tqqn.megawalls.utils.ItemBuilder;
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
                final PlayerModel playerModel = PlayerModule.getPlayerModel(clicked.getUniqueId());

                final AbstractClass playerClass = playerModel.getTempPlayerData().getCurrentClass();

                if (playerClass != null) {
                    if (playerClass == abstractClass) {
                        clicked.sendMessage(ChatUtil.format("<red>You have already selected this class!"));
                        close();
                        return;
                    }
                }

                playerModel.getTempPlayerData().setCurrentClass(abstractClass);
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
        final AbstractClass playerClass = playerModel.getTempPlayerData().getCurrentClass();
        final List<String> lore = new ArrayList<>();

        lore.add("<gray>Picks a random class from your");
        lore.add("<gray>collection!");
        lore.add(" ");
        lore.add("<gray>You are rewarded <gold>+30% coins <gray>when");
        lore.add("<gray>picking this option!");
        lore.add("");

        if (playerClass == null) {
            lore.add("<gray><bold>Selected!");
        } else {
            lore.add("<yellow>Click to select!");
        }

        final MenuButton randomButton = new MenuButton(ItemBuilder.getBuilder(Material.NETHER_STAR).hideAttributes().setDisplayName("<green>Random!").setLore(lore).build());
        randomButton.setClicker(player -> {
            if (playerClass == null) {
                player.sendMessage(ChatUtil.format("<red>You have already selected the random class!"));
                close();
                return;
            }
            playerModel.getTempPlayerData().setCurrentClass(null);
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
        MegaWalls.getInstance().getModuleManager().getModule(GameModule.class).giveLobbyItems(PlayerModule.getPlayerModel(viewer.getUniqueId()));
    }
}

package dev.tqqn.kireiwalls.framework.game.classes.menu;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.game.classes.ClassDescriptions;
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

public class ClassChooseMenu extends Menu {

    /**
     * Constructs a new Menu object with the specified title, number of rows, and MenuModule.
     *
     * @throws IllegalArgumentException if the number of rows is invalid or the title length exceeds 32 characters.
     */
    public ClassChooseMenu(PlayerModel viewer) {
        super("<red>Choose a Class!", 3, viewer.getPlayer());
        ClassModule classModule = (ClassModule) KireiWalls.getInstance().getModuleManager().getModule(ClassModule.class);
        int slot = 0;
        for (AbstractClass abstractClass : classModule.getClasses()) {
            registerButton(new MenuButton(abstractClass.getKitIcon(viewer)).setClicker(clicked -> {
                PlayerModel playerModel = PlayerModule.getPlayerModel(clicked.getUniqueId());

                if (playerModel.getCurrentClass() == abstractClass) {
                    playerModel.getPlayer().sendMessage(ChatUtil.format("<green>"));
                }

                playerModel.setCurrentClass(abstractClass);
                playerModel.getPlayer().sendMessage(ChatUtil.format("<green>You have selected the <yellow>" + abstractClass.getName() + "<green> class!"));
                close();
            }), slot++);
        }

        registerCloseButton(getInventory().getSize() - 5);
        registerButton(getRandomButton(), getInventory().getSize() -4);
    }

    public MenuButton getRandomButton() {

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
            PlayerModule.getPlayerModel(player.getUniqueId()).setCurrentClass(null);
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

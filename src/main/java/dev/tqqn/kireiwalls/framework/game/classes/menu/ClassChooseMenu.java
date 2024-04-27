package dev.tqqn.kireiwalls.framework.game.classes.menu;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.menu.Menu;
import dev.tqqn.kireiwalls.framework.menu.MenuButton;
import dev.tqqn.kireiwalls.modules.classes.ClassModule;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.utils.ItemBuilder;
import org.bukkit.Material;

public class ClassChooseMenu extends Menu {
    /**
     * Constructs a new Menu object with the specified title, number of rows, and MenuModule.
     *
     * @throws IllegalArgumentException if the number of rows is invalid or the title length exceeds 32 characters.
     */
    public ClassChooseMenu(PlayerModel viewer) {
        super("<red>Choose a Class!", 3);
        ClassModule classModule = (ClassModule) KireiWalls.getInstance().getModuleManager().getModule(ClassModule.class);
        int slot = 0;
        for (AbstractClass abstractClass : classModule.getClasses()) {
            registerButton(new MenuButton(abstractClass.getKitIcon(viewer)).setWhoClicked(clicked -> {
                PlayerModel playerModel = PlayerModule.getPlayerModel(clicked.getUniqueId());
                playerModel.setCurrentClass(abstractClass);
            }), slot++);
        }

        registerCloseButton(getSize() - 4);
        registerButton(getRandomButton(), getSize() -3);
    }

    public MenuButton getRandomButton() {
        MenuButton randomButton = new MenuButton(new ItemBuilder(Material.NETHER_STAR, 1).hideAttributes().setDisplayName("&aRandom!").setLore("&7Picks a random class from your", "&7collection!", " ", "&7You are rewarded &6+30% cions &7when", "&7picking this option!", " ", "&eClick to select!").build());
        randomButton.setWhoClicked(this::close); //TODO: Random Class
        return randomButton;
    }
}

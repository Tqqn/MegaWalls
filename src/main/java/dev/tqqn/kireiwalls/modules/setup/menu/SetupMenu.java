package dev.tqqn.kireiwalls.modules.setup.menu;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.menu.Menu;
import dev.tqqn.kireiwalls.framework.menu.MenuButton;
import dev.tqqn.kireiwalls.modules.setup.SetupModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import dev.tqqn.kireiwalls.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SetupMenu extends Menu {

    private final SetupModule setupModule;

    /**
     * Constructs a Menu with the specified title, number of rows, and viewer.
     *
     * @param viewer The player viewing the menu.
     * @throws IllegalArgumentException if the number of rows is invalid or the title length exceeds 32 characters.
     */
    public SetupMenu(Player viewer) {
        super("<red>Setup Menu", 1, viewer);
        this.setupModule = KireiWalls.getInstance().getModuleManager().getModule(SetupModule.class);

        registerButton(new MenuButton(ItemBuilder.getBuilder(Material.RED_BANNER).setDisplayName("&cRed Team").build()).setClicker(clicked -> {
            setupModule.giveTeamSetupItems(clicked, SetupModule.Teams.RED);
            viewer.sendMessage(ChatUtil.format("<green>You entered the setup-mode of team <red><bold>Red<reset><green>."));
            close();
        }), 2);

        registerButton(new MenuButton(ItemBuilder.getBuilder(Material.BLUE_BANNER).setDisplayName("&9Blue Team").build()).setClicker(clicked -> {
            setupModule.giveTeamSetupItems(clicked, SetupModule.Teams.BLUE);
            viewer.sendMessage(ChatUtil.format("<green>You entered the setup-mode of team <blue><bold>Blue<reset><green>."));
            close();
        }), 3);

        registerButton(new MenuButton(ItemBuilder.getBuilder(Material.GREEN_BANNER).setDisplayName("&aGreen Team").build()).setClicker(clicked -> {
            setupModule.giveTeamSetupItems(clicked, SetupModule.Teams.GREEN);
            viewer.sendMessage(ChatUtil.format("<green>You entered the setup-mode of team <bold>Green<reset><green>."));
            close();
        }), 5);

        registerButton(new MenuButton(ItemBuilder.getBuilder(Material.YELLOW_BANNER).setDisplayName("&eYellow Team").build()).setClicker(clicked -> {
            setupModule.giveTeamSetupItems(clicked, SetupModule.Teams.YELLOW);
            viewer.sendMessage(ChatUtil.format("<green>You entered the setup-mode of team <yellow><bold>Yellow<reset><green>."));
            close();
        }), 6);
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

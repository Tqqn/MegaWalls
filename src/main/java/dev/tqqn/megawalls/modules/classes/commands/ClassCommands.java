package dev.tqqn.megawalls.modules.classes.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.tqqn.megawalls.modules.classes.framework.menu.ClassChooseMenu;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import org.bukkit.entity.Player;

@CommandAlias("class")
@CommandPermission("mw.class")
public final class ClassCommands extends BaseCommand {

    @CommandAlias("kit")
    @Subcommand("kit")
    @Description("Open the kit menu.")
    @CommandPermission("mw.class.kit")
    public void openKit(Player player) {
        new ClassChooseMenu(PlayerModule.getPlayerModel(player.getUniqueId())).open();
    }
}

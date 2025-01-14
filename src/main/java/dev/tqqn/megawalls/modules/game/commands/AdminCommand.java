package dev.tqqn.megawalls.modules.game.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.utils.Notify;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandAlias("admin")
@CommandPermission("mw.admin")
public final class AdminCommand extends BaseCommand {

    @Subcommand("build")
    @CommandAlias("build")
    @Description("Enable/Disable your build mode.")
    @CommandPermission("mw.admin.build")
    public void build(Player player, @Optional OnlinePlayer onlinePlayer) {
        if (onlinePlayer == null) {
            handleBuildMode(player);
            return;
        }

        final Player target = onlinePlayer.getPlayer();

        final boolean value = handleBuildMode(target);
        Notify.SUCCESS.chat(player, "You successfully " + (value ? "<green>enabled" : "<red>disabled") + " <default>build-mode of player <primary>" + target.getName() + "<default>.");
    }

    private boolean handleBuildMode(Player player) {
        final PlayerModel playerModel = PlayerModule.getPlayerModel(player.getUniqueId());
        final boolean buildMode = playerModel.getTempPlayerData().isBuildMode();

        playerModel.getTempPlayerData().setBuildMode(!buildMode);
        setBuildMode(player, buildMode);
        return !buildMode;
    }

    private void setBuildMode(Player player, boolean buildMode) {
        GameMode gameMode;
        if (!buildMode) {
            gameMode = GameMode.CREATIVE;
        } else {
            gameMode = GameMode.SURVIVAL;
        }

        Notify.INFO.chat(player, "Build-Mode has been " + (buildMode ? "<red>disabled" : "<green>enabled") + "<default>.");
        player.setGameMode(gameMode);
    }
}

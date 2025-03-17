package dev.tqqn.megawalls.modules.game.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.modules.player.data.TempPlayerData;
import dev.tqqn.megawalls.modules.teams.TeamModule;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import dev.tqqn.megawalls.modules.teams.framework.wither.GameWither;
import dev.tqqn.megawalls.common.utils.Notify;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandAlias("admin")
@CommandPermission("mw.admin")
@RequiredArgsConstructor
public final class AdminCommand extends BaseCommand {

    private final TeamModule teamModule;

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

    @Subcommand("spectate")
    @Description("Toggle spectator mode.")
    @CommandPermission("mw.admin.spectate")
    public void spectate(Player player) {
        final PlayerModel playerModel = PlayerModule.getPlayerModel(player.getUniqueId());
        if (playerModel == null) return;
        final TempPlayerData tempPlayerData = playerModel.getTempPlayerData();

        final boolean value = tempPlayerData.isSpectatorMode();

        tempPlayerData.setSpectatorMode(!value);

        Notify.SUCCESS.chat(player, "You " + (value ? "<red>disabled" : "<green>enabled") + " <primary>spectator-mode.");
    }

    @Subcommand("damageWither")
    @Description("Damage a wither")
    @CommandPermission("mw.admin.damagewither")
    public void damageWither(Player player, TeamModule.TeamStaticData team, int damage) {
        final GameTeam gameTeam = teamModule.getTeam(team);
        if (gameTeam.getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH) {
            Notify.ERROR.chat(player, "This wither is already dead.");
            return;
        }
        gameTeam.getGameWither().damage(damage);
        Notify.SUCCESS.chat(player, "You successfully damaged " + team.getColor() + team.getPrettyName() + "'s <default>Wither by <primary>" + damage + "<default>.");
    }
}

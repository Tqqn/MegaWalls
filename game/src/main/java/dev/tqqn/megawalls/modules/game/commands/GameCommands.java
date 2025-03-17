package dev.tqqn.megawalls.modules.game.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.game.states.active.ActiveState;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.common.utils.ChatUtil;
import dev.tqqn.megawalls.common.utils.MessageUtil;
import dev.tqqn.megawalls.common.utils.Notify;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("game")
@CommandPermission("mw.game")
@RequiredArgsConstructor
public final class GameCommands extends BaseCommand {

    private final GameModule gameModule;

    @CommandAlias("shout")
    @Description("Shouts to every player in the game. Only usable when game is active.")
    public void shout(Player player, String message) {
        if (!gameModule.isState(GameStates.ACTIVE)) {
            Notify.ERROR.chat(player, "This command is only enabled during the active game.");
            return;
        }

        final PlayerModel playerModel = PlayerModule.getPlayerModel(player.getUniqueId());
        if (playerModel == null) return;

        if (playerModel.getTempPlayerData().getGameTeam() == null) {
            Notify.ERROR.chat(player, "You cannot use shouts because you are not in a team.");
            return;
        }

        final TextComponent formattedMessage = Component.empty().content(message);

        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(ChatUtil.format(MessageUtil.SHOUT_PREFIX.getStringMessage() + " ").append(playerModel.getTempPlayerData().getChatMessage(formattedMessage)));
        }
    }

    @Subcommand("start")
    @Description("Start the game.")
    @CommandPermission("mw.game.start")
    public void start(Player player) {
        if (!gameModule.isState(GameStates.WAITING)) {
            Notify.ERROR.chat(player, "The game cannot be started when its already active or ended.");
            return;
        }

        gameModule.setGameState(GameStates.ACTIVE);
        Notify.SUCCESS.chat(player, "You force started the game.");
    }

    @Subcommand("end")
    @Description("End the game.")
    @CommandPermission("mw.game.end")
    public void end(Player player) {
        if (gameModule.isState(GameStates.END)) {
            Notify.ERROR.chat(player, "The game cannot be ended as its already in the end state.");
            return;
        }

        if (!(gameModule.getCurrentState() instanceof ActiveState activeState)) {
            Notify.ERROR.chat(player, "Use /game forceend to end a NOT active game.");
            return;
        }

        activeState.setCycle(ActiveState.Cycle.END);
        Notify.SUCCESS.chat(player, "You started the end of the game.");
    }

    @Subcommand("forceend")
    @Description("Force end the game.")
    @CommandPermission("mw.game.force.end")
    public void forceEnd(Player player) {
        gameModule.setGameState(GameStates.FORCE_END);
        Notify.SUCCESS.chat(player, "You force ended the game.");
    }

    @Subcommand("setcycle")
    @Description("Set the Active Cycle.")
    @CommandPermission("mw.game.active.nextcycle")
    public void setCycle(Player player, ActiveState.Cycle cycle) {
        if (gameModule.isState(GameStates.END)) {
            Notify.ERROR.chat(player, "The Active Cycle cannot be changed when the game already ended.");
            return;
        }

        if (!gameModule.isState(GameStates.ACTIVE)) {
            Notify.ERROR.chat(player, "The Active Cycle cannot be changed when its not active. Start the game first.");
            return;
        }

        final ActiveState activeState = (ActiveState) gameModule.getCurrentState();
        if (activeState.setCycle(cycle)) {
            Notify.SUCCESS.chat(player, "You successfully set the current active state to <primary>" + cycle + "<default>.");
        } else {
            Notify.ERROR.chat(player, "The Active Cycle could not be changed.");
        }
    }
}

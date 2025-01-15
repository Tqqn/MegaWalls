package dev.tqqn.megawalls.modules.game.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.game.states.active.ActiveState;
import dev.tqqn.megawalls.utils.Notify;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@CommandAlias("game")
@CommandPermission("mw.game")
@RequiredArgsConstructor
public final class GameCommands extends BaseCommand {

    private final GameModule gameModule;

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

        gameModule.setGameState(GameStates.END);
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

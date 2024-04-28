package dev.tqqn.kireiwalls.modules.game.commands;

import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.modules.game.teams.TeamModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WitherDebugCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 0) {
                player.sendMessage(ChatUtil.format("<red>Wrong Argument."));
                return true;
            } else {
                GameTeam gameTeam = TeamModule.getGameTeams().get(strings[0]);
                if (gameTeam == null) {
                    return false;
                } else if (strings.length < 2) {
                    return false;
                } else {
                    gameTeam.getGameWither().damage(Integer.parseInt(strings[1]));
                    gameTeam.getGameWither().updateHealth();
                    String teamName = gameTeam.getName();
                    player.sendMessage(ChatUtil.format("<green>Damaged " + teamName + " Wither by " + strings[1]));
                    return true;
                }
            }
        } else {
            return true;
        }
    }
}

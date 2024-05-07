package dev.tqqn.kireiwalls.modules.game.commands;

import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The SpectateCommand class implements the CommandExecutor interface and represents a command executor for the spectate command.
 * It allows players to toggle their spectator mode.
 */
public final class SpectateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 0) {
                if (PlayerModule.getPlayerModel(player.getUniqueId()).isSpectatorMode()) {
                    PlayerModule.getPlayerModel(player.getUniqueId()).setSpectatorMode(false);
                    player.sendMessage(ChatUtil.format("<red>You have disabled spectator mode."));
                } else {
                    PlayerModule.getPlayerModel(player.getUniqueId()).setSpectatorMode(true);
                    player.sendMessage(ChatUtil.format("<green>You have enabled spectator mode."));
                }
            }
        }
        return true;
    }
}

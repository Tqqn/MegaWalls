package dev.tqqn.kireiwalls.modules.game.commands;

import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The BuildCommand class implements the CommandExecutor interface and represents a command executor for the build command.
 * It allows players to toggle their build mode.
 */
public final class BuildCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 0) {
                if (PlayerModule.getPlayerModel(player.getUniqueId()).isBuildMode()) {
                    PlayerModule.getPlayerModel(player.getUniqueId()).setBuildMode(false);
                    player.sendMessage(ChatUtil.format("<red>You have disabled build mode."));
                } else {
                    PlayerModule.getPlayerModel(player.getUniqueId()).setBuildMode(true);
                    player.sendMessage(ChatUtil.format("<green>You have enabled build mode."));
                }
            }
        }
        return true;
    }
}

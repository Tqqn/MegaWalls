package dev.tqqn.kireiwalls.modules.setup.commands;

import dev.tqqn.kireiwalls.framework.setup.model.SetupPlayer;
import dev.tqqn.kireiwalls.modules.setup.SetupModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SelectCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 0) {
                player.sendMessage(ChatUtil.format("<red>Wrong argument."));
                return true;
            }

            final SetupPlayer setupPlayer = SetupModule.getSetupPlayer(player.getUniqueId());

            switch (strings[0].toUpperCase()) {
                case "POS1" -> {
                    setupPlayer.setSelectionOne(player.getLocation());
                    player.sendMessage(ChatUtil.format("<green>Selected point 1."));
                }

                case "POS2" -> {
                    setupPlayer.setSelectionTwo(player.getLocation());
                    player.sendMessage(ChatUtil.format("<green>Selected point 2."));
                }

                default -> {
                    player.sendMessage(ChatUtil.format("<red>Invalid Argument. Try pos1 or pos2."));
                }
            }
        }
        return true;
    }

}

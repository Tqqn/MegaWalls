package dev.tqqn.kireiwalls.modules.game.commands;

import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.game.framework.GameStates;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import dev.tqqn.kireiwalls.utils.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShoutCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player sender) {
            if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) {
                sender.sendMessage(ChatUtil.format("<red>This command is only enabled during the active game!"));
                return true;
            }

            final PlayerModel playerModel = PlayerModule.getPlayerModel(sender.getUniqueId());

            if (playerModel.getTempPlayerData().isSpectatorMode()) {
                sender.sendMessage(ChatUtil.format("<red>You cannot use shouts as a spectator."));
                return true;
            }

            if (playerModel.getTempPlayerData().getGameTeam() == null) {
                sender.sendMessage(ChatUtil.format("<red>Cannot use shouts as you are not in a team."));
            }

            if (args.length > 0) {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    if (i > 0) message.append(" ");
                    message.append(args[i]);
                }

                TextComponent shoutMessage = Component.text().content(message.toString()).build();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatUtil.format(MessageUtil.SHOUT_PREFIX.getStringMessage() + " ").append(playerModel.getTempPlayerData().getChatMessage(shoutMessage)));
                }
                Bukkit.getLogger().info("[SHOUT]" + ((TextComponent) playerModel.getTempPlayerData().getChatMessage(shoutMessage)).content() + shoutMessage.content());
            }
        }

        return true;
    }
}

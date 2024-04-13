package dev.tqqn.kireiwalls.modules.game.commands;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;
        if (strings.length == 0) {
            player.sendMessage(ChatUtil.format("<red>Wrong Argument."));
            return true;
        }

        if (!isNumber(strings[0])) {
            player.sendMessage(ChatUtil.format("<red>This is not a number."));
            return true;
        }


        PlayerModel playerModel = PlayerModule.getPlayerModel(player.getUniqueId());
        playerModel.setCoins(playerModel.getCoins() + Integer.parseInt(strings[0]));

        player.sendMessage(ChatUtil.format("<green>Increased coins by <gold>" + strings[0] + "<green>."));
        return true;
    }

    private boolean isNumber(String number) {
        return StringUtils.isNumeric(number);
    }
}

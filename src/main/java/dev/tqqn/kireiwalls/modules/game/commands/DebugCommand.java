package dev.tqqn.kireiwalls.modules.game.commands;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.framework.game.classes.menu.ClassChooseMenu;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DebugCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 0) {
                player.sendMessage(ChatUtil.format("<red>Wrong Argument."));
                return true;
            } else {
                GameModule gameModule;
                if (strings[0].equalsIgnoreCase("end")) {
                    gameModule = (GameModule) KireiWalls.getInstance().getModuleManager().getModule(GameModule.class);
                    gameModule.endGame();
                    return true;
                } else if (strings[0].equalsIgnoreCase("start")) {
                    gameModule = (GameModule)KireiWalls.getInstance().getModuleManager().getModule(GameModule.class);
                    gameModule.setGameState(GameStates.ACTIVE);
                    return true;
                } else if (strings[0].equalsIgnoreCase("kit")) {
                    new ClassChooseMenu(PlayerModule.getPlayerModel(player.getUniqueId())).open();
                    return true;
                } else if (!this.isNumber(strings[0])) {
                    player.sendMessage(ChatUtil.format("<red>This is not a number."));
                    return true;
                } else {
                    PlayerModel playerModel = PlayerModule.getPlayerModel(player.getUniqueId());
                    playerModel.increaseCoins(Integer.parseInt(strings[0]));
                    player.sendMessage(ChatUtil.format("<green>Increased coins by <gold>" + strings[0] + "<green>."));
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    private boolean isNumber(String number) {
        return StringUtils.isNumeric(number);
    }
}

package dev.tqqn.kireiwalls.modules.game.commands;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.game.framework.GameStates;
import dev.tqqn.kireiwalls.modules.classes.framework.menu.ClassChooseMenu;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.ActiveState;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The DebugCommand class implements the CommandExecutor interface and represents a command executor for debug commands.
 * It allows players to perform various debug actions.
 */
public final class DebugCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 0) {
                player.sendMessage(ChatUtil.format("<red>Wrong Argument."));
                return true;
            } else {
                GameModule gameModule = KireiWalls.getInstance().getModuleManager().getModule(GameModule.class);
                if (strings[0].equalsIgnoreCase("end")) {
                    gameModule.endGame();
                    return true;
                } else if (strings[0].equalsIgnoreCase("start")) {
                    gameModule.setGameState(GameStates.ACTIVE);
                    return true;
                } else if (strings[0].equalsIgnoreCase("kit")) {
                    new ClassChooseMenu(PlayerModule.getPlayerModel(player.getUniqueId())).open();
                    return true;
                } else if (strings[0].equalsIgnoreCase("nextcycle")) {
                    ActiveState.nextCycle();
                    player.sendMessage(ChatUtil.format("<green>Cycled to next cycle."));
                    return true;
                } else if (!this.isNumber(strings[0])) {
                    player.sendMessage(ChatUtil.format("<red>This is not a number."));
                    return true;
                } else {
                    PlayerModel playerModel = PlayerModule.getPlayerModel(player.getUniqueId());
                    playerModel.getTempPlayerData().increaseCoins(Integer.parseInt(strings[0]));
                    player.sendMessage(ChatUtil.format("<green>Increased coins by <gold>" + strings[0] + "<green>."));
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    /**
     * Checks if a string is a number.
     *
     * @param number The string to check.
     * @return true if the string is a number, otherwise false.
     */
    private boolean isNumber(String number) {
        return StringUtils.isNumeric(number);
    }
}

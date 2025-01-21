package dev.tqqn.megawalls.modules.setup.commands;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.setup.SetupModule;
import dev.tqqn.megawalls.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetupCommand implements CommandExecutor {

    private final SetupModule setupModule;

    public SetupCommand() {
        this.setupModule = MegaWalls.getInstance().getModuleManager().getModule(SetupModule.class);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 0) {
                player.sendMessage(ChatUtil.format("<red>Wrong argument."));
                return true;
            }
            if (strings[0].equalsIgnoreCase("loadworld")) {
                if (strings.length < 2) {
                    player.sendMessage(ChatUtil.format("<red>Wrong argument. Need WorldName."));
                    return true;
                }
                setupModule.enableWorld(strings[1]);
            }

            if (strings[0].equalsIgnoreCase("tp")) {
                if (strings.length < 2) {
                    player.sendMessage(ChatUtil.format("<red>Wrong argument. Need WorldName."));
                    return true;
                }
                setupModule.teleportWorld(strings[1], player);
            }
        }
        return true;
    }
}

package dev.tqqn.kireiwalls.modules.setup.commands;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.setup.SetupModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BuildCommand implements CommandExecutor {

    private final SetupModule setupModule;

    public BuildCommand() {
        this.setupModule = KireiWalls.getInstance().getModuleManager().getModule(SetupModule.class);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 0) {
                setupModule.setBuild(SetupModule.getSetupPlayer(player.getUniqueId()));
            }
        }
        return true;
    }
}

package dev.tqqn.megawalls.modules.game.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import dev.tqqn.megawalls.modules.game.GameModule;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@CommandAlias("game")
@CommandPermission("mw.game")
@RequiredArgsConstructor
public final class GameCommands extends BaseCommand {

    private final GameModule gameModule;

    @Subcommand("start")
    @Description("Start the game.")
    @CommandPermission("mw.game.start")
    public void start(Player player) {
        if (gameModule)
    }
}

package dev.tqqn.kireiwalls.modules.game;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.AbstractGameState;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.commands.DebugCommand;
import dev.tqqn.kireiwalls.modules.game.commands.WitherDebugCommand;
import dev.tqqn.kireiwalls.modules.game.states.active.ActiveState;
import dev.tqqn.kireiwalls.modules.game.states.active.board.ActiveBoard;
import dev.tqqn.kireiwalls.modules.game.states.lobby.LobbyState;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class GameModule extends AbstractModule {

    @Getter private static AbstractGameState currentState;
    @Getter private GameSettings gameSettings;

    private final DatabaseModule databaseModule;

    @Getter private final Set<PlayerModel> ingamePlayers;

    public GameModule(KireiWalls plugin, DatabaseModule databaseModule) {
        super(plugin, "Game");
        currentState = new LobbyState(this);
        this.ingamePlayers = new HashSet<>();
        this.databaseModule = databaseModule;
    }

    @Override
    public void onEnable() {
        addComponent(DebugCommand.class, "debug");
        addComponent(WitherDebugCommand.class, "witherdebug");
        this.gameSettings = new GameSettings("DragonKeep", databaseModule.getDefaultConfig().getLobbyLocation(), databaseModule.getDefaultConfig().getLobbyTimer());
        currentState.enable();
    }

    @Override
    public void onDisable() {
        currentState.disable();
    }

    public void setGameState(GameStates gameState) {
        if (currentState.getGameStates() == gameState) return;

        if (currentState.getGameStates() == GameStates.ACTIVE && gameState == GameStates.WAITING) return;

        switch (gameState) {
            case ACTIVE -> {
                currentState.disable();
                currentState = new ActiveState(this);
                currentState.enable();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ScoreboardModule scoreboardModule = (ScoreboardModule) getPlugin().getModuleManager().getModule(ScoreboardModule.class);
                    scoreboardModule.setScoreboard(PlayerModule.getPlayerModel(player.getUniqueId()), new ActiveBoard(PlayerModule.getPlayerModel(player.getUniqueId())));
                }
            }
        }
    }

    public boolean canStart() {
        return true;
    }
}

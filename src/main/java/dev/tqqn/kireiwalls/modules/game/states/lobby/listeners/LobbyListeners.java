package dev.tqqn.kireiwalls.modules.game.states.lobby.listeners;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.events.GamePlayerJoinEvent;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.modules.ModuleManager;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.lobby.board.LobbyBoard;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LobbyListeners implements Listener {

    private final DatabaseModule databaseModule;

    public LobbyListeners() {
        ModuleManager moduleManager = KireiWalls.getInstance().getModuleManager();
        databaseModule = (DatabaseModule) moduleManager.getModule(DatabaseModule.class);
    }

    @EventHandler
    public void onJoin(GamePlayerJoinEvent event) {
        if (GameModule.getCurrentState().getGameStates() == GameStates.WAITING) {

            ScoreboardModule scoreboardModule = (ScoreboardModule) databaseModule.getPlugin().getModuleManager().getModule(ScoreboardModule.class);
            scoreboardModule.setScoreboard(event.getPlayerModel(), new LobbyBoard(event.getPlayerModel()));
        }
    }
}

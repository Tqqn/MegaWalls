package dev.tqqn.kireiwalls.modules.game.states.active.listeners;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.events.GamePlayerJoinEvent;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.framework.game.events.WitherDamageByPlayerEvent;
import dev.tqqn.kireiwalls.modules.ModuleManager;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.board.ActiveBoard;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ActiveListeners implements Listener {
    private final DatabaseModule databaseModule;

    public ActiveListeners() {
        ModuleManager moduleManager = KireiWalls.getInstance().getModuleManager();
        this.databaseModule = (DatabaseModule)moduleManager.getModule(DatabaseModule.class);
    }

    @EventHandler
    public void onJoin(GamePlayerJoinEvent event) {
        if (GameModule.getCurrentState().getGameStates() == GameStates.ACTIVE) {
            ScoreboardModule scoreboardModule = (ScoreboardModule)this.databaseModule.getPlugin().getModuleManager().getModule(ScoreboardModule.class);
            scoreboardModule.setScoreboard(event.getPlayerModel(), new ActiveBoard(event.getPlayerModel()));
        }
    }

    @EventHandler
    public void onWitherDamage(WitherDamageByPlayerEvent event) {
        if (event.getAttacker().getGameTeam() == event.getWitherTeam()) {
            event.setCancelled(true);
        }
    }
}

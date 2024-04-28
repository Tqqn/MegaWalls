package dev.tqqn.kireiwalls.modules.scoreboard;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.scoreboard.PluginScoreboard;

import java.util.HashMap;

public class ScoreboardModule extends AbstractModule {
    private final HashMap<Class<? extends PluginScoreboard>, PluginScoreboard> loadedScoreboards = new HashMap<>();

    public ScoreboardModule(KireiWalls plugin) {
        super(plugin, "Scoreboard");
    }

    public void onEnable() {
    }

    public void onDisable() {
        for (PluginScoreboard boards : this.loadedScoreboards.values()) {
            boards.removeScoreboardFromAllPlayers();
        }
        loadedScoreboards.clear();

    }

    public void setScoreboard(PlayerModel playerModel, PluginScoreboard pluginScoreboard) {
        pluginScoreboard.setScoreboard(playerModel);
    }

    public PluginScoreboard getBoard(Class<? extends PluginScoreboard> board) {
        return this.loadedScoreboards.get(board);
    }
}

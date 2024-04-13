package dev.tqqn.kireiwalls.modules.scoreboard;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.scoreboard.PluginScoreboard;

import java.util.HashMap;

public class ScoreboardModule extends AbstractModule {

    private final HashMap<Class<? extends PluginScoreboard>, PluginScoreboard> loadedScoreboards;

    public ScoreboardModule(KireiWalls plugin) {
        super(plugin, "Scoreboard");
        loadedScoreboards = new HashMap<>();
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        for (PluginScoreboard boards : loadedScoreboards.values()) {
            boards.removeScoreboardFromAllPlayers();
        }
    }

    public void setScoreboard(PlayerModel playerModel, PluginScoreboard pluginScoreboard) {
        pluginScoreboard.setScoreboard(playerModel);
    }

    public PluginScoreboard getBoard(Class<? extends PluginScoreboard> board) {
        return loadedScoreboards.get(board);
    }
}

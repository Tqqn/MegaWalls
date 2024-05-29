package dev.tqqn.kireiwalls.modules.scoreboard;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.AbstractModule;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.scoreboard.framework.PluginScoreboard;

/**
 * The ScoreboardModule class manages scoreboard-related functionality.
 */
public final class ScoreboardModule extends AbstractModule {

    public ScoreboardModule(KireiWalls plugin) {
        super(plugin, "Scoreboard");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    /**
     * Sets a scoreboard for a player.
     *
     * @param playerModel     The player model.
     * @param pluginScoreboard The scoreboard to set.
     */
    public void setScoreboard(PlayerModel playerModel, PluginScoreboard pluginScoreboard) {
        pluginScoreboard.setScoreboard(playerModel);
    }
}

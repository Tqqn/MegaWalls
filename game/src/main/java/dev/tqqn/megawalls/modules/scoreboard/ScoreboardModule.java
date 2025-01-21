package dev.tqqn.megawalls.modules.scoreboard;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.scoreboard.framework.PluginScoreboard;

/**
 * The ScoreboardModule class manages scoreboard-related functionality.
 */
public final class ScoreboardModule extends AbstractModule {

    public ScoreboardModule(MegaWalls plugin) {
        super(plugin, "Scoreboard");
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

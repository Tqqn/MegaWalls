package dev.tqqn.kireiwalls.modules.scoreboard.framework;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The PluginScoreboard class represents a scoreboard for a player in the plugin.
 * It provides methods to update, add lines, set, and remove the scoreboard for a player.
 */
public abstract class PluginScoreboard {

    @Getter
    private final String name;
    @Getter private final PlayerModel playerModel;

    private final List<String> boardLines;

    @Setter
    private String displayName;

    /**
     * Constructs a new PluginScoreboard object with the specified name.
     *
     * @param name        The name of the scoreboard.
     * @param playerModel The PlayerModel associated with the scoreboard.
     */
    public PluginScoreboard(String name, PlayerModel playerModel) {
        this.name = name;
        this.playerModel = playerModel;
        this.boardLines = new ArrayList<>();
    }

    /**
     * Updates the scoreboard.
     * This method must be implemented by subclasses to define how the scoreboard should be updated.
     */
    public abstract void update();

    /**
     * Adds a line to the scoreboard.
     *
     * @param line The line to add to the scoreboard.
     */
    public void addLines(String line) {
        boardLines.add(line);
    }

    /**
     * Sets the scoreboard for the specified player model.
     * If the player model already has a scoreboard, it will be replaced.
     *
     * @param playerModel The PlayerModel for which to set the scoreboard.
     */
    public void setScoreboard(PlayerModel playerModel) {
        if (playerModel.getCurrentScoreboard() != null) {
            removeScoreboard();
        }
        playerModel.setCurrentScoreboard(this);
        KireiWalls.getReflectionLayer().sendSideBarScoreboard(name, playerModel.getPlayer(), displayName, boardLines);
    }

    /**
     * Removes the scoreboard for the associated player model.
     */
    public void removeScoreboard() {
        playerModel.setCurrentScoreboard(null);
        KireiWalls.getReflectionLayer().removePlayerFromScoreboard(name, playerModel.getPlayer());
    }

    /**
     * Removes the scoreboard for all players.
     */
    public void removeScoreboardFromAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerModule.getPlayerModel(player.getUniqueId()).setCurrentScoreboard(null);
            KireiWalls.getReflectionLayer().removePlayerFromScoreboard(name, player);
        }
    }

    /**
     * Updates a specific line of the scoreboard.
     *
     * @param line  The new line content.
     * @param index The index of the line to update.
     */
    public void updateLine(String line, int index) {
        KireiWalls.getReflectionLayer().updateSidebarScoreboardLine(name, playerModel.getPlayer(), line, index);
    }
}

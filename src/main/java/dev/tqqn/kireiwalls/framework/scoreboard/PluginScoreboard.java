package dev.tqqn.kireiwalls.framework.scoreboard;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
     * @param name The name of the scoreboard.
     */
    public PluginScoreboard(String name, PlayerModel playerModel) {
        this.name = name;
        this.playerModel = playerModel;
        this.boardLines = new ArrayList<>();
    }

    public abstract void update();

    /**
     * Adds a String line to the boardLines Collection.
     *
     * @param line The String for the new line.
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

    public void removeScoreboard() {
        playerModel.setCurrentScoreboard(null);
        KireiWalls.getReflectionLayer().removePlayerFromScoreboard(name, playerModel.getPlayer());
    }

    public void removeScoreboardFromAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerModule.getPlayerModel(player.getUniqueId()).setCurrentScoreboard(null);
            KireiWalls.getReflectionLayer().removePlayerFromScoreboard(name, player);
        }
    }

    public void updateLine(String line, int index) {
        KireiWalls.getReflectionLayer().updateSidebarScoreboardLine(name, playerModel.getPlayer(), line, index);
    }
}

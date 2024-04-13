package dev.tqqn.kireiwalls.framework.database.models;

import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.framework.scoreboard.PluginScoreboard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class PlayerModel {

    private final UUID uuid;
    @Setter private String name;
    private final PlayerStats playerStats;

    @Setter private PluginScoreboard currentScoreboard = null;

    @Setter private GameTeam gameTeam;
    @Setter private boolean isAlive;

    @Setter private int coins;

    public PlayerModel(UUID uuid, String name, PlayerStats playerStats) {
        this.uuid = uuid;
        this.name = name;
        this.playerStats = playerStats;
        this.gameTeam = null;
        this.isAlive = false;
        this.coins = 0;
    }

    public PlayerModel(UUID uuid, String name) {
        this(uuid, name, new PlayerStats());
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid) : null;
    }

    public void spawn(Location location) {
        getPlayer().teleport(location);
    }
}

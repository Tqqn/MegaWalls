package dev.tqqn.kireiwalls.framework.database.models;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.framework.game.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.framework.scoreboard.PluginScoreboard;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class PlayerModel {

    private final UUID uuid;
    @Setter private String name;
    private final PlayerStats playerStats;

    @Setter private AbstractClass currentClass;

    @Setter private PluginScoreboard currentScoreboard = null;

    @Setter private GameTeam gameTeam;
    @Setter private boolean isAlive;

    @Setter private int coins;

    private boolean buildMode;
    private boolean spectatorMode;

    public PlayerModel(UUID uuid, String name, PlayerStats playerStats) {
        this.uuid = uuid;
        this.name = name;
        this.playerStats = playerStats;
        this.gameTeam = null;
        this.isAlive = false;
        this.coins = 0;
        this.buildMode = false;
        this.spectatorMode = false;
        this.currentClass = null;
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

    public void setBuildMode(boolean buildMode) {
        this.buildMode = buildMode;

        GameMode gameMode;
        if (buildMode) {
            gameMode = GameMode.CREATIVE;
        } else {
            gameMode = GameMode.SURVIVAL;
        }

        getPlayer().setGameMode(gameMode);
    }

    public void setSpectatorMode(boolean spectatorMode) {
        this.spectatorMode = spectatorMode;

        if (GameModule.getCurrentState().getGameStates() == GameStates.ACTIVE) {
            getPlayer().setCollidable(!spectatorMode);
            if (gameTeam == null) {
                if (spectatorMode) {
                    KireiWalls.getReflectionLayer().sendNameTag(getPlayer(), "spectator", "GRAY", "§7✖", "");
                } else {
                    KireiWalls.getReflectionLayer().sendNameTag(getPlayer(), "normal", "GRAY", "", "");
                }
                return;
            }

            if (spectatorMode) {
                getGameTeam().sendSpectatorTag(this);
            } else {
                getGameTeam().sendNameTag(this);
            }
        }
    }
}

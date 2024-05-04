package dev.tqqn.kireiwalls.framework.database.models;

import com.fastasyncworldedit.bukkit.adapter.DelegateLock;
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
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

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
    @Setter private boolean isProtected;

    private int coins;

    private boolean buildMode;
    private boolean spectatorMode;

    public PlayerModel(UUID uuid, String name, PlayerStats playerStats) {
        this.uuid = uuid;
        this.name = name;
        this.playerStats = playerStats;
        this.gameTeam = null;
        this.isAlive = false;
        this.isProtected = true;
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

    public void increaseCoins(int coins) {
        this.coins += coins;
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

    public void sendSpectatorTag(boolean spectatorMode) {
        if (gameTeam == null) {
            if (spectatorMode) {
                KireiWalls.getReflectionLayer().sendNameTag(getPlayer(), "spectator", "GRAY", "§7✖", "");
            } else {
                KireiWalls.getReflectionLayer().sendNameTag(getPlayer(), "normal", "GRAY", "", "");
            }

        } else {
            if (spectatorMode) {
                getGameTeam().sendSpectatorTag(this);
            } else {
                getGameTeam().sendNameTag(this);
            }
        }
    }

    public void setSpectatorMode(boolean spectatorMode) {
        this.spectatorMode = spectatorMode;

        if (GameModule.getCurrentState().getGameStates() == GameStates.ACTIVE) {

            if (gameTeam != null) {
                gameTeam.removeAlive(this);
            }

            getPlayer().setAllowFlight(spectatorMode);
            getPlayer().setCollidable(!spectatorMode);

            sendSpectatorTag(spectatorMode);

            GameModule gameModule = (GameModule) KireiWalls.getInstance().getModuleManager().getModule(GameModule.class);

            for (PlayerModel playerModel : gameModule.getIngamePlayers()) {
                if (playerModel == this) continue;

                if (spectatorMode) { // if spectator: show all spectators to new spectator, show all spectators to new spectator.
                    if (playerModel.isSpectatorMode()) {
                        getPlayer().showPlayer(KireiWalls.getInstance(), playerModel.getPlayer());
                        playerModel.getPlayer().showPlayer(KireiWalls.getInstance(), getPlayer());
                        return;
                    }

                    playerModel.getPlayer().hidePlayer(KireiWalls.getInstance(), getPlayer());
                } else { // if no spectator: show non spectators the user, hide all spectators from removed spectator
                    playerModel.getPlayer().showPlayer(KireiWalls.getInstance(), getPlayer());
                    if (playerModel.isSpectatorMode()) {
                        getPlayer().hidePlayer(KireiWalls.getInstance(), playerModel.getPlayer());
                    }
                }


            }
        }
    }
}

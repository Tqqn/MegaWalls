package dev.tqqn.kireiwalls.framework.database.models;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.framework.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.framework.game.teams.wither.GameWither;
import dev.tqqn.kireiwalls.framework.scoreboard.PluginScoreboard;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The PlayerModel class represents a decorator for player-related data and functionality.
 * It encapsulates attributes and methods related to player statistics, game state, and behavior.
 */
@Getter
public final class PlayerModel {

    private final UUID uuid;
    @Setter private String name;
    private final PlayerStats playerStats;

    @Setter private AbstractClass currentClass;

    @Setter private PluginScoreboard currentScoreboard = null;

    @Setter private GameTeam gameTeam;
    @Setter private boolean isAlive;
    @Setter private boolean isProtected;
    private int energy;

    private final Map<GameWither, Integer> witherDamageMap;
    private int coins;

    private boolean buildMode;
    private boolean spectatorMode;

    /**
     * Constructs a PlayerModel object with the specified UUID, name, and player statistics.
     *
     * @param uuid        The UUID of the player.
     * @param name        The name of the player.
     * @param playerStats The statistics of the player.
     */
    public PlayerModel(UUID uuid, String name, PlayerStats playerStats) {
        this.uuid = uuid;
        this.name = name;
        this.playerStats = playerStats;
        this.gameTeam = null;
        this.isAlive = false;
        this.isProtected = true;
        this.energy = 0;
        this.witherDamageMap = new HashMap<>();
        this.coins = 0;
        this.buildMode = false;
        this.spectatorMode = false;
        this.currentClass = null;
    }

    /**
     * Constructs a PlayerModel object with the specified UUID and name.
     *
     * @param uuid The UUID of the player.
     * @param name The name of the player.
     */
    public PlayerModel(UUID uuid, String name) {
        this(uuid, name, new PlayerStats());
    }

    /**
     * Retrieves the Bukkit Player object associated with this player model.
     *
     * @return The Bukkit Player object, or null if the player is offline.
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid) : null;
    }

    public void increaseCoins(int coins) {
        this.coins += coins;
    }

    public void addWitherDamage(GameWither gameWither, int damage) {
        if (witherDamageMap.containsKey(gameWither)) {
            witherDamageMap.put(gameWither, witherDamageMap.get(gameWither)+damage);
            return;
        }

        witherDamageMap.put(gameWither, damage);
    }

    public void awardWitherDamage(GameWither gameWither) {
        if (!witherDamageMap.containsKey(gameWither)) return;
        final int damage = witherDamageMap.get(gameWither);

        increaseCoins(damage);
        if (getPlayer() == null) return;

        getPlayer().sendMessage(ChatUtil.format("<green>You have received <gold>" + damage + " coins<green> for damaging the " + gameWither.getGameTeam().getColor() + "[" + gameWither.getGameTeam().getName() + "] Wither<green>!"));
    }

    public void increaseEnergy(int energy) {
        if ((this.energy + energy) > 100) {
            this.energy = 100;
        } else {
            this.energy += energy;
        }
        System.out.println("Increased energy: " + this.energy);
        KireiWalls.getReflectionLayer().sendEnergy(getPlayer(), this.energy, (float) ((double) this.energy / 100));
    }

    public void decreaseEnergy(int energy) {
        this.energy -= energy;
    }

    public void resetEnergy() {
        this.energy = 0;
        KireiWalls.getReflectionLayer().sendEnergy(getPlayer(), this.energy, (float) ((double) this.energy / 100));
    }

    /**
     * Sets the build mode for the player.
     *
     * @param buildMode {@code true} to enable build mode, {@code false} to disable it.
     */
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

    /**
     * Sends the appropriate name tag to the player based on spectator mode.
     *
     * @param spectatorMode {@code true} if the player is in spectator mode, {@code false} otherwise.
     */
    public void sendSpectatorTag(boolean spectatorMode) {

        // If the player is not in a team, send the default name tag for spectator or normal mode
        if (gameTeam == null) {
            if (spectatorMode) {
                KireiWalls.getReflectionLayer().sendNameTag(getPlayer(), "spectator", "GRAY", "§7✖", "");
            } else {
                KireiWalls.getReflectionLayer().sendNameTag(getPlayer(), "normal", "GRAY", "", "");
            }

        } else {
            // If the player is in a team, send the team's name tag for spectator or normal mode
            if (spectatorMode) {
                getGameTeam().sendSpectatorTag(this);
            } else {
                getGameTeam().sendNameTag(this);
            }
        }
    }

    /**
     * Sets the spectator mode for the player.
     *
     * @param spectatorMode {@code true} to enable spectator mode, {@code false} to disable it.
     */
    public void setSpectatorMode(boolean spectatorMode) {
        this.spectatorMode = spectatorMode;

        if (GameModule.getCurrentState().getGameStates() == GameStates.ACTIVE) {

            KireiWalls.getReflectionLayer().sendActionBar(this);

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

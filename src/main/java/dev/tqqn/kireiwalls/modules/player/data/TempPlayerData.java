package dev.tqqn.kireiwalls.modules.player.data;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.classes.framework.AbstractClass;
import dev.tqqn.kireiwalls.modules.classes.framework.Skins;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerStats;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.framework.GameStates;
import dev.tqqn.kireiwalls.modules.scoreboard.framework.PluginScoreboard;
import dev.tqqn.kireiwalls.modules.teams.framework.GameTeam;
import dev.tqqn.kireiwalls.modules.teams.framework.wither.GameWither;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import dev.tqqn.kireiwalls.utils.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class TempPlayerData {

    private final PlayerModel playerModel;

    private final PlayerStats playerStats;
    private final Map<GameWither, Integer> witherDamageMap;

    private AbstractClass currentClass;
    @Setter private PluginScoreboard currentScoreboard;
    @Setter private GameTeam gameTeam;

    @Setter private boolean isAlive;
    @Setter private boolean isProtected;

    private int energy;
    private int coins;

    private boolean buildMode;
    private boolean spectatorMode;

    @Setter private boolean firstChest;

    public TempPlayerData(PlayerModel playerModel, PlayerStats playerStats) {
        this.playerModel = playerModel;
        this.playerStats = playerStats;
        this.witherDamageMap = new HashMap<>();
        this.firstChest = true;
    }

    public void increaseCoins(int coins) { this.coins += coins; }

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
        if (playerModel.getPlayer() == null) return;

        playerModel.getPlayer().sendMessage(ChatUtil.format("<green>You have received <gold>" + damage + " coins<green> for damaging the " + gameWither.getGameTeam().getColor() + "[" + gameWither.getGameTeam().getName() + "] Wither<green>!"));
    }

    public void increaseEnergy(int energy) {
        if ((this.energy + energy) > 100) {
            this.energy = 100;
        } else {
            this.energy += energy;
        }
        System.out.println("Increased energy: " + this.energy);
        KireiWalls.getReflectionLayer().sendEnergy(playerModel.getPlayer(), this.energy, (float) ((double) this.energy / 100));
    }

    public void decreaseEnergy(int energy) {
        this.energy -= energy;
    }

    public void resetEnergy() {
        this.energy = 0;
        KireiWalls.getReflectionLayer().sendEnergy(playerModel.getPlayer(), this.energy, (float) ((double) this.energy / 100));
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

        playerModel.getPlayer().setGameMode(gameMode);
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
                KireiWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), "spectator", "GRAY", "§7✖", "");
            } else {
                KireiWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), "normal", "GRAY", "", "");
            }

        } else {
            // If the player is in a team, send the team's name tag for spectator or normal mode
            if (spectatorMode) {
                getGameTeam().sendSpectatorTag(playerModel);
            } else {
                getGameTeam().sendNameTag(playerModel);
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

        if (playerModel.getPlayer() == null) return;

        final Player player = playerModel.getPlayer();

        if (GameModule.getCurrentState().getGameStates() == GameStates.ACTIVE) {

            if (currentClass != null) KireiWalls.getReflectionLayer().sendActionBar(playerModel);

            if (gameTeam != null) {
                gameTeam.removeAlive(playerModel);
            }

            player.setAllowFlight(spectatorMode);
            player.setCollidable(!spectatorMode);

            sendSpectatorTag(spectatorMode);

            for (PlayerModel playerModel : GameModule.getIngamePlayers()) {
                if (playerModel == this.playerModel) continue;
                if (playerModel.getPlayer() == null) continue;

                if (spectatorMode) { // if spectator: show all spectators to new spectator, show all spectators to new spectator.
                    GameModule.getSpectators().add(playerModel);
                    System.out.println("Called1");

                    if (playerModel.getTempPlayerData().isSpectatorMode()) {
                        System.out.println("Called2");
                        player.showPlayer(KireiWalls.getInstance(), playerModel.getPlayer());
                        playerModel.getPlayer().showPlayer(KireiWalls.getInstance(), player);
                        return;
                    }

                    playerModel.getPlayer().hidePlayer(KireiWalls.getInstance(), player);
                    GameModule.getSpectators().add(playerModel);
                } else { // if no spectator: show non spectators the user, hide all spectators from removed spectator
                    System.out.println("Called3");
                    GameModule.getSpectators().remove(playerModel);

                    playerModel.getPlayer().showPlayer(KireiWalls.getInstance(), player);
                    GameModule.getSpectators().remove(playerModel);
                    if (isSpectatorMode()) {
                        System.out.println("Called4");
                        player.hidePlayer(KireiWalls.getInstance(), playerModel.getPlayer());
                    }
                }
            }
        }
    }

    public void setCurrentClass(AbstractClass abstractClass) {
        if (abstractClass == getCurrentClass()) return;
        this.currentClass = abstractClass;
        if (abstractClass != null) abstractClass.applySkin(playerModel);
        if (abstractClass == null) KireiWalls.getReflectionLayer().changeSkin(Skins.RANDOM, playerModel);
    }

    public Component getChatMessage(Component text) {

        if (GameModule.getCurrentState().getGameStates() == GameStates.WAITING) {
            return ChatUtil.format(MessageUtil.CHAT_LOBBY_FORMAT.getStringMessage(playerModel.getRank(), playerModel.getName())).append(text.color(playerModel.getChatColor()));
        } else {
            String spectator = isSpectatorMode() ? MessageUtil.SPECTATOR_PREFIX.getStringMessage() + " " : "";
            return ChatUtil.format(spectator + MessageUtil.CHAT_TEAM_FORMAT.getStringMessage(getGameTeam().getColor(), getGameTeam().getChatPrefix(), playerModel.getRank(), playerModel.getName())).append(text.color(playerModel.getChatColor()));
        }
    }
}

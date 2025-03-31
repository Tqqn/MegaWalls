package dev.tqqn.megawalls.modules.player.data;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.common.classes.ClassDescriptions;
import dev.tqqn.megawalls.common.classes.ClassSkins;
import dev.tqqn.megawalls.modules.classes.ClassModule;
import dev.tqqn.megawalls.modules.classes.framework.objects.AbstractClass;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerStats;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.scoreboard.framework.PluginScoreboard;
import dev.tqqn.megawalls.modules.teams.TeamModule;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import dev.tqqn.megawalls.modules.teams.framework.wither.GameWither;
import dev.tqqn.megawalls.common.utils.ChatUtil;
import dev.tqqn.megawalls.common.utils.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class TempPlayerData {

    private static final GameModule gameModule = MegaWalls.getInstance().getModuleManager().getModule(GameModule.class);

    private final PlayerModel playerModel;

    private final PlayerStats playerStats;
    private final Map<GameWither, Integer> witherDamageMap;

    private ClassDescriptions.ClassType currentClass;
    @Setter private PluginScoreboard currentScoreboard;
    @Setter private GameTeam gameTeam;

    @Setter private boolean isAlive;
    @Setter private boolean isProtected;

    private int energy;
    private int coins;

    @Setter private boolean buildMode;
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

        final TeamModule.TeamStaticData team = gameWither.getGameTeam().getTeamData();

        playerModel.getPlayer().sendMessage(ChatUtil.format("<green>You have received <gold>" + damage + " coins<green> for damaging the " + team.getColor() + "[" + team.getPrettyName() + "] Wither<green>!"));
    }

    public void increaseEnergy(int energy) {
        if ((this.energy + energy) > 100) {
            this.energy = 100;
        } else {
            this.energy += energy;
        }
        MegaWalls.getReflectionLayer().sendEnergy(playerModel.getPlayer(), this.energy, (float) ((double) this.energy / 100));
    }

    public void decreaseEnergy(int energy) {
        this.energy -= energy;
    }

    public void resetEnergy() {
        this.energy = 0;
        MegaWalls.getReflectionLayer().sendEnergy(playerModel.getPlayer(), this.energy, (float) ((double) this.energy / 100));
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
                MegaWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), "spectator", "GRAY", "§7✖", "");
            } else {
                MegaWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), "normal", "GRAY", "", "");
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

        if (!gameModule.isState(GameStates.ACTIVE)) return;

        if (currentClass != null) MegaWalls.getReflectionLayer().sendActionBar(playerModel);

        if (gameTeam != null) {
            gameTeam.removeAlive(playerModel);
        }

        player.setAllowFlight(spectatorMode);
        player.setCollidable(!spectatorMode);

        sendSpectatorTag(spectatorMode);

        for (PlayerModel playerModel : gameModule.getInGamePlayers()) {
            if (playerModel == this.playerModel) continue;
            if (playerModel.getPlayer() == null) continue;

            if (spectatorMode) { // if spectator: show all spectators to new spectator, show all spectators to new spectator.
                gameModule.addSpectator(playerModel);

                if (playerModel.getTempPlayerData().isSpectatorMode()) {
                    player.showPlayer(MegaWalls.getInstance(), playerModel.getPlayer());
                    playerModel.getPlayer().showPlayer(MegaWalls.getInstance(), player);
                    continue;
                }

                playerModel.getPlayer().hidePlayer(MegaWalls.getInstance(), player);
            } else { // if no spectator: show non spectators the user, hide all spectators from removed spectator
                gameModule.removeSpectator(playerModel);

                playerModel.getPlayer().showPlayer(MegaWalls.getInstance(), player);
                if (isSpectatorMode()) {
                    player.hidePlayer(MegaWalls.getInstance(), playerModel.getPlayer());
                }
            }
        }
    }

    public void setCurrentClass(ClassDescriptions.ClassType classType) {
        if (classType == getCurrentClass()) return;
        this.currentClass = classType;
        if (classType != null) MegaWalls.getInstance().getModuleManager().getModule(ClassModule.class).applySkin(playerModel);
        if (classType == null) MegaWalls.getReflectionLayer().changeSkin(ClassSkins.RANDOM, playerModel);
    }

    public Component getChatMessage(Component text) {
        if (gameModule.isState(GameStates.WAITING)) {
            return ChatUtil.format(MessageUtil.CHAT_LOBBY_FORMAT.getStringMessage(playerModel.getRank(), playerModel.getName())).append(text.color(playerModel.getChatColor()));
        } else {
            String spectator = isSpectatorMode() ? MessageUtil.SPECTATOR_PREFIX.getStringMessage() + " " : "";
            return ChatUtil.format(spectator + MessageUtil.CHAT_TEAM_FORMAT.getStringMessage(getGameTeam().getTeamData().getColor(), getGameTeam().getChatPrefix(), playerModel.getRank(), playerModel.getName())).append(text.color(playerModel.getChatColor()));
        }
    }
}

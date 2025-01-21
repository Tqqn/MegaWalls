package dev.tqqn.megawalls.modules.teams.framework;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerStats;
import dev.tqqn.megawalls.modules.teams.TeamModule;
import dev.tqqn.megawalls.modules.teams.framework.wither.GameWither;
import dev.tqqn.megawalls.modules.region.framework.types.TeamProtectionRegion;
import dev.tqqn.megawalls.modules.region.framework.types.WitherProtectionRegion;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * The GameTeam class represents a team in the game, containing various attributes such as name, players,
 * spawn regions, and the associated wither.
 */
@Getter
public final class GameTeam {

    private final TeamModule.TeamStaticData teamData;
    private GameWither gameWither;
    private final GameTeamSettings gameTeamSettings;
    private final Set<PlayerModel> currentPlayers;
    private final Set<PlayerModel> alivePlayers;
    private int currentFinalKills;
    private final TeamProtectionRegion teamProtectionRegion;
    private final WitherProtectionRegion witherProtectionRegion;
    private final String chatPrefix;

    /**
     * Constructs a GameTeam object with the specified attributes.
     */

    public GameTeam(TeamModule.TeamStaticData teamData, GameTeamSettings gameTeamSettings) {
        this.teamData = teamData;
        this.gameTeamSettings = gameTeamSettings;
        this.currentPlayers = new HashSet<>();
        this.alivePlayers = new HashSet<>();
        this.currentFinalKills = 0;
        this.teamProtectionRegion = new TeamProtectionRegion(teamData, gameTeamSettings.getTeamProtectionCuboid(), this);
        this.witherProtectionRegion = new WitherProtectionRegion(teamData, gameTeamSettings.getWitherProtectionCuboid(), this);
        this.chatPrefix = teamData.getColor() + "[" + teamData.name() + "]";
    }

    /**
     * Spawns the wither associated with the team.
     */
    public void spawnWither() {
        this.gameWither = new GameWither(this);
    }

    /**
     * Adds a player to the team.
     *
     * @param playerModel The player to add.
     */
    public void addPlayer(PlayerModel playerModel) {
        currentPlayers.add(playerModel);
        alivePlayers.add(playerModel);
        playerModel.getTempPlayerData().setGameTeam(this);
        sendNameTag(playerModel);
    }

    public void addAlivePlayer(PlayerModel playerModel) {
        alivePlayers.add(playerModel);
    }

    /**
     * Removes an alive player from the team.
     *
     * @param playerModel The player to remove.
     */
    public void removeAlive(PlayerModel playerModel) {
        alivePlayers.remove(playerModel);
    }

    /**
     * Sends the name tag of a player in the team.
     *
     * @param playerModel The player whose name tag to send.
     */
    public void sendNameTag(PlayerModel playerModel) {
        MegaWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), teamData.getTagName(), teamData.getPrettyName(), teamData.getLegacyColor() + teamData.getPrefix(), " " + playerModel.getTempPlayerData().getCurrentClass().getTag(playerModel));
    }

    /**
     * Sends the spectator tag of a player in the team.
     *
     * @param playerModel The player whose spectator tag to send.
     */
    public void sendSpectatorTag(PlayerModel playerModel) {
        MegaWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), teamData.getTagName(), teamData.getPrettyName(), "§7✖ " + teamData.getLegacyColor() + teamData.getPrefix(), "");
    }

    /**
     * Removes a player from the team.
     *
     * @param playerModel The player to remove.
     */
    public void removePlayer(PlayerModel playerModel) {
        currentPlayers.remove(playerModel);
        playerModel.getTempPlayerData().setGameTeam(null);
    }

    public void increaseCurrentFinalKills() {
        this.currentFinalKills++;
    }

    public void decreaseFinalKills(PlayerModel playerModel) {
        this.currentFinalKills = currentFinalKills - playerModel.getTempPlayerData().getPlayerStats().getStat(PlayerStats.StatType.FINAL_KILLS);
    }
}

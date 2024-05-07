package dev.tqqn.kireiwalls.framework.game.teams;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.teams.wither.GameWither;
import dev.tqqn.kireiwalls.framework.region.Cuboid;
import dev.tqqn.kireiwalls.framework.region.types.TeamProtectionRegion;
import dev.tqqn.kireiwalls.framework.region.types.WitherProtectionRegion;
import dev.tqqn.kireiwalls.nms.ReflectionLayer;
import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.NamedTextColor;
import org.antlr.v4.runtime.tree.xpath.XPathLexerErrorListener;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The GameTeam class represents a team in the game, containing various attributes such as name, players,
 * spawn regions, and the associated wither.
 */
@Getter
public final class GameTeam {

    private final String name;
    private final String prettyName;
    private final String tagName;
    private final String prefix;
    private final String color;
    private final String legacyColor;
    private final NamedTextColor namedTextColor;
    private GameWither gameWither;
    private final GameTeamSettings gameTeamSettings;
    private final Set<PlayerModel> currentPlayers;
    private final Set<PlayerModel> alivePlayers;

    /**
     * Constructs a GameTeam object with the specified attributes.
     *
     * @param name The name of the team.
     * @param prettyName The pretty name of the team.
     * @param tagName The tag name of the team.
     * @param prefix The prefix of the team.
     * @param color The color of the team.
     * @param legacyColor The legacy color of the team.
     * @param adventureColor The named text color of the team.
     * @param spawnRegion The spawn region of the team.
     * @param witherRegion The wither region of the team.
     * @param spawnLocation The spawn location of the team.
     * @param witherLocation The wither location of the team.
     */
    public GameTeam(String name, String prettyName, String tagName, String prefix, String color, String legacyColor, NamedTextColor adventureColor, Cuboid spawnRegion, Cuboid witherRegion, Location spawnLocation, Location witherLocation) {
        this.name = name;
        this.prettyName = prettyName;
        this.tagName = tagName;
        this.prefix = "[" + prefix + "]";
        this.color = color;
        this.legacyColor = legacyColor;
        this.namedTextColor = adventureColor;

        TeamProtectionRegion teamProtectionRegion = new TeamProtectionRegion(name, spawnRegion, this);
        WitherProtectionRegion witherProtectionRegion = new WitherProtectionRegion(name, witherRegion, this);

        this.gameTeamSettings = new GameTeamSettings(teamProtectionRegion, witherProtectionRegion, spawnLocation, witherLocation);
        this.currentPlayers = new HashSet<>();
        this.alivePlayers = new HashSet<>();
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
        playerModel.setGameTeam(this);
        sendNameTag(playerModel);
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
        KireiWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), tagName, name, legacyColor + prefix, " " + playerModel.getCurrentClass().getTag(playerModel));
    }

    /**
     * Sends the spectator tag of a player in the team.
     *
     * @param playerModel The player whose spectator tag to send.
     */
    public void sendSpectatorTag(PlayerModel playerModel) {
        KireiWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), tagName, name, "§7✖ " + legacyColor + prefix, "");
    }

    /**
     * Removes a player from the team.
     *
     * @param playerModel The player to remove.
     */
    public void removePlayer(PlayerModel playerModel) {
        currentPlayers.remove(playerModel);
        playerModel.setGameTeam(null);
    }
}

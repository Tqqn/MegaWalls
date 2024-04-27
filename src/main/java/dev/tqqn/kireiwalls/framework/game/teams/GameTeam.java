package dev.tqqn.kireiwalls.framework.game.teams;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.teams.wither.GameWither;
import dev.tqqn.kireiwalls.framework.region.types.TeamProtectionRegion;
import dev.tqqn.kireiwalls.nms.ReflectionLayer;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

@Getter
public class GameTeam {

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

    public GameTeam(String name, String prettyName, String tagName, String prefix, String color, String legacyColor, NamedTextColor adventureColor, TeamProtectionRegion teamProtectionRegion, Location spawnLocation, Location witherLocation) {
        this.name = name;
        this.prettyName = prettyName;
        this.tagName = tagName;
        this.prefix = "[" + prefix + "]";
        this.color = color;
        this.legacyColor = legacyColor;
        this.namedTextColor = adventureColor;

        this.gameTeamSettings = new GameTeamSettings(teamProtectionRegion, spawnLocation, witherLocation);
        this.currentPlayers = new HashSet<>();
    }

    public void spawnWither() {
        this.gameWither = new GameWither(this);
    }

    public void addPlayer(PlayerModel playerModel) {
        currentPlayers.add(playerModel);
        playerModel.setGameTeam(this);
        sendNameTag(playerModel);
    }

    public void sendNameTag(PlayerModel playerModel) {
        System.out.println("Called name tag sending");
        KireiWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), tagName, name, legacyColor + prefix, "");
    }

    public void sendSpectatorTag(PlayerModel playerModel) {
        System.out.println("Called Spectator tag");
        KireiWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), tagName, name, "§7✖ " + legacyColor + prefix, "");
    }

    public void removePlayer(PlayerModel playerModel) {
        currentPlayers.remove(playerModel);
        playerModel.setGameTeam(null);
    }
}

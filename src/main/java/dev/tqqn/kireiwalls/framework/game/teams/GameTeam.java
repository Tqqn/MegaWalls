package dev.tqqn.kireiwalls.framework.game.teams;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.region.types.TeamProtectionRegion;
import lombok.Getter;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

@Getter
public class GameTeam {

    private final String name;
    private final String prettyName;
    private final String prefix;
    private final String color;
    private final GameTeamSettings gameTeamSettings;
    private final Set<PlayerModel> currentPlayers;

    public GameTeam(String name, String prettyName, String prefix, String color, TeamProtectionRegion teamProtectionRegion, Location spawnLocation, Location witherLocation) {
        this.name = name;
        this.prettyName = prettyName;
        this.prefix = "[" + prefix + "]";
        this.color = color;
        this.gameTeamSettings = new GameTeamSettings(teamProtectionRegion, spawnLocation, witherLocation);
        this.currentPlayers = new HashSet<>();
    }

    public void addPlayer(PlayerModel playerModel) {
        currentPlayers.add(playerModel);
        playerModel.setGameTeam(this);
    }

    public void removePlayer(PlayerModel playerModel) {
        currentPlayers.remove(playerModel);
        playerModel.setGameTeam(null);
    }
}

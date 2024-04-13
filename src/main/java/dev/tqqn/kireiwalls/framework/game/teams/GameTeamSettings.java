package dev.tqqn.kireiwalls.framework.game.teams;

import dev.tqqn.kireiwalls.framework.region.types.TeamProtectionRegion;
import lombok.Getter;
import org.bukkit.Location;

@Getter
public class GameTeamSettings {

    private final TeamProtectionRegion teamProtectionRegion;
    private final Location spawnLocation;
    private final Location witherLocation;

    public GameTeamSettings(TeamProtectionRegion teamProtectionRegion, Location spawnLocation, Location witherLocation) {
        this.teamProtectionRegion = teamProtectionRegion;
        this.spawnLocation = spawnLocation;
        this.witherLocation = witherLocation;
    }
}

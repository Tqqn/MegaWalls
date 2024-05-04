package dev.tqqn.kireiwalls.framework.game.teams;

import dev.tqqn.kireiwalls.framework.region.types.TeamProtectionRegion;
import dev.tqqn.kireiwalls.framework.region.types.WitherProtectionRegion;
import lombok.Getter;
import org.bukkit.Location;

@Getter
public class GameTeamSettings {

    private final TeamProtectionRegion teamProtectionRegion;
    private final WitherProtectionRegion witherProtectionRegion;
    private final Location spawnLocation;
    private final Location witherLocation;

    public GameTeamSettings(TeamProtectionRegion teamProtectionRegion, WitherProtectionRegion witherProtectionRegion, Location spawnLocation, Location witherLocation) {
        this.teamProtectionRegion = teamProtectionRegion;
        this.witherProtectionRegion = witherProtectionRegion;
        this.spawnLocation = spawnLocation;
        this.witherLocation = witherLocation;
    }
}

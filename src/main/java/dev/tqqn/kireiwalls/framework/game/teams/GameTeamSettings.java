package dev.tqqn.kireiwalls.framework.game.teams;

import dev.tqqn.kireiwalls.framework.region.types.TeamProtectionRegion;
import dev.tqqn.kireiwalls.framework.region.types.WitherProtectionRegion;
import lombok.Getter;
import org.bukkit.Location;

/**
 * The GameTeamSettings class represents the settings of a game team, including protection regions,
 * spawn location, and wither location.
 */
@Getter
public final class GameTeamSettings {

    private final TeamProtectionRegion teamProtectionRegion;
    private final WitherProtectionRegion witherProtectionRegion;
    private final Location spawnLocation;
    private final Location witherLocation;

    /**
     * Constructs a GameTeamSettings object with the specified attributes.
     *
     * @param teamProtectionRegion The team protection region.
     * @param witherProtectionRegion The wither protection region.
     * @param spawnLocation The spawn location of the team.
     * @param witherLocation The wither location of the team.
     */
    public GameTeamSettings(TeamProtectionRegion teamProtectionRegion, WitherProtectionRegion witherProtectionRegion, Location spawnLocation, Location witherLocation) {
        this.teamProtectionRegion = teamProtectionRegion;
        this.witherProtectionRegion = witherProtectionRegion;
        this.spawnLocation = spawnLocation;
        this.witherLocation = witherLocation;
    }
}

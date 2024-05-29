package dev.tqqn.kireiwalls.modules.teams.framework;

import dev.tqqn.kireiwalls.modules.region.framework.Cuboid;
import dev.tqqn.kireiwalls.modules.database.drivers.mongo.MongoItem;
import dev.tqqn.kireiwalls.modules.database.drivers.mongo.MongoObject;
import lombok.Getter;
import org.bukkit.Location;

/**
 * The GameTeamSettings class represents the settings of a game team, including protection regions,
 * spawn location, and wither location.
 */
@Getter
@MongoItem("gamesettings")
public final class GameTeamSettings extends MongoObject<String> {

    private final Cuboid teamProtectionCuboid;
    private final Cuboid witherProtectionCuboid;
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
    public GameTeamSettings(String mapName, String teamName, Cuboid teamProtectionRegion, Cuboid witherProtectionRegion, Location spawnLocation, Location witherLocation) {
        super(mapName + "_" + teamName);
        this.teamProtectionCuboid = teamProtectionRegion;
        this.witherProtectionCuboid = witherProtectionRegion;
        this.spawnLocation = spawnLocation;
        this.witherLocation = witherLocation;
    }
}

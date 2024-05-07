package dev.tqqn.kireiwalls.modules.game;

import dev.tqqn.kireiwalls.framework.region.Cuboid;
import dev.tqqn.kireiwalls.framework.region.types.MiddleProtectionRegion;
import lombok.Getter;
import org.bukkit.Location;

/**
 * The GameSettings class holds settings for the game.
 */
@Getter
public final class GameSettings {
    private final String mapName;
    private final Location lobbyLocation;
    private final int lobbyCount;

    private final MiddleProtectionRegion middleProtectionRegion;

    public GameSettings(String mapName, Location lobbyLocation, int lobbyCount, Cuboid middle) {
        this.mapName = mapName;
        this.lobbyLocation = lobbyLocation;
        this.lobbyCount = lobbyCount;
        this.middleProtectionRegion = new MiddleProtectionRegion("Middle", middle);
    }
}

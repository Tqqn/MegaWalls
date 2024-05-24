package dev.tqqn.kireiwalls.framework.arena;

import dev.tqqn.kireiwalls.framework.region.Cuboid;
import dev.tqqn.kireiwalls.framework.region.types.MiddleProtectionRegion;
import lombok.Getter;
import org.bukkit.Location;

@Getter
public final class ArenaSettings {

    private final Location lobbyLocation;
    private final Cuboid[] middleCuboids;
    private final Cuboid middleCuboid;
    private transient MiddleProtectionRegion middleProtectionRegion;

    public ArenaSettings(Location lobbyLocation, Cuboid[] middleCuboids, Cuboid middle) {
        this.lobbyLocation = lobbyLocation;
        this.middleCuboids = middleCuboids;
        this.middleCuboid = middle;
    }

    public void initRegion() {
        this.middleProtectionRegion = new MiddleProtectionRegion("Middle", middleCuboid);
    }
}

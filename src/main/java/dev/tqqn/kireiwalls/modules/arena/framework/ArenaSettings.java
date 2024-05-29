package dev.tqqn.kireiwalls.modules.arena.framework;

import dev.tqqn.kireiwalls.modules.region.framework.Cuboid;
import dev.tqqn.kireiwalls.modules.region.framework.types.MiddleProtectionRegion;
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

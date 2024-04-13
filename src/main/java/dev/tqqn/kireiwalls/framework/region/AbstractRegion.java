package dev.tqqn.kireiwalls.framework.region;

import dev.tqqn.kireiwalls.framework.region.types.RegionType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractRegion {

    private static final List<AbstractRegion> CACHE = new ArrayList<>();

    private final Cuboid cuboid;
    private final String name;
    private final RegionType regionType;

    private final List<Player> playersInRegion;

    public AbstractRegion(String name, Cuboid cuboid, RegionType regionType) {
        this.name = name;
        this.cuboid = cuboid;
        this.regionType = regionType;
        this.playersInRegion = new ArrayList<>();
        CACHE.add(this);
    }

    public void entry(Player player) {
        onEntry(player);
        playersInRegion.add(player);
    }

    public void exit(Player player) {
        onExit(player);
        playersInRegion.remove(player);
    }

    public static List<AbstractRegion> getRegions() {
        return CACHE;
    }

    protected abstract void onEntry(Player player);
    protected abstract void onExit(Player player);
}

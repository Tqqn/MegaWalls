package dev.tqqn.kireiwalls.framework.region;

import dev.tqqn.kireiwalls.framework.region.types.RegionType;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public abstract class AbstractRegion {

    private static final Set<AbstractRegion> CACHE = new HashSet<>();

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

    public static Set<AbstractRegion> getRegions() {
        return CACHE;
    }

    public void onEnter(PlayerMoveEvent event) {
    }

    public void onBlockBreak(BlockBreakEvent event) {
        // Empty for Override
    }

    public void onBlockPlace(BlockPlaceEvent event) {
        // Empty for Override
    }

    public void onEntry(Player player) {
        // Empty for Override
    }

    public void onExit(Player player) {
        // Empty for Override
    }

    public void onDamage(EntityDamageEvent event) {
        // Empty for Override
    }

    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        // Empty for Override
    }
}

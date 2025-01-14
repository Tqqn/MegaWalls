package dev.tqqn.megawalls.modules.region.framework;

import dev.tqqn.megawalls.modules.region.framework.types.RegionType;
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

/**
 * The AbstractRegion class represents an abstract region with defined boundaries and associated actions.
 * It serves as the base class for specific types of regions in the game world.
 */
@Getter
public abstract class AbstractRegion {

    private static final Set<AbstractRegion> CACHE = new HashSet<>();

    private final Cuboid cuboid;
    private final String name;
    private final RegionType regionType;

    private final List<Player> playersInRegion;

    /**
     * Constructs an AbstractRegion object with the given name, cuboid boundaries, and region type.
     *
     * @param name       The name of the region.
     * @param cuboid     The cuboid defining the boundaries of the region.
     * @param regionType The type of the region.
     */
    public AbstractRegion(String name, Cuboid cuboid, RegionType regionType) {
        this.name = name;
        this.cuboid = cuboid;
        this.regionType = regionType;
        this.playersInRegion = new ArrayList<>();
        CACHE.add(this);
    }

    /**
     * Handles player entry into the region.
     * Invokes the onEntry method and adds the player to the list of players in the region.
     *
     * @param player The player entering the region.
     */
    public void entry(Player player) {
        onEntry(player);
        playersInRegion.add(player);
    }

    /**
     * Handles player exit from the region.
     * Invokes the onExit method and removes the player from the list of players in the region.
     *
     * @param player The player exiting the region.
     */
    public void exit(Player player) {
        onExit(player);
        playersInRegion.remove(player);
    }

    /**
     * Retrieves all registered regions.
     *
     * @return A set of all registered regions.
     */
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

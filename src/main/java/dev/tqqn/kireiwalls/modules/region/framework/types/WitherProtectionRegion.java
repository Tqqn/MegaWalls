package dev.tqqn.kireiwalls.modules.region.framework.types;

import dev.tqqn.kireiwalls.modules.teams.framework.GameTeam;
import dev.tqqn.kireiwalls.modules.teams.framework.wither.GameWither;
import dev.tqqn.kireiwalls.modules.region.framework.AbstractRegion;
import dev.tqqn.kireiwalls.modules.region.framework.Cuboid;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * The WitherProtectionRegion class represents a protection region around a team's wither.
 * It restricts certain actions within the region to ensure fair gameplay.
 */
public final class WitherProtectionRegion extends AbstractRegion {

    private final GameTeam gameTeam;

    /**
     * Constructs a WitherProtectionRegion object with the given name, cuboid boundaries, and associated game team.
     *
     * @param name     The name of the region.
     * @param cuboid   The cuboid defining the boundaries of the region.
     * @param gameTeam The game team associated with this region.
     */
    public WitherProtectionRegion(String name, Cuboid cuboid, GameTeam gameTeam) {
        super(name, cuboid, RegionType.WITHER);
        this.gameTeam = gameTeam;
    }

    /**
     * Handles player entry events into the wither protection region.
     * Adds the player to the wither's boss bar if the wither is active.
     *
     * @param player The player entering the region.
     */
    @Override
    public void onEntry(Player player) {
        if (gameTeam.getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH) return;

        gameTeam.getGameWither().getWitherBar().addPlayer(player);
    }

    /**
     * Handles player exit events from the wither protection region.
     * Removes the player from the wither's boss bar if the wither is active.
     *
     * @param player The player leaving the region.
     */
    @Override
    public void onExit(Player player) {
        if (gameTeam.getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH) return;
        gameTeam.getGameWither().getWitherBar().removePlayer(player);
    }

    /**
     * Handles block break events within the wither protection region.
     * Cancels block breaking if the wither status is not DEATH.
     *
     * @param event The BlockBreakEvent.
     */
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (gameTeam.getGameWither().getWitherStatus() != GameWither.WitherStatus.DEATH) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles block place events within the wither protection region.
     * Cancels block placing if the wither status is not DEATH.
     *
     * @param event The BlockPlaceEvent.
     */
    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (gameTeam.getGameWither().getWitherStatus() != GameWither.WitherStatus.DEATH) {
            event.setCancelled(true);
        }
    }
}

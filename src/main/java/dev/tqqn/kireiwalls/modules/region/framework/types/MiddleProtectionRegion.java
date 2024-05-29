package dev.tqqn.kireiwalls.modules.region.framework.types;

import dev.tqqn.kireiwalls.modules.game.framework.GameStates;
import dev.tqqn.kireiwalls.modules.region.framework.AbstractRegion;
import dev.tqqn.kireiwalls.modules.region.framework.Cuboid;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.ActiveState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * The MiddleProtectionRegion class represents a protection region in the middle of the game map.
 * It prevents certain actions from being performed during specific game states.
 */
public final class MiddleProtectionRegion extends AbstractRegion {

    /**
     * Constructs a MiddleProtectionRegion object with the given name and cuboid boundaries.
     *
     * @param name   The name of the region.
     * @param cuboid The cuboid defining the boundaries of the region.
     */
    public MiddleProtectionRegion(String name, Cuboid cuboid) {
        super(name, cuboid, RegionType.MIDDLE);
    }

    /**
     * Handles player entry events into the middle protection region.
     * Prevents certain actions during the ACTIVE game state and PREPARE cycle.
     *
     * @param event The PlayerMoveEvent.
     */
    public void onEnter(PlayerMoveEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;
        if (ActiveState.getCurrentCycle() == ActiveState.Cycle.PREPARE) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles block break events within the middle protection region.
     * Prevents block breaking during the ACTIVE game state and PREPARE cycle.
     *
     * @param event The BlockBreakEvent.
     */
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;
        if (ActiveState.getCurrentCycle() == ActiveState.Cycle.PREPARE) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles block place events within the middle protection region.
     * Prevents block placing during the ACTIVE game state and PREPARE cycle.
     *
     * @param event The BlockPlaceEvent.
     */
    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;
        if (ActiveState.getCurrentCycle() == ActiveState.Cycle.PREPARE) {
            event.setCancelled(true);
        }
    }
}

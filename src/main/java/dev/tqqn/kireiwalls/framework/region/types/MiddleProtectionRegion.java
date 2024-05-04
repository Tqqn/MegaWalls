package dev.tqqn.kireiwalls.framework.region.types;

import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.framework.region.AbstractRegion;
import dev.tqqn.kireiwalls.framework.region.Cuboid;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.ActiveState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MiddleProtectionRegion extends AbstractRegion {

    public MiddleProtectionRegion(String name, Cuboid cuboid) {
        super(name, cuboid, RegionType.MIDDLE);
    }

    public void onEnter(PlayerMoveEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;
        if (ActiveState.getCurrentCycle() == ActiveState.Cycle.PREPARE) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;
        if (ActiveState.getCurrentCycle() == ActiveState.Cycle.PREPARE) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;
        if (ActiveState.getCurrentCycle() == ActiveState.Cycle.PREPARE) {
            event.setCancelled(true);
        }
    }
}

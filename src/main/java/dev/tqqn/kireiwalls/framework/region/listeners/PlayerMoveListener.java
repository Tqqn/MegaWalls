package dev.tqqn.kireiwalls.framework.region.listeners;

import dev.tqqn.kireiwalls.framework.region.AbstractRegion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
            event.getFrom().getBlockY() == event.getTo().getBlockY() &&
            event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        for (AbstractRegion abstractRegion : AbstractRegion.getRegions()) {
            if (abstractRegion.getCuboid().isIn(event.getFrom()) && !abstractRegion.getCuboid().isIn(event.getTo())) {
                abstractRegion.exit(event.getPlayer());
                return;
            }
            if (abstractRegion.getCuboid().isIn(event.getTo()) && !abstractRegion.getCuboid().isIn(event.getFrom())) {
                abstractRegion.entry(event.getPlayer());
                return;
            }
        }
    }
}

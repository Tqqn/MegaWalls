package dev.tqqn.kireiwalls.framework.region.listeners;

import dev.tqqn.kireiwalls.framework.region.AbstractRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class RegionListener implements Listener {

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

    @EventHandler
    public void onEntry(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        for (AbstractRegion abstractRegion : AbstractRegion.getRegions()) {
            if (abstractRegion.getCuboid().isIn(event.getTo()) && !abstractRegion.getCuboid().isIn(event.getFrom())) {
                abstractRegion.onEnter(event);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        for (AbstractRegion abstractRegion : AbstractRegion.getRegions()) {
            if (abstractRegion.getCuboid().isIn(event.getBlock().getLocation())) {
                abstractRegion.onBlockBreak(event);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        for (AbstractRegion abstractRegion : AbstractRegion.getRegions()) {
            if (abstractRegion.getCuboid().isIn(event.getBlock().getLocation())) {
                abstractRegion.onBlockPlace(event);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (AbstractRegion abstractRegion : AbstractRegion.getRegions()) {
                if (abstractRegion.getCuboid().isIn(player.getLocation())) {
                    abstractRegion.onDamage(event);
                }
            }
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (AbstractRegion abstractRegion : AbstractRegion.getRegions()) {
                if (abstractRegion.getCuboid().isIn(player.getLocation())) {
                    abstractRegion.onDamageByEntity(event);
                }
            }
        }
    }
}

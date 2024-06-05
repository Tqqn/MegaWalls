package dev.tqqn.kireiwalls.modules.region.framework.listeners;

import dev.tqqn.kireiwalls.modules.region.framework.AbstractRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * The RegionListener class listens for events related to player movement, block interactions,
 * and entity damage within defined regions.
 */
public final class RegionListener implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

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

    /**
     * Handles player movement events.
     *
     * @param event The PlayerMoveEvent.
     */
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

    /**
     * Handles player entry events.
     *
     * @param event The PlayerMoveEvent.
     */
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

    /**
     * Handles block break events.
     *
     * @param event The BlockBreakEvent.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        for (AbstractRegion abstractRegion : AbstractRegion.getRegions()) {
            if (abstractRegion.getCuboid().isIn(event.getBlock().getLocation())) {
                abstractRegion.onBlockBreak(event);
            }
        }
    }

    /**
     * Handles block place events.
     *
     * @param event The BlockPlaceEvent.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        for (AbstractRegion abstractRegion : AbstractRegion.getRegions()) {
            if (abstractRegion.getCuboid().isIn(event.getBlock().getLocation())) {
                abstractRegion.onBlockPlace(event);
            }
        }
    }

    /**
     * Handles entity damage events.
     *
     * @param event The EntityDamageEvent.
     */
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

    /**
     * Handles entity damage by entity events.
     *
     * @param event The EntityDamageByEntityEvent.
     */
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

package dev.tqqn.kireiwalls.modules.chest.framework.listeners;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.chest.framework.events.GatheringChestSpawnEvent;
import dev.tqqn.kireiwalls.modules.game.framework.GameStates;
import dev.tqqn.kireiwalls.modules.chest.ChestModule;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.ActiveState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ChestListeners implements Listener {

    private final ChestModule chestModule;
    private final Set<Location> placedBlocks;

    public ChestListeners() {
        chestModule = KireiWalls.getInstance().getModuleManager().getModule(ChestModule.class);
        placedBlocks = new HashSet<>();
    }

    @EventHandler
    public void onChestSpawn(GatheringChestSpawnEvent event) {
        final Location location = event.getGatheringChest().getChestLocation();
        location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, location, 5);
        location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0.5f, 1);
        location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 0.5f, 1);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        final Material blockType = event.getBlock().getType();
        if (blockType == Material.STONE || blockType == Material.DIRT || blockType == Material.GRASS_BLOCK || blockType.toString().contains("_ORE") || blockType.toString().contains("_LOG")) {
            placedBlocks.add(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;
        if (ActiveState.getCurrentCycle() == ActiveState.Cycle.END) return;
        if (isPlacedByPlayer(event.getBlock().getLocation())) {
            placedBlocks.remove(event.getBlock().getLocation());
            return;
        }

        final Material blockType = event.getBlock().getType();

        if (blockType.toString().contains("_LOG") || blockType == Material.STONE || blockType == Material.DIRT || blockType == Material.GRASS_BLOCK || blockType.toString().contains("_ORE")) {
            if (blockType == Material.DIAMOND_ORE) return;

            int chestChance = chestModule.getDefaultChestSpawnRate();

            if (ActiveState.getCurrentCycle() == ActiveState.Cycle.PRE_DM || ActiveState.getCurrentCycle() == ActiveState.Cycle.COUNTDOWN_TO_DM || ActiveState.getCurrentCycle() == ActiveState.Cycle.DM) chestChance = chestChance / 3;

            if (ThreadLocalRandom.current().nextInt(0, 100) < chestChance) {
                event.setCancelled(true);
                chestModule.spawnChest(blockType, event.getBlock().getLocation(), event.getPlayer().getUniqueId());
            }
        }

        if (blockType == Material.TRAPPED_CHEST || blockType == Material.CHEST) {
            chestModule.removeChest(event.getBlock().getLocation());
        }

    }

    @EventHandler
    public void onChestInteract(PlayerInteractEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() == null) return;
            if (ActiveState.getCurrentCycle() != ActiveState.Cycle.PREPARE) return;

            final Block block = event.getClickedBlock();

            if (block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.CHEST) {
                if (block.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR) return;

                Chest chest = (Chest) block.getState();
                event.getPlayer().openInventory(chest.getBlockInventory());
            }
        }
    }

    private boolean isPlacedByPlayer(Location location) {
        return placedBlocks.contains(location);
    }
}

package dev.tqqn.megawalls.modules.chest.framework.listeners;

import dev.tqqn.megawalls.modules.chest.framework.events.GatheringChestSpawnEvent;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.chest.ChestModule;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.states.active.ActiveState;
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

    private static final Set<Material> allowedChestBlocks = Set.of(Material.STONE, Material.DIRT, Material.GRASS_BLOCK);

    private final ChestModule chestModule;
    private final GameModule gameModule;
    private final Set<Location> placedBlocks;

    public ChestListeners(ChestModule chestModule, GameModule gameModule) {
        this.chestModule = chestModule;
        this.gameModule = gameModule;
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
        if (allowedChestBlocks.contains(blockType) || blockType.toString().contains("_ORE") || blockType.toString().contains("_LOG")) {
            placedBlocks.add(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (!gameModule.isState(GameStates.ACTIVE)) return;
        if (!(gameModule.getCurrentState() instanceof ActiveState activeState)) return;
        if (activeState.getCurrentCycle() == ActiveState.Cycle.END) return;
        if (isPlacedByPlayer(event.getBlock().getLocation())) {
            placedBlocks.remove(event.getBlock().getLocation());
            return;
        }

        final Material blockType = event.getBlock().getType();

        if (allowedChestBlocks.contains(blockType) || blockType.toString().contains("_ORE") || blockType.toString().contains("_LOG")) {
            if (blockType == Material.DIAMOND_ORE) return;

            int chestChance = chestModule.getDefaultChestSpawnRate();

            if (activeState.getCurrentCycle() == ActiveState.Cycle.PRE_DM || activeState.getCurrentCycle() == ActiveState.Cycle.COUNTDOWN_TO_DM || activeState.getCurrentCycle() == ActiveState.Cycle.DM) chestChance = chestChance / 3;

            if (ThreadLocalRandom.current().nextInt(0, 100) > chestChance) return;

            event.setCancelled(true);
            chestModule.spawnChest(blockType, event.getBlock().getLocation(), event.getPlayer().getUniqueId());
        }

        if (blockType == Material.TRAPPED_CHEST || blockType == Material.CHEST) {
            chestModule.removeChest(event.getBlock().getLocation());
        }

    }

    @EventHandler
    public void onChestInteract(PlayerInteractEvent event) {
        if (!gameModule.isState(GameStates.ACTIVE)) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() == null) return;
            if (!(gameModule.getCurrentState() instanceof ActiveState activeState)) return;
            if (activeState.getCurrentCycle() != ActiveState.Cycle.PREPARE) return;

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

package dev.tqqn.megawalls.modules.player.listeners;

import dev.tqqn.megawalls.modules.database.framework.events.GamePlayerJoinEvent;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.game.framework.events.WitherDamageByPlayerEvent;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * The SpectatorListeners class defines event handlers for spectator-related events.
 */
public final class SpectatorListeners implements Listener {

    /**
     * Handles the player join event.
     *
     * @param event The GamePlayerJoinEvent instance.
     */
    @EventHandler
    public void onPlayerJoin(GamePlayerJoinEvent event) {
        if (GameModule.getCurrentState().getGameStates() == GameStates.ACTIVE) {
            if (event.getPlayerModel().getTempPlayerData().isSpectatorMode()) {
                event.getPlayerModel().getTempPlayerData().setSpectatorMode(true);
            }
        } else {
            event.getPlayerModel().getTempPlayerData().setSpectatorMode(false);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (PlayerModule.getPlayerModel(event.getPlayer().getUniqueId()).getTempPlayerData().isSpectatorMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        if (PlayerModule.getPlayerModel(event.getPlayer().getUniqueId()).getTempPlayerData().isSpectatorMode()) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the wither attack event.
     *
     * @param event The WitherDamageByPlayerEvent instance.
     */
    @EventHandler
    public void onWitherAttack(WitherDamageByPlayerEvent event) {
        if (event.getAttacker().getTempPlayerData().isSpectatorMode()) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the player interact event.
     *
     * @param event The PlayerInteractEvent instance.
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (PlayerModule.getPlayerModel(event.getPlayer().getUniqueId()).getTempPlayerData().isSpectatorMode()) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the block break event.
     *
     * @param event The BlockBreakEvent instance.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (PlayerModule.getPlayerModel(event.getPlayer().getUniqueId()).getTempPlayerData().isSpectatorMode()) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the block place event.
     *
     * @param event The BlockPlaceEvent instance.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (PlayerModule.getPlayerModel(event.getPlayer().getUniqueId()).getTempPlayerData().isSpectatorMode()) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the block damage event.
     *
     * @param event The BlockDamageEvent instance.
     */
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        if (PlayerModule.getPlayerModel(event.getPlayer().getUniqueId()).getTempPlayerData().isSpectatorMode()) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the entity damage event.
     *
     * @param event The EntityDamageEvent instance.
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (PlayerModule.getPlayerModel(event.getEntity().getUniqueId()).getTempPlayerData().isSpectatorMode()) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the entity damage by entity event.
     *
     * @param event The EntityDamageByEntityEvent instance.
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity target = event.getDamager();
        if (target instanceof Player player) {
            if (PlayerModule.getPlayerModel(player.getUniqueId()).getTempPlayerData().isSpectatorMode()) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Handles the entity target event.
     *
     * @param event The EntityTargetEvent instance.
     */
    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        Entity target = event.getTarget();
        if (target instanceof Player player) {
            if (PlayerModule.getPlayerModel(player.getUniqueId()).getTempPlayerData().isSpectatorMode()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHungerLose(FoodLevelChangeEvent event) {
        Entity target = event.getEntity();
        if (target instanceof Player player) {
            if (PlayerModule.getPlayerModel(player.getUniqueId()).getTempPlayerData().isSpectatorMode()) {
                event.setCancelled(true);
            }
        }
    }
}

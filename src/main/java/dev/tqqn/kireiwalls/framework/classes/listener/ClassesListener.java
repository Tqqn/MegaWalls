package dev.tqqn.kireiwalls.framework.classes.listener;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 * The ClassesListener class implements event listeners for class-related actions in the game.
 * It handles events such as player hits, interactions, and block placements specific to player classes.
 */
public final class ClassesListener implements Listener {

    /**
     * Handles the Energy gain of the players.
     *
     * @param event The EntityDamageByEntityEvent representing the hit event.
     */
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player player)) return;
        PlayerModel playerModel = PlayerModule.getPlayerModel(player.getUniqueId());

        if (event.getDamager() instanceof Player damager) {
            PlayerModel damageModel = PlayerModule.getPlayerModel(damager.getUniqueId());

            if (damageModel.getCurrentClass() == null) return;
            if (playerModel.getCurrentClass() == null) return;
            if (damager.getAttackCooldown() == 1) {
                damageModel.getCurrentClass().onChargedPlayerHit(damageModel);
            } else {
                damageModel.getCurrentClass().onNonChargedPlayerHit(damageModel);
            }
            playerModel.getCurrentClass().onTakenHit(playerModel);
        } else if (event.getDamager() instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player shooter) {
                PlayerModel shooterModel = PlayerModule.getPlayerModel(shooter.getUniqueId());
                if (shooterModel.getCurrentClass() == null) return;
                shooterModel.getCurrentClass().onPlayerHitBow(shooterModel);
                if (playerModel.getCurrentClass() == null) return;
                playerModel.getCurrentClass().onTakenBowHit(playerModel);
            }
        }
    }

    /**
     * Handles the event when a player interacts with the environment.
     *
     * @param event The PlayerInteractEvent representing the interaction event.
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;

        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());
        if (playerModel.getCurrentClass() == null) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                if (event.getItem().getType() == Material.BOW || event.getItem().getType() == Material.IRON_SWORD) {
                    if (playerModel.getEnergy() >= playerModel.getCurrentClass().getClassOptions().getClassEnergy().getNeededEnergyForAbility()) {
                        playerModel.getCurrentClass().executeAbility(playerModel);
                    }
                }
            }
        }
    }

    /**
     * Handles the event when a player places a block.
     *
     * @param event The BlockPlaceEvent representing the block placement event.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());
        if (playerModel.getCurrentClass() == null) return;

        playerModel.getCurrentClass().onBuild(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());
        if (playerModel.getCurrentClass() == null) return;

        playerModel.getCurrentClass().onBreak(event);
    }

    @EventHandler
    public void onPotionConsume(PlayerItemConsumeEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.ACTIVE) return;
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());
        if (playerModel.getCurrentClass() == null) return;

        playerModel.getCurrentClass().onPotionConsume(event);
    }
}

package dev.tqqn.megawalls.modules.classes.framework.listener;

import dev.tqqn.megawalls.modules.classes.framework.AbstractClass;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.modules.player.data.TempPlayerData;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public final class ClassesListener implements Listener {

    private final GameModule gameModule;

    /**
     * Handles the Energy gain of the players.
     *
     * @param event The EntityDamageByEntityEvent representing the hit event.
     */
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player player)) return;
        final PlayerModel defendingModel = PlayerModule.getPlayerModel(player.getUniqueId());
        if (defendingModel == null) return;

        final AbstractClass defendingClass = defendingModel.getTempPlayerData().getCurrentClass();
        if (defendingClass == null) return;

        if (event.getDamager() instanceof Player damager) {
            if (damager == player) return; //Hits self

            final PlayerModel attackingModel = PlayerModule.getPlayerModel(damager.getUniqueId());
            if (attackingModel == null) return;

            final AbstractClass attackingClass = attackingModel.getTempPlayerData().getCurrentClass();

            if (attackingClass == null) return;

            if (damager.getAttackCooldown() == 1) {
                attackingClass.onChargedPlayerHit(attackingModel);
            } else {
                attackingClass.onNonChargedPlayerHit(attackingModel);
            }

            defendingClass.onTakenHit(defendingModel);

        } else if (event.getDamager() instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player shooter) {
                if (shooter == player) return;

                final PlayerModel shooterModel = PlayerModule.getPlayerModel(shooter.getUniqueId());
                if (shooterModel == null) return;

                final AbstractClass shooterClass = shooterModel.getTempPlayerData().getCurrentClass();

                if (shooterClass == null) return;
                shooterClass.onPlayerHitBow(shooterModel);

                defendingClass.onTakenBowHit(defendingModel);
            }
        }
    }

    //TODO: Better check on item ability use. Best to implement this in class module.

    /**
     * Handles the event when a player interacts with the environment.
     *
     * @param event The PlayerInteractEvent representing the interaction event.
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!gameModule.isState(GameStates.ACTIVE)) return;

        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());
        final TempPlayerData tempPlayerData = playerModel.getTempPlayerData();

        final AbstractClass playerClass = tempPlayerData.getCurrentClass();
        if (playerClass == null) return;

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.BOW && event.getItem().getType() != Material.IRON_SWORD) return;

        if (tempPlayerData.getEnergy() >= playerClass.getClassOptions().getClassEnergy().getNeededEnergyForAbility()) {
            playerClass.executeAbility(playerModel);
        }
    }

    /**
     * Handles the event when a player places a block.
     *
     * @param event The BlockPlaceEvent representing the block placement event.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!gameModule.isState(GameStates.ACTIVE)) return;
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());
        final AbstractClass playerClass = playerModel.getTempPlayerData().getCurrentClass();
        if (playerClass == null) return;

        playerClass.onBuild(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!gameModule.isState(GameStates.ACTIVE)) return;
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());
        final AbstractClass playerClass = playerModel.getTempPlayerData().getCurrentClass();
        if (playerClass == null) return;

        playerClass.onBreak(event);
    }

    @EventHandler
    public void onPotionConsume(PlayerItemConsumeEvent event) {
        if (!gameModule.isState(GameStates.ACTIVE)) return;
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());
        final AbstractClass playerClass = playerModel.getTempPlayerData().getCurrentClass();
        if (playerClass == null) return;

        playerClass.onPotionConsume(event);
    }
}

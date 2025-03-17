package dev.tqqn.megawalls.modules.region.framework.types;

import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.teams.TeamModule;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import dev.tqqn.megawalls.modules.teams.framework.wither.GameWither;
import dev.tqqn.megawalls.modules.region.framework.AbstractRegion;
import dev.tqqn.megawalls.modules.region.framework.Cuboid;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.common.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * The TeamProtectionRegion class represents a protection region around a team's spawn zone.
 * It restricts certain actions within the region to ensure fair gameplay.
 */
public final class TeamProtectionRegion extends AbstractRegion {

    private final GameTeam gameTeam;

    /**
     * Constructs a TeamProtectionRegion object with the given name, cuboid boundaries, and associated game team.
     *
     * @param name     The name of the region.
     * @param cuboid   The cuboid defining the boundaries of the region.
     * @param gameTeam The game team associated with this region.
     */
    public TeamProtectionRegion(TeamModule.TeamStaticData name, Cuboid cuboid, GameTeam gameTeam) {
        super(name.name(), cuboid, RegionType.PROTECTION);
        this.gameTeam = gameTeam;
    }

    /**
     * Handles player exit events from the team protection region.
     * Notifies players if they lose protection upon leaving the spawn zone.
     *
     * @param player The player leaving the region.
     */
    @Override
    public void onExit(Player player) {

        final PlayerModel playerModel = PlayerModule.getPlayerModel(player.getUniqueId());
        if (playerModel.getTempPlayerData().getGameTeam() != gameTeam) return;
        if (playerModel.getTempPlayerData().isProtected()) {
            player.sendMessage(ChatUtil.format("<red>You lost your protection because you left your spawn zone!"));
            playerModel.getTempPlayerData().setProtected(false);
        }
    }

    /**
     * Handles block break events within the team protection region.
     * Cancels block breaking if the wither status is not DEATH.
     *
     * @param event The BlockBreakEvent.
     */
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (gameTeam.getGameWither().getWitherStatus() != GameWither.WitherStatus.DEATH) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles block place events within the team protection region.
     * Cancels block placing if the wither status is not DEATH.
     *
     * @param event The BlockPlaceEvent.
     */
    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (gameTeam.getGameWither().getWitherStatus() != GameWither.WitherStatus.DEATH) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles entity damage events within the team protection region.
     * Cancels damage events for players who are protected.
     *
     * @param event The EntityDamageEvent.
     */
    @Override
    public void onDamage(EntityDamageEvent event) {
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getEntity().getUniqueId());
        if (!playerModel.getTempPlayerData().isProtected()) return;

        if (playerModel.getTempPlayerData().getGameTeam() == gameTeam) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles entity damage by entity events within the team protection region.
     * Cancels damage events and notifies the attacker if the player is protected.
     *
     * @param event The EntityDamageByEntityEvent.
     */
    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getEntity().getUniqueId());
        if (!playerModel.getTempPlayerData().isProtected()) return;

        if (playerModel.getTempPlayerData().getGameTeam() == gameTeam) {
            event.setCancelled(true);
            event.getDamager().sendMessage(ChatUtil.format("<red><bold>Leave the spawn zone! <reset><red>Spawned players will have increased damage!"));
        }
    }
}

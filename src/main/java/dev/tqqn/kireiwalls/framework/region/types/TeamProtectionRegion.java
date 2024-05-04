package dev.tqqn.kireiwalls.framework.region.types;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.framework.game.teams.wither.GameWither;
import dev.tqqn.kireiwalls.framework.region.AbstractRegion;
import dev.tqqn.kireiwalls.framework.region.Cuboid;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class TeamProtectionRegion extends AbstractRegion {

    private final GameTeam gameTeam;

    public TeamProtectionRegion(String name, Cuboid cuboid, GameTeam gameTeam) {
        super(name, cuboid, RegionType.PROTECTION);
        this.gameTeam = gameTeam;
    }

    @Override
    public void onExit(Player player) {

        final PlayerModel playerModel = PlayerModule.getPlayerModel(player.getUniqueId());
        if (playerModel.isProtected()) {
            player.sendMessage(ChatUtil.format("<red>You lost your protection because you left your spawn zone!"));
            playerModel.setProtected(false);
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (gameTeam.getGameWither().getWitherStatus() != GameWither.WitherStatus.DEATH) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (gameTeam.getGameWither().getWitherStatus() != GameWither.WitherStatus.DEATH) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getEntity().getUniqueId());
        if (!playerModel.isProtected()) return;

        if (playerModel.getGameTeam() == gameTeam) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getEntity().getUniqueId());
        if (!playerModel.isProtected()) return;

        if (playerModel.getGameTeam() == gameTeam) {
            event.setCancelled(true);
            event.getDamager().sendMessage(ChatUtil.format("<red><bold>Leave the spawn zone! <reset><red>Spawned players will have increased damage!"));
        }
    }
}

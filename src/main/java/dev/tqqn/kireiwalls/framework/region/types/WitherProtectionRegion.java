package dev.tqqn.kireiwalls.framework.region.types;

import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.framework.game.teams.wither.GameWither;
import dev.tqqn.kireiwalls.framework.region.AbstractRegion;
import dev.tqqn.kireiwalls.framework.region.Cuboid;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class WitherProtectionRegion extends AbstractRegion {

    private final GameTeam gameTeam;

    public WitherProtectionRegion(String name, Cuboid cuboid, GameTeam gameTeam) {
        super(name, cuboid, RegionType.WITHER);
        this.gameTeam = gameTeam;
    }

    @Override
    public void onEntry(Player player) {
        if (gameTeam.getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH) return;

        gameTeam.getGameWither().getWitherBar().addPlayer(player);
    }

    @Override
    public void onExit(Player player) {
        if (gameTeam.getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH) return;
        gameTeam.getGameWither().getWitherBar().removePlayer(player);
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
}

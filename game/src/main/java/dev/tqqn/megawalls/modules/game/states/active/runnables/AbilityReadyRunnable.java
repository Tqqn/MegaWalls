package dev.tqqn.megawalls.modules.game.states.active.runnables;

import dev.tqqn.megawalls.common.classes.ClassAbilityParticles;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.game.GameModule;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

@RequiredArgsConstructor
public final class AbilityReadyRunnable extends BukkitRunnable {

    private final GameModule gameModule;

    @Override
    public void run() {
        for (PlayerModel playerModel : gameModule.getInGamePlayers()) {
            if (!playerModel.getTempPlayerData().getCurrentClass().canUseAbility(playerModel)) continue;

            final Location playerLocation = playerModel.getPlayer().getLocation();
            ClassAbilityParticles.DEFAULT_GREEN.spawnParticle(calculateHandLocation(playerLocation));
        }
    }

    private Location calculateHandLocation(Location playerLoc) {
        final Location handLocation = playerLoc.clone();
        final Vector handDirection = playerLoc.getDirection().clone();

        handLocation.add(0, 0.5, 0);
        handDirection.multiply(2);

        return handLocation.add(handDirection);
    }
}

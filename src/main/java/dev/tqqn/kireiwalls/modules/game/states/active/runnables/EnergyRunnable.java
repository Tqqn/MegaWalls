package dev.tqqn.kireiwalls.modules.game.states.active.runnables;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The EnergyRunnable class represents a BukkitRunnable task responsible for updating the energy bar flicker.
 */
public final class EnergyRunnable extends BukkitRunnable {

    private final GameModule gameModule;
    private boolean flicker;

    public EnergyRunnable(GameModule gameModule) {
        this.gameModule = gameModule;
        flicker = false;
    }

    @Override
    public void run() {
        for (PlayerModel playerModel : gameModule.getIngamePlayers()) {
            if (playerModel.isSpectatorMode()) continue;
            if (playerModel.getCurrentClass().canUseAbility(playerModel)) {
                if (flicker) {
                    flicker = false;
                } else {
                    flicker = true;
                }
                // TODO: Flickering Energy Bar
            }
        }
    }
}

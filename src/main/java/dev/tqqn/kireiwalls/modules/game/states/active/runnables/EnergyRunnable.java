package dev.tqqn.kireiwalls.modules.game.states.active.runnables;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The EnergyRunnable class represents a BukkitRunnable task responsible for updating the energy bar flicker.
 */
public final class EnergyRunnable extends BukkitRunnable {

    private boolean flicker;

    public EnergyRunnable() {
        flicker = false;
    }

    @Override
    public void run() {
        for (PlayerModel playerModel : GameModule.getIngamePlayers()) {
            if (playerModel.getTempPlayerData().isSpectatorMode()) continue;
            if (playerModel.getTempPlayerData().getCurrentClass().canUseAbility(playerModel)) {
                if (flicker) {
                    KireiWalls.getReflectionLayer().sendEnergy(playerModel.getPlayer(), playerModel.getTempPlayerData().getEnergy(), 0);
                } else {
                    KireiWalls.getReflectionLayer().sendEnergy(playerModel.getPlayer(), playerModel.getTempPlayerData().getEnergy(), 1);
                }
            }
        }
        flicker = !flicker;
    }
}

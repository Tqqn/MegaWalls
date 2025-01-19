package dev.tqqn.megawalls.modules.game.states.active.runnables;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.game.GameModule;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The EnergyRunnable class represents a BukkitRunnable task responsible for updating the energy bar flicker.
 */
public final class EnergyRunnable extends BukkitRunnable {

    private static final GameModule GAME_MODULE = MegaWalls.getInstance().getModuleManager().getModule(GameModule.class);

    private boolean flicker;

    public EnergyRunnable() {
        flicker = false;
    }

    @Override
    public void run() {
        for (PlayerModel playerModel : GAME_MODULE.getInGamePlayers()) {
            if (playerModel.getTempPlayerData().isSpectatorMode()) continue;
            if (!playerModel.getTempPlayerData().getCurrentClass().canUseAbility(playerModel)) continue;

            if (flicker) {
                MegaWalls.getReflectionLayer().sendEnergy(playerModel.getPlayer(), playerModel.getTempPlayerData().getEnergy(), 0);
            } else {
                MegaWalls.getReflectionLayer().sendEnergy(playerModel.getPlayer(), playerModel.getTempPlayerData().getEnergy(), 1);
            }
        }
        flicker = !flicker;
    }
}

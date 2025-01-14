package dev.tqqn.megawalls.modules.game.states.active.runnables;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.game.GameModule;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (PlayerModel playerModel : GameModule.getIngamePlayers()) {
            if (playerModel.getTempPlayerData().getCurrentClass() == null) continue;
            if (playerModel.getTempPlayerData().isSpectatorMode()) continue;

            MegaWalls.getReflectionLayer().sendActionBar(playerModel);
        }
    }
}

package dev.tqqn.kireiwalls.modules.game.states.active.runnables;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (PlayerModel playerModel : GameModule.getIngamePlayers()) {
            if (playerModel.getTempPlayerData().getCurrentClass() == null) continue;
            if (playerModel.getTempPlayerData().isSpectatorMode()) continue;

            KireiWalls.getReflectionLayer().sendActionBar(playerModel);
        }
    }
}

package dev.tqqn.kireiwalls.modules.game.states.active.runnables;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarRunnable extends BukkitRunnable {

    private final GameModule gameModule;

    public ActionBarRunnable(GameModule gameModule) {
        this.gameModule = gameModule;
    }

    @Override
    public void run() {
        for (PlayerModel playerModel : gameModule.getIngamePlayers()) {
            if (playerModel.getCurrentClass() == null) continue;
            if (playerModel.isSpectatorMode()) continue;

            KireiWalls.getReflectionLayer().sendActionBar(playerModel);
        }
    }
}

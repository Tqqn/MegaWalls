package dev.tqqn.megawalls.modules.game.states.active.runnables;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.game.GameModule;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarRunnable extends BukkitRunnable {

    private static final GameModule GAME_MODULE = MegaWalls.getInstance().getModuleManager().getModule(GameModule.class);

    @Override
    public void run() {
        for (PlayerModel playerModel : GAME_MODULE.getInGamePlayers()) {
            if (playerModel.getTempPlayerData().getCurrentClass() == null) continue;
            if (playerModel.getTempPlayerData().isSpectatorMode()) continue;

            MegaWalls.getReflectionLayer().sendActionBar(playerModel);
        }
    }
}

package dev.tqqn.megawalls.modules.player.listeners;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.database.framework.events.GamePlayerJoinEvent;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerListeners implements Listener {

    private final PlayerModule playerModule;

    public PlayerListeners() {
        playerModule = MegaWalls.getInstance().getModuleManager().getModule(PlayerModule.class);
    }

    @EventHandler
    public void onJoin(GamePlayerJoinEvent event) {
        playerModule.handlePlayerJoin(event.getPlayerModel());
    }
}

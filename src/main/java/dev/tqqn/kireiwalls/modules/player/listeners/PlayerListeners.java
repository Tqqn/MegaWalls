package dev.tqqn.kireiwalls.modules.player.listeners;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.database.framework.events.GamePlayerJoinEvent;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerListeners implements Listener {

    private final PlayerModule playerModule;

    public PlayerListeners() {
        playerModule = KireiWalls.getInstance().getModuleManager().getModule(PlayerModule.class);
    }

    @EventHandler
    public void onJoin(GamePlayerJoinEvent event) {
        playerModule.handlePlayerJoin(event.getPlayerModel());
    }
}

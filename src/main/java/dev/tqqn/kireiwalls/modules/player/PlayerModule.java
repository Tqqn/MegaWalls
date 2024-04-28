package dev.tqqn.kireiwalls.modules.player;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.player.listeners.SpectatorListeners;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerModule extends AbstractModule {
    private static final HashMap<UUID, PlayerModel> CACHED_PLAYERS = new HashMap<>();

    public PlayerModule(KireiWalls plugin) {
        super(plugin, "Player");
    }

    public void onEnable() {
        this.addComponent(SpectatorListeners.class, "");
    }

    public void onDisable() {
    }

    public static PlayerModel getPlayerModel(UUID uuid) {
        return CACHED_PLAYERS.get(uuid);
    }

    public void cachePlayerModel(PlayerModel playerModel) {
        CACHED_PLAYERS.put(playerModel.getUuid(), playerModel);
    }

    public void removePlayerFromCache(UUID uuid) {
        CACHED_PLAYERS.remove(uuid);
    }

    public void processSpigotLogin(Player player) {
    }
}

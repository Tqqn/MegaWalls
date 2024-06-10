package dev.tqqn.kireiwalls.modules.player;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.AbstractModule;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.player.listeners.SpectatorListeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The PlayerModule class manages player-related functionality and data caching.
 */
public final class PlayerModule extends AbstractModule {

    private static final Map<UUID, PlayerModel> CACHED_PLAYERS = new HashMap<>();

    public PlayerModule(KireiWalls plugin) {
        super(plugin, "Player");
    }

    @Override
    public void onEnable() {
        this.addComponent(SpectatorListeners.class);
    }

    /**
     * Retrieves the PlayerModel associated with the specified UUID.
     *
     * @param uuid The UUID of the player.
     * @return The PlayerModel associated with the UUID.
     */
    public static PlayerModel getPlayerModel(UUID uuid) {
        return CACHED_PLAYERS.get(uuid);
    }

    /**
     * Caches the PlayerModel associated with the specified player.
     *
     * @param playerModel The PlayerModel to be cached.
     */
    public void cachePlayerModel(PlayerModel playerModel) {
        CACHED_PLAYERS.put(playerModel.getUuid(), playerModel);
    }

    /**
     * Removes the PlayerModel associated with the specified UUID from the cache.
     *
     * @param uuid The UUID of the player to be removed from the cache.
     */
    public void removePlayerFromCache(UUID uuid) {
        CACHED_PLAYERS.remove(uuid);
    }
}

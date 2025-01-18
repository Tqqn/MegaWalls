package dev.tqqn.megawalls.modules.player;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.classes.framework.Skins;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.player.listeners.PlayerListeners;
import dev.tqqn.megawalls.modules.player.listeners.SpectatorListeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The PlayerModule class manages player-related functionality and data caching.
 */
public final class PlayerModule extends AbstractModule {

    private static final Map<UUID, PlayerModel> CACHED_PLAYERS = new HashMap<>();

    private final GameModule gameModule;

    public PlayerModule(MegaWalls plugin, GameModule gameModule) {
        super(plugin, "Player");
        this.gameModule = gameModule;
    }

    @Override
    public void onEnable() {
        register(new PlayerListeners());
        register(new SpectatorListeners(gameModule));
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

    public void handlePlayerJoin(PlayerModel playerModel) {
        if (playerModel.getTempPlayerData().getGameTeam() != null) {
            playerModel.getTempPlayerData().getGameTeam().sendNameTag(playerModel);
        }

        if (!gameModule.isState(GameStates.ACTIVE) && !gameModule.isState(GameStates.WAITING)) return;
        if (playerModel.getTempPlayerData().getGameTeam() != null && !playerModel.getTempPlayerData().isSpectatorMode()) {
            playerModel.getTempPlayerData().getGameTeam().addAlivePlayer(playerModel);
        }

        MegaWalls.getReflectionLayer().changeSkin(playerModel.getTempPlayerData().getCurrentClass() == null ? Skins.RANDOM : playerModel.getTempPlayerData().getCurrentClass().getSkins(), playerModel);
    }
}

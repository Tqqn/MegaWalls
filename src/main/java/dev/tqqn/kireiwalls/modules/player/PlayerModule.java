package dev.tqqn.kireiwalls.modules.player;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerModule extends AbstractModule {

    private static final HashMap<UUID, PlayerModel> CACHED_PLAYERS = new HashMap<>(); // Cached Player Map

    public PlayerModule(KireiWalls plugin) {
        super(plugin, "Player");

    }

    @Override
    public void onEnable() {
    }

    @Override
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
        System.out.println("Removed");
    }

    public void processSpigotLogin(Player player) {
        // TODO: checks if lobby or shit
    }


}

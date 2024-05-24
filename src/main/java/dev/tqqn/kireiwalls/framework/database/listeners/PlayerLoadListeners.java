package dev.tqqn.kireiwalls.framework.database.listeners;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.events.GamePlayerJoinEvent;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The PlayerLoadListeners class implements event listeners for player loading events.
 * It manages the loading of player data from the database and handles player login and join events.
 */
public final class PlayerLoadListeners implements Listener {

    private final DatabaseModule databaseModule;
    private final PlayerModule playerModule;
    private final ConcurrentHashMap<UUID, PlayerModel> joiningPlayers = new ConcurrentHashMap<>();

    public PlayerLoadListeners() {
        this.databaseModule = KireiWalls.getInstance().getModuleManager().getModule(DatabaseModule.class);
        this.playerModule = databaseModule.getPlugin().getModuleManager().getModule(PlayerModule.class);
    }

    /**
     * Listens for the AsyncPlayerPreLoginEvent and loads player data if not already loaded.
     *
     * @param event The AsyncPlayerPreLoginEvent.
     */
    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!isPlayerLoaded(event.getUniqueId())) {
            PlayerModel playerModel = databaseModule.getPlayer(event.getUniqueId(), event.getName());

            joiningPlayers.put(event.getUniqueId(), playerModel);
        }
    }

    /**
     * Listens for the PlayerLoginEvent and handles login procedures.
     *
     * @param event The PlayerLoginEvent.
     */
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (GameModule.getCurrentState().getGameStates() != GameStates.WAITING && !event.getPlayer().hasPermission("staff.join") && !isPlayerLoaded(event.getPlayer().getUniqueId())) {
            event.kickMessage(ChatUtil.format("<red>This game has already started."));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            joiningPlayers.remove(event.getPlayer().getUniqueId());
            return;
        }

        PlayerModel playerModel;

        if (!isPlayerLoaded(event.getPlayer().getUniqueId())) {
            playerModel = joiningPlayers.get(event.getPlayer().getUniqueId());
        } else {
            playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());
        }

        if (playerModel == null) {
            event.kickMessage(ChatUtil.format("<red>Something went wrong getting your playerdata! Try it again later."));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

    /**
     * Listens for the PlayerJoinEvent and handles player joining procedures.
     *
     * @param event The PlayerJoinEvent.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.empty());
        final Player player = event.getPlayer();

        PlayerModel playerModel;

        if (!isPlayerLoaded(player.getUniqueId())) {
            playerModel = joiningPlayers.remove(player.getUniqueId());
            playerModule.cachePlayerModel(playerModel);
        } else {
            playerModel = PlayerModule.getPlayerModel(player.getUniqueId());
        }

        GamePlayerJoinEvent gamePlayerJoinEvent = new GamePlayerJoinEvent(playerModel);
        Bukkit.getPluginManager().callEvent(gamePlayerJoinEvent);

        if (!playerModel.getName().equals(player.getName())) playerModel.setName(player.getName());
    }

    /**
     * Checks if a player is already loaded.
     *
     * @param uuid The UUID of the player.
     * @return {@code true} if the player is already loaded, {@code false} otherwise.
     */
    private boolean isPlayerLoaded(UUID uuid) {
        return PlayerModule.getPlayerModel(uuid) != null;
    }
}

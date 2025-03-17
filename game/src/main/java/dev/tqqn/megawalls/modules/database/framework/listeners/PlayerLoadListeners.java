package dev.tqqn.megawalls.modules.database.framework.listeners;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.database.framework.events.GamePlayerJoinEvent;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.database.DatabaseModule;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.common.utils.ChatUtil;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The PlayerLoadListeners class implements event listeners for player loading events.
 * It manages the loading of player data from the database and handles player login and join events.
 */

@RequiredArgsConstructor
public final class PlayerLoadListeners implements Listener {

    private final DatabaseModule databaseModule;
    private final GameModule gameModule;
    private final PlayerModule playerModule;
    private final ConcurrentHashMap<UUID, PlayerModel> joiningPlayers = new ConcurrentHashMap<>();

    /**
     * Listens for the AsyncPlayerPreLoginEvent and loads player data if not already loaded.
     *
     * @param event The AsyncPlayerPreLoginEvent.
     */
    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (isPlayerLoaded(event.getUniqueId())) return;
        PlayerModel playerModel = databaseModule.getPlayer(event.getUniqueId(), event.getName());

        joiningPlayers.put(event.getUniqueId(), playerModel);
    }

    /**
     * Listens for the PlayerLoginEvent and handles login procedures.
     *
     * @param event The PlayerLoginEvent.
     */
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();

        if (!gameModule.isState(GameStates.WAITING) && !player.hasPermission("staff.join") && !isPlayerLoaded(player.getUniqueId())) {
            event.kickMessage(ChatUtil.format("<red>This game has already started."));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            joiningPlayers.remove(player.getUniqueId());
            return;
        }

        PlayerModel playerModel;

        if (!isPlayerLoaded(player.getUniqueId())) {
            playerModel = joiningPlayers.get(player.getUniqueId());
        } else {
            playerModel = PlayerModule.getPlayerModel(player.getUniqueId());
        }

        if (playerModel == null) {
            event.kickMessage(ChatUtil.format("<red>Something went wrong getting your playerdata! Try it again later."));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            return;
        }

        playerModel.setPlayerWeakReference(new WeakReference<>(player));
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
        final UUID playerUUID = player.getUniqueId();

        PlayerModel playerModel;

        if (!isPlayerLoaded(playerUUID)) {
            playerModel = joiningPlayers.remove(playerUUID);
            playerModule.cachePlayerModel(playerModel);
        } else {
            playerModel = PlayerModule.getPlayerModel(playerUUID);
        }

        GamePlayerJoinEvent gamePlayerJoinEvent = new GamePlayerJoinEvent(playerModel);
        Bukkit.getPluginManager().callEvent(gamePlayerJoinEvent);

        if (!playerModel.getName().equals(player.getName())) playerModel.setName(player.getName());
        MegaWalls.getReflectionLayer().injectPlayer(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        MegaWalls.getReflectionLayer().unInjectPlayer(event.getPlayer());
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

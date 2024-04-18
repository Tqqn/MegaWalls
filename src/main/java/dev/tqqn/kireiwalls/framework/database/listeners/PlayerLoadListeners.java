package dev.tqqn.kireiwalls.framework.database.listeners;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.events.GamePlayerJoinEvent;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.states.active.board.ActiveBoard;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;
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

public class PlayerLoadListeners implements Listener {

    private final DatabaseModule databaseModule;
    private final PlayerModule playerModule;
    private final ConcurrentHashMap<UUID, PlayerModel> joiningPlayers = new ConcurrentHashMap<>();

    public PlayerLoadListeners() {
        this.databaseModule = (DatabaseModule) KireiWalls.getInstance().getModuleManager().getModule(DatabaseModule.class);
        this.playerModule = (PlayerModule) databaseModule.getPlugin().getModuleManager().getModule(PlayerModule.class);
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {

        PlayerModel playerModel = databaseModule.getPlayer(event.getUniqueId(), event.getName());

        joiningPlayers.put(event.getUniqueId(), playerModel);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        PlayerModel playerModel = joiningPlayers.get(event.getPlayer().getUniqueId());
        if (playerModel == null) {
            event.kickMessage(ChatUtil.format("<red>Something went wrong getting your playerdata! Try it again later."));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.empty());
        final Player player = event.getPlayer();

        PlayerModel playerModel = joiningPlayers.remove(player.getUniqueId());

        playerModule.cachePlayerModel(playerModel);

        if (!playerModel.getName().equals(player.getName())) playerModel.setName(player.getName());

        Bukkit.getPluginManager().callEvent(new GamePlayerJoinEvent(PlayerModule.getPlayerModel(player.getUniqueId())));

        playerModule.processSpigotLogin(player);
    }
}

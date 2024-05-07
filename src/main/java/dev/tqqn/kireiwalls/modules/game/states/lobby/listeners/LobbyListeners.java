package dev.tqqn.kireiwalls.modules.game.states.lobby.listeners;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.events.GamePlayerJoinEvent;
import dev.tqqn.kireiwalls.modules.ModuleManager;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.lobby.board.LobbyBoard;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * The LobbyListeners class contains event handlers for actions that occur in the lobby.
 */
public final class LobbyListeners implements Listener {
    private final DatabaseModule databaseModule;
    private final GameModule gameModule;

    public LobbyListeners() {
        ModuleManager moduleManager = KireiWalls.getInstance().getModuleManager();
        this.databaseModule = (DatabaseModule)moduleManager.getModule(DatabaseModule.class);
        this.gameModule = (GameModule)moduleManager.getModule(GameModule.class);
    }

    /**
     * Handles the event when a player joins the lobby.
     *
     * @param event The GamePlayerJoinEvent
     */
    @EventHandler
    public void onJoin(GamePlayerJoinEvent event) {
        this.gameModule.getIngamePlayers().add(event.getPlayerModel());
        if (event.getPlayerModel().isBuildMode()) {
            event.getPlayerModel().getPlayer().setGameMode(GameMode.CREATIVE);
        } else {
            event.getPlayerModel().getPlayer().setGameMode(GameMode.SURVIVAL);
            event.getPlayerModel().getPlayer().getInventory().clear();
        }

        event.getPlayerModel().getPlayer().teleport(gameModule.getGameSettings().getLobbyLocation());
        ScoreboardModule scoreboardModule = (ScoreboardModule) databaseModule.getPlugin().getModuleManager().getModule(ScoreboardModule.class);
        scoreboardModule.setScoreboard(event.getPlayerModel(), new LobbyBoard(event.getPlayerModel()));
        event.getPlayerModel().getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        event.getPlayerModel().getPlayer().setHealth(event.getPlayerModel().getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        event.getPlayerModel().getPlayer().setFoodLevel(20);
    }

    /**
     * Handles the event when a player quits the game.
     *
     * @param event The PlayerQuitEvent
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.databaseModule.getPlayerModule().removePlayerFromCache(event.getPlayer().getUniqueId());
    }

    /**
     * Handles the event when a block is broken in the lobby.
     *
     * @param event The BlockBreakEvent
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    /**
     * Handles the event when a block is placed in the lobby.
     *
     * @param event The BlockPlaceEvent
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    /**
     * Handles the event when an entity is damaged in the lobby.
     *
     * @param event The EntityDamageEvent
     */
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    /**
     * Handles the event when a player loses hunger in the lobby.
     *
     * @param event The FoodLevelChangeEvent
     */
    @EventHandler
    public void onHungerLose(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}

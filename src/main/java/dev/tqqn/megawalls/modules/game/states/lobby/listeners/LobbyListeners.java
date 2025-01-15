package dev.tqqn.megawalls.modules.game.states.lobby.listeners;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.classes.framework.Skins;
import dev.tqqn.megawalls.modules.classes.framework.menu.ClassChooseMenu;
import dev.tqqn.megawalls.modules.database.framework.events.GamePlayerJoinEvent;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.ModuleManager;
import dev.tqqn.megawalls.modules.arena.ArenaModule;
import dev.tqqn.megawalls.modules.database.DatabaseModule;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.states.lobby.board.LobbyBoard;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.modules.player.data.TempPlayerData;
import dev.tqqn.megawalls.modules.scoreboard.ScoreboardModule;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * The LobbyListeners class contains event handlers for actions that occur in the lobby.
 */
public final class LobbyListeners implements Listener {

    private final DatabaseModule databaseModule;
    private final GameModule gameModule;
    private final ArenaModule arenaModule;

    public LobbyListeners() {
        ModuleManager moduleManager = MegaWalls.getInstance().getModuleManager();
        this.databaseModule = moduleManager.getModule(DatabaseModule.class);
        this.gameModule = moduleManager.getModule(GameModule.class);
        this.arenaModule = moduleManager.getModule(ArenaModule.class);
    }

    /**
     * Handles the event when a player joins the lobby.
     *
     * @param event The GamePlayerJoinEvent
     */
    @EventHandler
    public void onJoin(GamePlayerJoinEvent event) {
        final PlayerModel playerModel = event.getPlayerModel();
        final Player player = playerModel.getPlayer();

        if (player == null) {
            event.setCancelled(true);
            return;
        }

        final TempPlayerData tempPlayerData = playerModel.getTempPlayerData();

        GameModule.getIngamePlayers().add(event.getPlayerModel());
        if (tempPlayerData.isBuildMode()) {
            player.setGameMode(GameMode.CREATIVE);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
        }

        player.teleport(arenaModule.getCurrentArena().getArenaSettings().getLobbyLocation());

        ScoreboardModule scoreboardModule = MegaWalls.getInstance().getModuleManager().getModule(ScoreboardModule.class);
        scoreboardModule.setScoreboard(playerModel, new LobbyBoard(playerModel));

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        player.setHealth(playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        player.setFoodLevel(20);

        if (tempPlayerData.getCurrentClass() != null) {
            tempPlayerData.getCurrentClass().applySkin(event.getPlayerModel());
        } else {
            MegaWalls.getReflectionLayer().changeSkin(Skins.RANDOM, playerModel);
        }

        MegaWalls.getInstance().getServer().getScheduler().runTaskLater(MegaWalls.getInstance(), () -> gameModule.giveLobbyItems(playerModel), 1L);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        final ItemStack itemStack = event.getItem();

        if (!itemStack.getItemMeta().hasLocalizedName()) return;

        String localName = itemStack.getItemMeta().getLocalizedName();

        final Player player = event.getPlayer();

        switch (localName) {
            case "class_selector" -> {
                new ClassChooseMenu(PlayerModule.getPlayerModel(player.getUniqueId())).open();
            }

            case "skin_selector" -> {
                // open Skin Selector #TODO
            }
        }
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

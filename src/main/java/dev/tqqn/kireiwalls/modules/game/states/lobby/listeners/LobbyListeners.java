package dev.tqqn.kireiwalls.modules.game.states.lobby.listeners;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.classes.AbstractClass;
import dev.tqqn.kireiwalls.framework.classes.Skins;
import dev.tqqn.kireiwalls.framework.classes.menu.ClassChooseMenu;
import dev.tqqn.kireiwalls.framework.database.events.GamePlayerJoinEvent;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.ModuleManager;
import dev.tqqn.kireiwalls.modules.arena.ArenaModule;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.lobby.board.LobbyBoard;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import dev.tqqn.kireiwalls.utils.FinalItems;
import dev.tqqn.kireiwalls.utils.ItemBuilder;
import dev.tqqn.kireiwalls.utils.MessageUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
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
        ModuleManager moduleManager = KireiWalls.getInstance().getModuleManager();
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

        GameModule.getIngamePlayers().add(event.getPlayerModel());
        if (playerModel.isBuildMode()) {
            playerModel.getPlayer().setGameMode(GameMode.CREATIVE);
        } else {
            playerModel.getPlayer().setGameMode(GameMode.SURVIVAL);
            playerModel.getPlayer().getInventory().clear();
        }

        playerModel.getPlayer().teleport(arenaModule.getCurrentArena().getArenaSettings().getLobbyLocation());
        ScoreboardModule scoreboardModule = databaseModule.getPlugin().getModuleManager().getModule(ScoreboardModule.class);
        scoreboardModule.setScoreboard(playerModel, new LobbyBoard(playerModel));
        playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        playerModel.getPlayer().setHealth(playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        playerModel.getPlayer().setFoodLevel(20);

        gameModule.giveLobbyItems(playerModel);

        if (playerModel.getCurrentClass() != null) {
            playerModel.getCurrentClass().applySkin(event.getPlayerModel());
        } else {
            KireiWalls.getReflectionLayer().changeSkin(Skins.RANDOM, playerModel);
        }

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

package dev.tqqn.megawalls.modules.game.states.active.listeners;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.chest.framework.ChestItem;
import dev.tqqn.megawalls.modules.chest.framework.events.GatheringChestSpawnEvent;
import dev.tqqn.megawalls.modules.database.framework.events.GamePlayerJoinEvent;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerStats;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.game.framework.events.GamePlayerKilledEvent;
import dev.tqqn.megawalls.modules.game.framework.events.GameWinEvent;
import dev.tqqn.megawalls.modules.game.framework.events.WitherDamageByPlayerEvent;
import dev.tqqn.megawalls.modules.player.data.TempPlayerData;
import dev.tqqn.megawalls.modules.teams.TeamModule;
import dev.tqqn.megawalls.modules.teams.framework.wither.GameWither;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.states.active.ActiveState;
import dev.tqqn.megawalls.modules.game.states.active.board.ActiveBoard;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.modules.scoreboard.ScoreboardModule;
import dev.tqqn.megawalls.utils.ChatUtil;
import dev.tqqn.megawalls.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 * The ActiveListeners class implements various event listeners for active gameplay.
 */
@RequiredArgsConstructor
public final class ActiveListeners implements Listener {

    private final GameModule gameModule;

    /**
     * Handles the event when a player joins during active gameplay.
     *
     * @param event The GamePlayerJoinEvent instance
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(GamePlayerJoinEvent event) {
        if (!gameModule.isState(GameStates.ACTIVE)) return;
        ScoreboardModule scoreboardModule = gameModule.getPlugin().getModuleManager().getModule(ScoreboardModule.class);
        scoreboardModule.setScoreboard(event.getPlayerModel(), new ActiveBoard(event.getPlayerModel()));

        event.getPlayerModel().getTempPlayerData().setSpectatorMode(event.getPlayerModel().getTempPlayerData().isSpectatorMode());
    }

    @EventHandler
    public void onChestSpawn(GatheringChestSpawnEvent event) {
        if (!gameModule.isState(GameStates.ACTIVE)) return;
        final PlayerModel playerModel = event.getPlayerModel();

        if (playerModel.getTempPlayerData().isFirstChest()) {
            event.addItem(new ChestItem(new ItemStack(Material.IRON_AXE), 100, 1, 1));
            event.addItem(new ChestItem(new ItemStack(Material.FURNACE), 100, 2, 4));
            event.addItem(new ChestItem(new ItemStack(Material.ARROW), 100, 20, 24));
            playerModel.getTempPlayerData().setFirstChest(false);
        }
    }

    /**
     * Handles the event when a player is killed during active gameplay.
     *
     * @param event The GamePlayerKilledEvent instance
     */
    @EventHandler
    public void onPlayerDeath(GamePlayerKilledEvent event) {

        final Player deathPlayer = event.getKilledPlayer().getPlayer();
        if (deathPlayer == null) return;

        String broadcastMessage = getDeathMessage(event);

        final TempPlayerData deathPlayerTemp = event.getKilledPlayer().getTempPlayerData();

        boolean isFinal = deathPlayerTemp.getGameTeam().getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH;
        String prefix;
        int coin;

        if (isFinal) {
            prefix = MessageUtil.FINAL_KILL.getStringMessage();
            coin = 30;
        } else {
            prefix = MessageUtil.KILL.getStringMessage();
            coin = 15;
        }

        if (event.getKiller() != null) {
            final Player killer = event.getKiller().getPlayer();
            final TempPlayerData killerTempData = event.getKiller().getTempPlayerData();

            if (isFinal) {
                killerTempData.getPlayerStats().increaseStat(PlayerStats.StatType.FINAL_KILLS);
                killerTempData.getGameTeam().increaseCurrentFinalKills();
            } else {
                killerTempData.getPlayerStats().increaseStat(PlayerStats.StatType.KILLS);
                killer.sendMessage(ChatUtil.format(broadcastMessage));
            }
            killer.sendMessage(ChatUtil.format(MessageUtil.COINS_EARNED.getStringMessage(String.valueOf(coin)) + prefix));
            killerTempData.increaseCoins(coin);
        }

        if (isFinal) {
            for (PlayerModel playerModel : gameModule.getInGamePlayers()) {
                playerModel.getPlayer().sendMessage(ChatUtil.format(broadcastMessage));
            }

            event.getKilledPlayer().getTempPlayerData().getGameTeam().decreaseFinalKills(event.getKilledPlayer());
        } else {
            event.getKilledPlayer().getPlayer().sendMessage(ChatUtil.format(broadcastMessage));
        }
    }

    private String getDeathMessage(GamePlayerKilledEvent event) {
        String broadcastMessage;

        final TeamModule.TeamStaticData killedTeamData = event.getKilledPlayer().getTempPlayerData().getGameTeam().getTeamData();

        switch (event.getDeathReason()) {
            case KILLED_BY_NO_PLAYER -> broadcastMessage = MessageUtil.KILLED_NO_KILLER.getStringMessage(killedTeamData.getColor(), event.getKilledPlayer().getName());
            case KILLED_BY_PLAYER_BOW -> {
                if (event.getKiller() == null) return "";
                final TeamModule.TeamStaticData killerTeamData = event.getKiller().getTempPlayerData().getGameTeam().getTeamData();
                broadcastMessage = MessageUtil.KILLED_BY_KILLER_BOW.getStringMessage(killedTeamData.getColor(), event.getKilledPlayer().getName(), killerTeamData.getColor(), event.getKiller().getName());
            }
            case KILLED_BY_PLAYER_HAND -> {
                if (event.getKiller() == null) return "";
                final TeamModule.TeamStaticData killerTeamData = event.getKiller().getTempPlayerData().getGameTeam().getTeamData();
                broadcastMessage = MessageUtil.KILLED_BY_KILLER_HAND.getStringMessage(killedTeamData.getColor(), event.getKilledPlayer().getName(), killerTeamData.getColor(), event.getKiller().getName());
            }
            default -> broadcastMessage = "";
        }
        return broadcastMessage;
    }

    /**
     * Handles the event when a player dies during active gameplay.
     *
     * @param event The PlayerDeathEvent instance
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.deathMessage(Component.empty());
        if (event.getDrops().isEmpty()) return;

        event.getDrops().removeIf(itemStack -> {
            if (!itemStack.getItemMeta().hasLocalizedName()) return false;
            return itemStack.getItemMeta().getLocalizedName().equals("kit");
        });
    }

    /**
     * Handles the event when an entity damages another entity during active gameplay.
     *
     * @param event The EntityDamageByEntityEvent instance
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player hitPlayer)) return;

        final ActiveState activeState = (ActiveState) gameModule.getCurrentState();

        if (activeState.getCurrentCycle() == ActiveState.Cycle.PREPARE || activeState.getCurrentCycle() == ActiveState.Cycle.END) {
            event.setCancelled(true);
            return;
        }

        final PlayerModel hitPlayerModel = PlayerModule.getPlayerModel(hitPlayer.getUniqueId());
        if (event.getDamager() instanceof Projectile projectile) {
            if (!(projectile.getShooter() instanceof Player shooter)) return;

            if (shooter == hitPlayer) {
                event.setCancelled(true);
                projectile.remove();
                return;
            }

            final PlayerModel shooterModel = PlayerModule.getPlayerModel(shooter.getUniqueId());
            if (shooterModel.getTempPlayerData().getGameTeam().equals(hitPlayerModel.getTempPlayerData().getGameTeam())) event.setCancelled(true);
        }

        if (!(event.getDamager() instanceof Player hitter)) return;
        if (hitter == hitPlayer) return;
        PlayerModel hitterModel = PlayerModule.getPlayerModel(hitter.getUniqueId());
        if (hitterModel.getTempPlayerData().getGameTeam().equals(hitPlayerModel.getTempPlayerData().getGameTeam())) event.setCancelled(true);
    }

    /**
     * Handles the event when a player respawns during active gameplay.
     *
     * @param event The PlayerRespawnEvent instance
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());
        final TempPlayerData tempPlayerData = playerModel.getTempPlayerData();

        if (tempPlayerData.getGameTeam().getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH) {

            if (event.getPlayer().getKiller() == null) {
                event.setRespawnLocation(tempPlayerData.getGameTeam().getGameTeamSettings().getSpawnLocation());
            } else {
                event.setRespawnLocation(event.getPlayer().getKiller().getLocation());
            }

            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> tempPlayerData.setSpectatorMode(true), 2L);
        } else {
            event.setRespawnLocation(tempPlayerData.getGameTeam().getGameTeamSettings().getSpawnLocation());
            tempPlayerData.setProtected(true);
            tempPlayerData.getCurrentClass().applyKit(playerModel);
        }
    }

    /**
     * Handles the event when a wither damages a player during active gameplay.
     *
     * @param event The WitherDamageByPlayerEvent instance
     */
    @EventHandler
    public void onWitherDamage(WitherDamageByPlayerEvent event) {
        if (event.getAttacker().getTempPlayerData().getGameTeam() == event.getWitherTeam()) {
            event.setCancelled(true);
            return;
        }

        event.getAttacker().getTempPlayerData().addWitherDamage(event.getWitherTeam().getGameWither(), event.getDamage());
    }

    /**
     * Handles the event when an entity damages another entity causing death during active gameplay.
     *
     * @param event The EntityDamageByEntityEvent instance
     */
    @EventHandler
    public void onEntityDamageDeath(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer)) return;
        if ((damagedPlayer.getHealth()) - event.getFinalDamage() > 0) return;

        final PlayerModel damagedPlayerModel = PlayerModule.getPlayerModel(damagedPlayer.getUniqueId());

        GamePlayerKilledEvent gamePlayerKilledEvent = null;

        final Location deathLocation = event.getEntity().getLocation().clone();

        if (event.getDamager() instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player damager) {
                gamePlayerKilledEvent = new GamePlayerKilledEvent(damagedPlayerModel, PlayerModule.getPlayerModel(damager.getUniqueId()), GamePlayerKilledEvent.DeathReason.KILLED_BY_PLAYER_BOW, deathLocation);
            } else {
                gamePlayerKilledEvent = new GamePlayerKilledEvent(damagedPlayerModel, null, GamePlayerKilledEvent.DeathReason.KILLED_BY_NO_PLAYER, deathLocation);
            }
        }

        if (event.getDamager() instanceof Player damager) {
            gamePlayerKilledEvent = new GamePlayerKilledEvent(damagedPlayerModel, PlayerModule.getPlayerModel(damager.getUniqueId()), GamePlayerKilledEvent.DeathReason.KILLED_BY_PLAYER_HAND, deathLocation);
        } else {
            if (!(event.getDamager() instanceof Projectile)) {
                gamePlayerKilledEvent = new GamePlayerKilledEvent(damagedPlayerModel, null, GamePlayerKilledEvent.DeathReason.KILLED_BY_NO_PLAYER, deathLocation);
            }
        }

        if (gamePlayerKilledEvent != null) {
            Bukkit.getPluginManager().callEvent(gamePlayerKilledEvent);
        }

        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> damagedPlayer.spigot().respawn(), 1L);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        event.setExpToDrop(0);
        Block block = event.getBlock();
        if (block.getType() == Material.DIAMOND_ORE) return;

        event.setDropItems(false);
        final Player player = event.getPlayer();
        player.getInventory().addItem(block.getDrops().toArray(new ItemStack[0]));
        if (block.getType() != Material.CHEST && block.getType() != Material.TRAPPED_CHEST) return;
        Chest chest = (Chest) block.getState();
        for (ItemStack itemStack : chest.getBlockInventory().getContents()) {
            if (itemStack == null) continue;
            player.getInventory().addItem(itemStack);
        }
    }

    @EventHandler
    public void onDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.getHealth() - event.getFinalDamage() > 0) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;

        final Location deathLocation = player.getLocation().clone();
        final GamePlayerKilledEvent gamePlayerKilledEvent = new GamePlayerKilledEvent(PlayerModule.getPlayerModel(event.getEntity().getUniqueId()), null, GamePlayerKilledEvent.DeathReason.KILLED_BY_NO_PLAYER, deathLocation);
        Bukkit.getPluginManager().callEvent(gamePlayerKilledEvent);
    }

    @EventHandler
    public void onPotionConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() != Material.POTION) return;
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> event.getPlayer().getInventory().remove(Material.GLASS_BOTTLE), 1L);
    }

    @EventHandler
    public void onGameWin(GameWinEvent event) {
        final TeamModule.TeamStaticData teamData = event.getWinner().getTeamData();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatUtil.centerMessage(MessageUtil.MESSAGE_LINE.getStringMessage()));
            player.sendMessage(ChatUtil.centerMessage("<white>Mega Walls"));
            player.sendMessage(" ");
            player.sendMessage(ChatUtil.centerMessage(teamData.getColor() + "Winner <gray>- " + teamData.getColor() + teamData.getPrettyName() + " Team"));
            if (event.getWinReason() == GameWinEvent.WinReason.DRAW) player.sendMessage(ChatUtil.centerMessage("<gray>Final Kills by alive players!"));
            if (event.getWinReason() == GameWinEvent.WinReason.LAST_ALIVE) player.sendMessage(ChatUtil.centerMessage("<gray>Last team standing!"));
            player.sendMessage(" ");
            player.sendMessage(ChatUtil.centerMessage(MessageUtil.MESSAGE_LINE.getStringMessage()));
        }
    }
}

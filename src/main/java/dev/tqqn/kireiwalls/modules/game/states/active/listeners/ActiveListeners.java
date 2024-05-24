package dev.tqqn.kireiwalls.modules.game.states.active.listeners;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.events.GamePlayerJoinEvent;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.database.models.PlayerStats;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.framework.game.events.GamePlayerKilledEvent;
import dev.tqqn.kireiwalls.framework.game.events.WitherDamageByPlayerEvent;
import dev.tqqn.kireiwalls.framework.teams.wither.GameWither;
import dev.tqqn.kireiwalls.modules.ModuleManager;
import dev.tqqn.kireiwalls.modules.arena.ArenaModule;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.ActiveState;
import dev.tqqn.kireiwalls.modules.game.states.active.board.ActiveBoard;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import dev.tqqn.kireiwalls.utils.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * The ActiveListeners class implements various event listeners for active gameplay.
 */
public final class ActiveListeners implements Listener {

    private final DatabaseModule databaseModule;

    public ActiveListeners() {
        ModuleManager moduleManager = KireiWalls.getInstance().getModuleManager();
        this.databaseModule = moduleManager.getModule(DatabaseModule.class);
    }

    /**
     * Handles the event when a player joins during active gameplay.
     *
     * @param event The GamePlayerJoinEvent instance
     */
    @EventHandler
    public void onJoin(GamePlayerJoinEvent event) {
        if (GameModule.getCurrentState().getGameStates() == GameStates.ACTIVE) {
            ScoreboardModule scoreboardModule = this.databaseModule.getPlugin().getModuleManager().getModule(ScoreboardModule.class);
            scoreboardModule.setScoreboard(event.getPlayerModel(), new ActiveBoard(event.getPlayerModel()));

            for (Player player : Bukkit.getOnlinePlayers()) {
                final PlayerModel players = PlayerModule.getPlayerModel(player.getUniqueId());

                if (players.isSpectatorMode()) {
                    if (event.getPlayerModel().isSpectatorMode()) {
                        event.getPlayerModel().getPlayer().showPlayer(KireiWalls.getInstance(), player);
                    } else {
                        event.getPlayerModel().getPlayer().hidePlayer(KireiWalls.getInstance(), player);
                    }
                }

                if (players.isSpectatorMode()) {
                    players.sendSpectatorTag(true);
                    continue;
                }

                players.getGameTeam().sendNameTag(players);
            }
        }
    }

    /**
     * Handles the event when a player is killed during active gameplay.
     *
     * @param event The GamePlayerKilledEvent instance
     */
    @EventHandler
    public void onPlayerDeath(GamePlayerKilledEvent event) {

        String broadcastMessage;

        switch (event.getDeathReason()) {
            case KILLED_BY_NO_PLAYER -> broadcastMessage = MessageUtil.KILLED_NO_KILLER.getStringMessage(event.getKilledPlayer().getGameTeam().getColor(), event.getKilledPlayer().getName());
            case KILLED_BY_PLAYER_BOW -> broadcastMessage = MessageUtil.KILLED_BY_KILLER_BOW.getStringMessage(event.getKilledPlayer().getGameTeam().getColor(), event.getKilledPlayer().getName(), event.getKiller().getGameTeam().getColor(), event.getKiller().getName());
            case KILLED_BY_PLAYER_HAND -> broadcastMessage = MessageUtil.KILLED_BY_KILLER_HAND.getStringMessage(event.getKilledPlayer().getGameTeam().getColor(), event.getKilledPlayer().getName(), event.getKiller().getGameTeam().getColor(), event.getKiller().getName());
            default -> broadcastMessage = "";
        }

        boolean isFinal = event.getKilledPlayer().getGameTeam().getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH;
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
            if (isFinal) {
                event.getKiller().getPlayerStats().increaseStat(PlayerStats.StatType.FINAL_KILLS);
            } else {
                event.getKiller().getPlayerStats().increaseStat(PlayerStats.StatType.KILLS);
            }
            event.getKiller().getPlayer().sendMessage(ChatUtil.format(MessageUtil.COINS_EARNED.getStringMessage(String.valueOf(coin)) + prefix));
            event.getKiller().increaseCoins(coin);
        }

        if (isFinal) {
            for (PlayerModel playerModel : GameModule.getIngamePlayers()) {
                if (event.getKiller() == playerModel) {
                    playerModel.getPlayer().sendMessage(ChatUtil.format(broadcastMessage));
                } else {
                    playerModel.getPlayer().sendMessage(ChatUtil.format(broadcastMessage));
                }
            }
        } else {
            event.getKilledPlayer().getPlayer().sendMessage(ChatUtil.format(broadcastMessage));
            if (event.getKiller() != null) {
                event.getKiller().getPlayer().sendMessage(ChatUtil.format(broadcastMessage));
            }
        }
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

        if (ActiveState.getCurrentCycle() == ActiveState.Cycle.PREPARE || ActiveState.getCurrentCycle() == ActiveState.Cycle.END) {
            event.setCancelled(true);
            return;
        }

        PlayerModel hitPlayerModel = PlayerModule.getPlayerModel(hitPlayer.getUniqueId());
        if (event.getDamager() instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player shooter) {
                if (shooter == hitPlayer) {
                    event.setCancelled(true);
                    projectile.remove();
                    return;
                }
                PlayerModel shooterModel = PlayerModule.getPlayerModel(shooter.getUniqueId());
                if (shooterModel.getGameTeam().equals(hitPlayerModel.getGameTeam())) {
                    event.setCancelled(true);
                }
            }
        }

        if (event.getDamager() instanceof Player hitter) {
            if (hitter == hitPlayer) return;
            PlayerModel hitterModel = PlayerModule.getPlayerModel(hitter.getUniqueId());
            if (hitterModel.getGameTeam().equals(hitPlayerModel.getGameTeam())) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Handles the event when a player respawns during active gameplay.
     *
     * @param event The PlayerRespawnEvent instance
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());

        if (playerModel.getGameTeam().getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH) {

            if (event.getPlayer().getKiller() == null) {
                event.setRespawnLocation(playerModel.getGameTeam().getGameTeamSettings().getSpawnLocation());
            } else {
                event.setRespawnLocation(event.getPlayer().getKiller().getLocation());
            }

            Bukkit.getScheduler().runTaskLater(KireiWalls.getInstance(), () -> playerModel.setSpectatorMode(true), 2L);
        } else {
            event.setRespawnLocation(playerModel.getGameTeam().getGameTeamSettings().getSpawnLocation());
            playerModel.setProtected(true);
            playerModel.getCurrentClass().applyKit(playerModel);
        }
    }

    /**
     * Handles the event when a wither damages a player during active gameplay.
     *
     * @param event The WitherDamageByPlayerEvent instance
     */
    @EventHandler
    public void onWitherDamage(WitherDamageByPlayerEvent event) {
        if (event.getAttacker().getGameTeam() == event.getWitherTeam()) {
            event.setCancelled(true);
            return;
        }

        event.getAttacker().addWitherDamage(event.getWitherTeam().getGameWither(), event.getDamage());

    }

    /**
     * Handles the event when an entity damages another entity causing death during active gameplay.
     *
     * @param event The EntityDamageByEntityEvent instance
     */
    @EventHandler
    public void onEntityDamageDeath(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player damagedPlayer) {

            if ((damagedPlayer.getHealth()) - event.getFinalDamage() <= 0) {

                final PlayerModel damagedPlayerModel = PlayerModule.getPlayerModel(damagedPlayer.getUniqueId());

                GamePlayerKilledEvent gamePlayerKilledEvent = null;

                if (event.getDamager() instanceof Projectile projectile) {
                    if (projectile.getShooter() instanceof Player damager) {
                        gamePlayerKilledEvent = new GamePlayerKilledEvent(damagedPlayerModel, PlayerModule.getPlayerModel(damager.getUniqueId()), GamePlayerKilledEvent.DeathReason.KILLED_BY_PLAYER_BOW);
                    } else {
                        gamePlayerKilledEvent = new GamePlayerKilledEvent(damagedPlayerModel, null, GamePlayerKilledEvent.DeathReason.KILLED_BY_NO_PLAYER);
                    }
                }

                if (event.getDamager() instanceof Player damager) {
                    gamePlayerKilledEvent = new GamePlayerKilledEvent(damagedPlayerModel, PlayerModule.getPlayerModel(damager.getUniqueId()), GamePlayerKilledEvent.DeathReason.KILLED_BY_PLAYER_HAND);
                }

                if (!(event.getDamager() instanceof Player)) {
                    if (!(event.getDamager() instanceof Projectile)) {
                        gamePlayerKilledEvent = new GamePlayerKilledEvent(damagedPlayerModel, null, GamePlayerKilledEvent.DeathReason.KILLED_BY_NO_PLAYER);
                    }
                }

                if (gamePlayerKilledEvent != null) {
                    Bukkit.getPluginManager().callEvent(gamePlayerKilledEvent);
                }

                Bukkit.getScheduler().runTaskLater(KireiWalls.getInstance(), () -> damagedPlayer.spigot().respawn(), 1L);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.getHealth() - event.getFinalDamage() > 0) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;
        GamePlayerKilledEvent gamePlayerKilledEvent = new GamePlayerKilledEvent(PlayerModule.getPlayerModel(event.getEntity().getUniqueId()), null, GamePlayerKilledEvent.DeathReason.KILLED_BY_NO_PLAYER);
        Bukkit.getPluginManager().callEvent(gamePlayerKilledEvent);
    }

    @EventHandler
    public void onPotionConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() != Material.POTION) return;
        Bukkit.getScheduler().runTaskLater(KireiWalls.getInstance(), () -> event.getPlayer().getInventory().remove(Material.GLASS_BOTTLE), 1L);
    }
}

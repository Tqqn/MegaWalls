package dev.tqqn.kireiwalls.modules.game.states.active;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.arena.ArenaModule;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.game.framework.AbstractGameState;
import dev.tqqn.kireiwalls.modules.game.framework.GameStates;
import dev.tqqn.kireiwalls.modules.game.framework.events.GameWinEvent;
import dev.tqqn.kireiwalls.modules.game.framework.events.WitherDeathEvent;
import dev.tqqn.kireiwalls.modules.game.states.active.runnables.HungerRunnable;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.modules.teams.framework.GameTeam;
import dev.tqqn.kireiwalls.modules.teams.framework.wither.GameWither;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.listeners.ActiveListeners;
import dev.tqqn.kireiwalls.modules.game.states.active.runnables.ActionBarRunnable;
import dev.tqqn.kireiwalls.modules.game.states.active.runnables.EnergyRunnable;
import dev.tqqn.kireiwalls.modules.teams.TeamModule;
import dev.tqqn.kireiwalls.utils.MessageUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The ActiveState class represents the active state of the game, managing various game cycles and timers.
 */
public final class ActiveState extends AbstractGameState {

    @Getter private static Cycle currentCycle;
    @Getter private static int cycleTimer;
    private int hungerTimer;

    private int witherDamageTimer;
    private boolean areWithersDead;
    private boolean initHealth;
    private boolean isAlreadyEnd;

    @Getter private HungerStage currentHungerStage;

    private final List<GameTeam> aliveTeams;

    private EnergyRunnable energyRunnable;
    private ActionBarRunnable actionBarRunnable;
    private HungerRunnable hungerRunnable;

    public ActiveState(GameModule gameModule) {
        super(gameModule, GameStates.ACTIVE, "Active");
        this.aliveTeams = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        this.getGameModule().shufflePlayers();
        aliveTeams.addAll(TeamModule.getGameTeams().values());
        //setTimer(3600);
        setTimer(1300);
        this.addListener(ActiveListeners.class);
        this.addListener(ActiveState.class);
        TeamModule.getGameTeams().values().forEach(GameTeam::spawnWither);
        currentCycle = ActiveState.Cycle.PREPARE;
        cycleTimer = 120;
        hungerTimer = 300;
        this.areWithersDead = false;
        this.initHealth = false;
        this.isAlreadyEnd = false;

        this.energyRunnable = new EnergyRunnable();
        this.actionBarRunnable = new ActionBarRunnable();

        this.energyRunnable.runTaskTimerAsynchronously(this.getGameModule().getPlugin(), 0, 5L);
        this.actionBarRunnable.runTaskTimerAsynchronously(this.getGameModule().getPlugin(), 0, 10L);
        this.runTaskTimerAsynchronously(this.getGameModule().getPlugin(), 0L, 20L);
        this.currentHungerStage = HungerStage.LEVEL_1;
        this.hungerRunnable = new HungerRunnable(this);
    }

    @Override
    public void onDisable() {
        this.energyRunnable.cancel();
        this.actionBarRunnable.cancel();
        this.hungerRunnable.cancel();
        this.cancel();
    }

    /**
     * Runs the game cycle.
     */
    public void run() {
        // Decrement timer
        --timer;

        // Handle cycle transitions and countdowns
        if (currentCycle == ActiveState.Cycle.PREPARE) {
            --cycleTimer;
            if (cycleTimer <= 0) {
                cycleTimer = 0;
                currentCycle = ActiveState.Cycle.PRE_DM;
                getGameModule().wallsFall();
                witherDamageTimer = 5;
                System.out.println("Walls fall");
            }
        }

        if (currentCycle == ActiveState.Cycle.COUNTDOWN_TO_DM) {
            --cycleTimer;

            if (cycleTimer != 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(MessageUtil.DM_COUNTDOWN.getMessage(String.valueOf(cycleTimer)));
                }
            }

            if (cycleTimer <= 0) {
                currentCycle = ActiveState.Cycle.DM;
            }
        }

        if (currentCycle == Cycle.PRE_DM) {
            witherDamageTimer--;
            if (witherDamageTimer == 0) {
                witherDamageTimer = 5;
                for (GameTeam gameTeam : TeamModule.getGameTeams().values()) {
                    if (gameTeam.getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH) continue;
                    if (!(gameTeam.getGameWither().getHealth()-2 >= 1)) continue;
                    gameTeam.getGameWither().damage(2);
                }
            }
        }

        // Initialize health on Tab during Deathmatch cycle
        if (currentCycle == Cycle.DM && !initHealth) {
            getGameModule().initHealthOnTab();
            initHealth = true;
            this.hungerRunnable.runTaskTimerAsynchronously(KireiWalls.getInstance(), 0, 20L);
        }

        // Start Deathmatch countdown when all withers are dead
        if (currentCycle != Cycle.DM && currentCycle != Cycle.END && this.getGameModule().areAllWithersDead() && !this.areWithersDead) {
            currentCycle = Cycle.COUNTDOWN_TO_DM;
            this.areWithersDead = true;
            cycleTimer = 10;

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(MessageUtil.ALL_WITHERS_DEAD.getMessage());
            }
        }

        if (currentCycle == Cycle.DM) {
            hungerTimer--;
        }

        if (hungerTimer == 0) {
            if (currentHungerStage.getNextHungerStage() == null) return;
            this.currentHungerStage = currentHungerStage.getNextHungerStage();
        }

        checkAliveTeams();

        if (!isAlreadyEnd && aliveTeams.size() == 1) {
            currentCycle = Cycle.END;
            this.isAlreadyEnd = true;
            Bukkit.getScheduler().runTask(KireiWalls.getInstance(), () -> {
                GameWinEvent gameWinEvent = new GameWinEvent(getGameModule().getWinningTeamByDraw(), GameWinEvent.WinReason.LAST_ALIVE);
                Bukkit.getPluginManager().callEvent(gameWinEvent);
                this.getGameModule().endGame();
            });
            return;
        }

        // End the game if timer runs out
        if (!isAlreadyEnd && timer <= 0) {
            currentCycle = Cycle.END;
            this.isAlreadyEnd = true;
            Bukkit.getScheduler().runTask(KireiWalls.getInstance(), () -> {
                GameWinEvent gameWinEvent = new GameWinEvent(getGameModule().getWinningTeamByDraw(), GameWinEvent.WinReason.DRAW);
                Bukkit.getPluginManager().callEvent(gameWinEvent);
                this.getGameModule().endGame();
            });
            System.out.println("Game has reached time limit, ending game...");
        }
    }

    public static void nextCycle() {
        cycleTimer = 0;
    }

    private void checkAliveTeams() {
        //aliveTeams.removeIf(GameTeam::checkAlivePlayersIfDeath);
    }

    @EventHandler
    public void onWitherDead(WitherDeathEvent event) {
        aliveTeams.remove(event.getGameWither().getGameTeam());
    }

    /**
     * Represents the various cycles of the game.
     */
    public enum Cycle {
        PREPARE,
        PRE_DM,
        COUNTDOWN_TO_DM,
        DM,
        END;
    }

    @Getter
    public enum HungerStage {
        LEVEL_1(5),
        LEVEL_2(8),
        LEVEL_3(10);

        private final int level;

        HungerStage(int level) {
            this.level = level;
        }

        @Nullable
        public HungerStage getNextHungerStage() {
            if (ordinal() == values().length - 1) return null;
            return values()[ordinal() + 1];
        }
    }
}

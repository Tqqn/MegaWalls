package dev.tqqn.kireiwalls.modules.game.states.active;

import dev.tqqn.kireiwalls.framework.game.AbstractGameState;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.framework.teams.GameTeam;
import dev.tqqn.kireiwalls.framework.teams.wither.GameWither;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.listeners.ActiveListeners;
import dev.tqqn.kireiwalls.modules.game.states.active.runnables.ActionBarRunnable;
import dev.tqqn.kireiwalls.modules.game.states.active.runnables.EnergyRunnable;
import dev.tqqn.kireiwalls.modules.teams.TeamModule;
import dev.tqqn.kireiwalls.utils.MessageUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * The ActiveState class represents the active state of the game, managing various game cycles and timers.
 */
public final class ActiveState extends AbstractGameState {
    @Getter
    private static Cycle currentCycle;
    @Getter
    private static int cycleTimer;
    private int witherDamageTimer;
    private boolean areWithersDead;
    private boolean initHealth;

    private EnergyRunnable energyRunnable;
    private ActionBarRunnable actionBarRunnable;

    public ActiveState(GameModule gameModule) {
        super(gameModule, GameStates.ACTIVE, "Active");
    }

    @Override
    public void onEnable() {
        this.getGameModule().shufflePlayers();
        setTimer(3600);
        this.addListener(ActiveListeners.class);
        TeamModule.getGameTeams().values().forEach(GameTeam::spawnWither);
        currentCycle = ActiveState.Cycle.PREPARE;
        cycleTimer = 30;
        this.areWithersDead = false;
        this.initHealth = false;
        this.energyRunnable = new EnergyRunnable(this.getGameModule());
        this.actionBarRunnable = new ActionBarRunnable(this.getGameModule());

        this.energyRunnable.runTaskTimerAsynchronously(this.getGameModule().getPlugin(), 0, 5L);
        this.actionBarRunnable.runTaskTimerAsynchronously(this.getGameModule().getPlugin(), 0, 10L);
        this.runTaskTimerAsynchronously(this.getGameModule().getPlugin(), 0L, 20L);
    }

    @Override
    public void onDisable() {
        this.energyRunnable.cancel();
        this.actionBarRunnable.cancel();
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
                    if (gameTeam.getGameWither().getWitherStatus() != GameWither.WitherStatus.DEATH) {
                        if (gameTeam.getGameWither().getHealth()-2 >= 1) {
                            gameTeam.getGameWither().damage(2);
                        }
                    }
                }
            }
        }

        // Initialize health on Tab during Deathmatch cycle
        if (currentCycle == Cycle.DM && !initHealth) {
            getGameModule().initHealthOnTab();
            initHealth = true;
        }

        // Start Deathmatch countdown when all withers are dead
        if (this.getGameModule().areAllWithersDead() && currentCycle != ActiveState.Cycle.DM && currentCycle != ActiveState.Cycle.END && !this.areWithersDead) {
            currentCycle = ActiveState.Cycle.COUNTDOWN_TO_DM;
            this.areWithersDead = true;
            cycleTimer = 10;

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(MessageUtil.ALL_WITHERS_DEAD.getMessage());
            }

            System.out.println("DM Countdown started.");
        }

        // End the game if timer runs out
        if (timer <= 0) {
            currentCycle = ActiveState.Cycle.END;
            this.getGameModule().endGame();
        }

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
}

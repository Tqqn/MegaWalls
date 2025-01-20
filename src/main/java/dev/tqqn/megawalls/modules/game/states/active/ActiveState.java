package dev.tqqn.megawalls.modules.game.states.active;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.game.framework.AbstractGameState;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.game.framework.events.GameWinEvent;
import dev.tqqn.megawalls.modules.game.framework.events.WitherDeathEvent;
import dev.tqqn.megawalls.modules.game.states.active.board.ActiveBoard;
import dev.tqqn.megawalls.modules.game.states.active.objects.ActiveStateData;
import dev.tqqn.megawalls.modules.game.states.active.runnables.HungerRunnable;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.modules.scoreboard.ScoreboardModule;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import dev.tqqn.megawalls.modules.teams.framework.wither.GameWither;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.states.active.listeners.ActiveListeners;
import dev.tqqn.megawalls.modules.game.states.active.runnables.ActionBarRunnable;
import dev.tqqn.megawalls.modules.game.states.active.runnables.EnergyRunnable;
import dev.tqqn.megawalls.modules.teams.TeamModule;
import dev.tqqn.megawalls.utils.MessageUtil;
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

    @Getter private final ActiveStateData activeStateData = new ActiveStateData();

    @Getter private Cycle currentCycle = Cycle.PREPARE;
    @Getter private int cycleTimer = 120;
    private int hungerTimer = 300;

    private int witherDamageTimer = 0;

    @Getter private HungerStage currentHungerStage = HungerStage.LEVEL_0;

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

        setTimer(3600);

        this.register(new ActiveListeners(getGameModule()));
        this.register(this);

        getGameModule().spawnWithers();

        this.runTaskTimerAsynchronously(this.getGameModule().getPlugin(), 0L, 20L);

        this.energyRunnable = new EnergyRunnable();
        this.actionBarRunnable = new ActionBarRunnable();

        this.energyRunnable.runTaskTimerAsynchronously(this.getGameModule().getPlugin(), 0, 5L);
        this.actionBarRunnable.runTaskTimerAsynchronously(this.getGameModule().getPlugin(), 0, 10L);

        this.currentHungerStage = HungerStage.LEVEL_0;
        this.hungerRunnable = new HungerRunnable(this);
        activateHunger();

        enableScoreboard();
    }

    @Override
    public void onDisable() {
        disableHunger();
        disableEnergyRunnable();
        disableActionBarRunnable();
        cancel();
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
                setCycle(Cycle.PRE_DM);
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
                setCycle(Cycle.DM);
            }
        }

        if (currentCycle == Cycle.PRE_DM) {
            witherDamageTimer--;
            if (witherDamageTimer <= 0) {
                witherDamageTimer = 5;
                for (GameTeam gameTeam : TeamModule.getGameTeams().values()) {
                    if (gameTeam.getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH) continue;
                    if (!(gameTeam.getGameWither().getHealth()-2 >= 1)) continue;
                    gameTeam.getGameWither().damage(2);
                }
            }
        }

        if (currentCycle == Cycle.DM) hungerTimer--;

        // Start Deathmatch countdown when all withers are dead
        if (currentCycle != Cycle.DM && currentCycle != Cycle.END && this.getGameModule().areAllWithersDead() && !activeStateData.isAllWitherDeath()) {
            setCycle(Cycle.COUNTDOWN_TO_DM);
        }

        if (hungerTimer == 0) {
            if (currentHungerStage.getNextHungerStage() != null) {
                currentHungerStage = currentHungerStage.getNextHungerStage();
            }
        }

        checkAliveTeams();

        if (!getActiveStateData().isHasEnded() && aliveTeams.size() <= 1) {
            setCycle(Cycle.END);
            return;
        }

        // End the game if timer runs out
        if (!getActiveStateData().isHasEnded() && timer <= 0) {
            setCycle(Cycle.END);
        }
    }

    private void enableScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ScoreboardModule scoreboardModule = MegaWalls.getInstance().getModuleManager().getModule(ScoreboardModule.class);
            scoreboardModule.setScoreboard(PlayerModule.getPlayerModel(player.getUniqueId()), new ActiveBoard(PlayerModule.getPlayerModel(player.getUniqueId())));
        }
    }

    public boolean setCycle(Cycle newCycle) {
        switch (newCycle) {
            case PREPARE -> {
                enableWitherGod();
                if (currentCycle != Cycle.PRE_DM && currentCycle != Cycle.DM) return false;
                getGameModule().teleportPlayersToSpawn();
                getGameModule().undoWallsFall();
            }

            case PRE_DM -> {
                disableWitherGod();
                getGameModule().wallsFall();
                witherDamageTimer = 5;
            }

            case COUNTDOWN_TO_DM -> {
                activeStateData.setAllWitherDeath(true);
                cycleTimer = 10;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(MessageUtil.ALL_WITHERS_DEAD.getMessage());
                }
            }

            case DM -> {
                if (!activeStateData.isInitHealth()) {
                    getGameModule().initHealthOnTab();
                    activeStateData.setInitHealth(true);
                }
            }

            case END -> {
                activeStateData.setHasEnded(true);

                callEndGameEvent(getWinReason());
                cancel();
            }
        }

        this.currentCycle = newCycle;
        return true;
    }

    private void checkAliveTeams() {
        aliveTeams.removeIf((team) -> team.getAlivePlayers().isEmpty());
    }

    private void enableWitherGod() {
        for (GameTeam gameTeam : TeamModule.getGameTeams().values()) {
            gameTeam.getGameWither().setGod(true);
        }
    }

    private void disableWitherGod() {
        for (GameTeam gameTeam : TeamModule.getGameTeams().values()) {
            gameTeam.getGameWither().setGod(false);
        }
    }

    private void activateHunger() {
        currentHungerStage = HungerStage.LEVEL_1;
        hungerRunnable.runTaskTimerAsynchronously(MegaWalls.getInstance(), 0, 20L);
    }

    private void disableHunger() {
        if (hungerRunnable.getTaskId() == 0) return;
        hungerRunnable.cancel();
    }

    private void disableEnergyRunnable() {
        if (energyRunnable.getTaskId() == 0) return;
        energyRunnable.cancel();
    }

    private void disableActionBarRunnable() {
        if (actionBarRunnable.getTaskId() == 0) return;
        actionBarRunnable.cancel();
    }

    private void callEndGameEvent(GameWinEvent.WinReason winReason) {
        Bukkit.getScheduler().runTask(MegaWalls.getInstance(), () -> {
            GameWinEvent gameWinEvent = new GameWinEvent(getGameModule().getWinningTeamByDraw(), winReason);
            Bukkit.getPluginManager().callEvent(gameWinEvent);
        });
    }

    private GameWinEvent.WinReason getWinReason() {
        if (getActiveStateData().isHasEnded()) return null;
        if (timer <= 0) return GameWinEvent.WinReason.DRAW;
        if (aliveTeams.size() <= 1) return GameWinEvent.WinReason.LAST_ALIVE;

        return null;
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
        LEVEL_0(0),
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

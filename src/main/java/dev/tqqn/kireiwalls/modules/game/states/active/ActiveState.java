package dev.tqqn.kireiwalls.modules.game.states.active;

import dev.tqqn.kireiwalls.framework.game.AbstractGameState;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.listeners.ActiveListeners;
import dev.tqqn.kireiwalls.modules.game.teams.TeamModule;
import dev.tqqn.kireiwalls.utils.MessageUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActiveState extends AbstractGameState {
    @Getter
    private static Cycle currentCycle;
    @Getter
    private static int cycleTimer;
    private boolean areWithersDead;
    private boolean initHealth;

    public ActiveState(GameModule gameModule) {
        super(gameModule, GameStates.ACTIVE, "Active");
    }

    public void onEnable() {
        this.getGameModule().shufflePlayers();
        setTimer(3600);
        this.addListener(ActiveListeners.class);
        TeamModule.getGameTeams().values().forEach(GameTeam::spawnWither);
        currentCycle = ActiveState.Cycle.PREPARE;
        cycleTimer = 10;
        this.areWithersDead = false;
        this.initHealth = false;
        this.runTaskTimerAsynchronously(this.getGameModule().getPlugin(), 0L, 20L);
    }

    public void onDisable() {
        this.cancel();
    }

    public void run() {
        --timer;
        if (currentCycle == ActiveState.Cycle.PREPARE) {
            --cycleTimer;
            if (cycleTimer <= 0) {
                cycleTimer = 0;
                currentCycle = ActiveState.Cycle.PRE_DM;
                getGameModule().wallsFall();
                System.out.println("Walls fall");
            }
        }

        if (currentCycle == ActiveState.Cycle.COUNTDOWN_TO_DM) {
            --cycleTimer;

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(MessageUtil.DM_COUNTDOWN.getMessage(String.valueOf(cycleTimer)));
            }

            if (cycleTimer <= 0) {
                currentCycle = ActiveState.Cycle.DM;
            }
        }

        if (currentCycle == Cycle.DM && !initHealth) {
            getGameModule().initHealthOnTab();
            initHealth = true;
        }

        if (this.getGameModule().areAllWithersDead() && currentCycle != ActiveState.Cycle.DM && currentCycle != ActiveState.Cycle.END && !this.areWithersDead) {
            currentCycle = ActiveState.Cycle.COUNTDOWN_TO_DM;
            this.areWithersDead = true;
            cycleTimer = 10;

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(MessageUtil.ALL_WITHERS_DEAD.getMessage());
            }

            System.out.println("DM Countdown started.");
        }

        if (timer <= 0) {
            currentCycle = ActiveState.Cycle.END;
            this.getGameModule().endGame();
        }

    }

    public enum Cycle {
        PREPARE,
        PRE_DM,
        COUNTDOWN_TO_DM,
        DM,
        END;
    }
}

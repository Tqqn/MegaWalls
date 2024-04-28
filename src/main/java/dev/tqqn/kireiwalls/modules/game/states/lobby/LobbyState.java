package dev.tqqn.kireiwalls.modules.game.states.lobby;

import dev.tqqn.kireiwalls.framework.game.AbstractGameState;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.lobby.listeners.LobbyListeners;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LobbyState extends AbstractGameState {
    private boolean canStart = false;

    public LobbyState(GameModule gameModule) {
        super(gameModule, GameStates.WAITING, "Lobby");
    }

    public void run() {
        --timer;
        if (this.getGameModule().canStart() && timer <= 0) {
            this.cancel();
            this.getGameModule().setGameState(GameStates.ACTIVE);
        } else {
            if (this.getGameModule().canStart()) {
                if (!this.canStart) {
                    timer = 50;
                    this.canStart = true;

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ChatUtil.format("<red>Game is beginning in <green>" + timer + " <red>seconds."));
                    }
                }

                if (timer <= 5) {

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ChatUtil.format("<red>Game is beginning in <green>" + timer + " <red>seconds."));
                    }
                }

            } else if (this.canStart) {
                this.canStart = false;
                timer = 180;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatUtil.format("<red>Not enough players. Setting timer to <green>3 <red>minutes."));
                }
            }

            if (timer == 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatUtil.format("<red>Not enough players. Setting timer to <green>3 <red>minutes."));
                }
            }

        }
    }

    public void onEnable() {
        setTimer(this.getGameModule().getGameSettings().getLobbyCount());
        this.addListener(LobbyListeners.class);
        this.runTaskTimer(this.getGameModule().getPlugin(), 0L, 20L);
    }

    public void onDisable() {
        this.cancel();
    }
}

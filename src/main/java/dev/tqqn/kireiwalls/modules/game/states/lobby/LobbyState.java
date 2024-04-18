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
        super(gameModule, GameStates.WAITING, 300,"Lobby");

    }

    @Override
    public void run() {
        if (getGameModule().canStart() && timer <= 0) {
            cancel();
            getGameModule().setGameState(GameStates.ACTIVE);
            return;
        }

        if (getGameModule().canStart()) {
            if (!canStart) {
                timer = 50;
                canStart = true;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatUtil.format("<red>Game is beginning in <green>" + timer + " <red>seconds."));
                }
            }

            if (timer <= 5) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatUtil.format("<red>Game is beginning in <green>" + timer + " <red>seconds."));
                }
            }
        } else {
            if (canStart) {
                canStart = false;
                timer = 180;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatUtil.format("<red>Not enough players. Setting timer to <green>3 <red>minutes."));
                }
            }
        }

        if (timer == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatUtil.format("<red>Not enough players. Setting timer to <green>3 <red>minutes."));
            }
        }
        timer--;
    }

    @Override
    public void onEnable() {
        addListener(LobbyListeners.class);
        this.runTaskTimer(getGameModule().getPlugin(), 0L, 20L);
    }

    @Override
    public void onDisable() {
        this.cancel();
    }
}

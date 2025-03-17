package dev.tqqn.megawalls.modules.game.states.lobby;

import dev.tqqn.megawalls.modules.game.framework.AbstractGameState;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.states.lobby.listeners.LobbyListeners;
import dev.tqqn.megawalls.common.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * The LobbyState class represents the waiting state of the game in the lobby.
 */
public final class LobbyState extends AbstractGameState {
    private boolean canStart = false;

    public LobbyState(GameModule gameModule) {
        super(gameModule, GameStates.WAITING, "Lobby");
    }

    @Override
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

    @Override
    public void onEnable() {
        setTimer(300);
        register(new LobbyListeners());
        this.runTaskTimer(this.getGameModule().getPlugin(), 0L, 20L);
    }

    @Override
    public void onDisable() {
        this.cancel();
    }
}

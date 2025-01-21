package dev.tqqn.megawalls.modules.game.framework.events;

import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public final class GameWinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final GameTeam winner;
    private final WinReason winReason;

    public GameWinEvent(GameTeam winner, WinReason winReason) {
        this.winner = winner;
        this.winReason = winReason;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Enumerates the possible reasons for a player's death.
     */
    public enum WinReason {
        DRAW,
        LAST_ALIVE;
    }
}

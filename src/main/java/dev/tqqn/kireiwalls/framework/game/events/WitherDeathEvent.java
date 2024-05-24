package dev.tqqn.kireiwalls.framework.game.events;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.teams.wither.GameWither;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * The WitherDeathEvent class represents an event that occurs when the wither dies.
 * It provides information about the killer player and the game wither.
 */
public final class WitherDeathEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    @Getter private final PlayerModel killer;
    @Getter private final GameWither gameWither;

    /**
     * Constructs a WitherDeathEvent object with the specified parameters.
     *
     * @param killer The player model of the killer.
     * @param gameWither The game wither associated with the event.
     */
    public WitherDeathEvent(PlayerModel killer, GameWither gameWither) {
        this.killer = killer;
        this.gameWither = gameWither;
    }

    @Override
    public @NotNull HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}

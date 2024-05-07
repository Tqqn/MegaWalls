package dev.tqqn.kireiwalls.framework.database.events;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * The GamePlayerJoinEvent class represents an event that occurs when a player joins a game.
 * This event is fired when a player's data model is created upon joining a game.
 * !! Use this event instead of the PlayerJoinEvent. As this guarantees a PlayerModel Object.
 */
public final class GamePlayerJoinEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Getter private final PlayerModel playerModel;

    private boolean cancelled = false;

    public GamePlayerJoinEvent(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    @Override
    public @NotNull HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}

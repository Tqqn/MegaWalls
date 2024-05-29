package dev.tqqn.kireiwalls.modules.game.framework.events;

import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The GamePlayerKilledEvent class represents an event where a player is killed in the game.
 * It provides information about the killed player, the killer (if any), and the reason for death.
 */
@Getter
public final class GamePlayerKilledEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final PlayerModel killedPlayer;
    @Nullable private final PlayerModel killer;
    private final DeathReason deathReason;

    /**
     * Constructs a GamePlayerKilledEvent object with the specified parameters.
     *
     * @param killedPlayer The player model of the killed player.
     * @param killer The player model of the killer, if any.
     * @param deathReason The reason for the player's death.
     */
    public GamePlayerKilledEvent(PlayerModel killedPlayer, @Nullable PlayerModel killer, DeathReason deathReason) {
        this.killedPlayer = killedPlayer;
        this.killer = killer;
        this.deathReason = deathReason;
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
    public enum DeathReason {
        KILLED_BY_PLAYER_HAND,
        KILLED_PLAYER_ABILITY,
        KILLED_BY_PLAYER_BOW,
        KILLED_BY_NO_PLAYER;
    }
}

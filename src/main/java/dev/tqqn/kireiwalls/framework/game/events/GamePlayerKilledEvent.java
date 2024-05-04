package dev.tqqn.kireiwalls.framework.game.events;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class GamePlayerKilledEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final PlayerModel killedPlayer;
    @Nullable private final PlayerModel killer;
    private final DeathReason deathReason;

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

    public enum DeathReason {
        KILLED_BY_PLAYER_HAND,
        KILLED_PLAYER_ABILITY,
        KILLED_BY_PLAYER_BOW,
        KILLED_BY_NO_PLAYER;
    }
}

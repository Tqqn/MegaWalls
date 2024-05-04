package dev.tqqn.kireiwalls.framework.game.events;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class GamePlayerRespawnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final PlayerModel killedPlayer;

    public GamePlayerRespawnEvent(PlayerModel killedPlayer) {
        this.killedPlayer = killedPlayer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

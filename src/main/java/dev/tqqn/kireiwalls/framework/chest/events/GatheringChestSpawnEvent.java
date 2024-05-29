package dev.tqqn.kireiwalls.framework.chest.events;

import dev.tqqn.kireiwalls.framework.chest.ChestItem;
import dev.tqqn.kireiwalls.framework.chest.types.GatheringChest;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public final class GatheringChestSpawnEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final PlayerModel playerModel;
    private final GatheringChest gatheringChest;

    public GatheringChestSpawnEvent(PlayerModel playerModel, GatheringChest gatheringChest) {
        this.playerModel = playerModel;
        this.gatheringChest = gatheringChest;
    }

    public void addItem(ChestItem chestItem) {
        gatheringChest.addChestItem(chestItem);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

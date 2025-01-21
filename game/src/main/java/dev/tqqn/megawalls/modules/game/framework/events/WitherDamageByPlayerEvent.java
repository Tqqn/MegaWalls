package dev.tqqn.megawalls.modules.game.framework.events;

import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * The WitherDamageByPlayerEvent class represents an event where a player damages the wither.
 * It provides information about the wither's team and the attacking player.
 */
public final class WitherDamageByPlayerEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Getter private final GameTeam witherTeam;
    @Getter private final PlayerModel attacker;
    @Getter private final int damage;

    private boolean cancelled = false;

    /**
     * Constructs a WitherDamageByPlayerEvent object with the specified parameters.
     *
     * @param witherTeam The team that the wither belongs to.
     * @param attacker The player model of the attacker.
     */
    public WitherDamageByPlayerEvent(GameTeam witherTeam, PlayerModel attacker, int damage) {
        this.witherTeam = witherTeam;
        this.attacker = attacker;
        this.damage = damage;
    }

    @Override
    public @NotNull HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public boolean isCancelled() { return cancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}

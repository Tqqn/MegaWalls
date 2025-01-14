package dev.tqqn.megawalls.modules.database.framework.models;

import dev.tqqn.megawalls.modules.player.data.TempPlayerData;
import dev.tqqn.megawalls.modules.database.drivers.mongo.MongoItem;
import dev.tqqn.megawalls.modules.database.drivers.mongo.MongoObject;
import dev.tqqn.megawalls.utils.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The PlayerModel class represents a decorator for player-related data and functionality.
 * It encapsulates attributes and methods related to player statistics, game state, and behavior.
 */
@Getter
@MongoItem("players")
public final class PlayerModel extends MongoObject<UUID> {

    private final UUID uuid;
    @Setter private String name;

    private final transient TempPlayerData tempPlayerData;
    /**
     * Constructs a PlayerModel object with the specified UUID, name, and player statistics.
     *
     * @param uuid        The UUID of the player.
     * @param name        The name of the player.
     * @param playerStats The statistics of the player.
     */
    public PlayerModel(UUID uuid, String name, PlayerStats playerStats) {
        super(uuid);
        this.uuid = uuid;
        this.name = name;
        this.tempPlayerData = new TempPlayerData(this, playerStats);
    }

    /**
     * Constructs a PlayerModel object with the specified UUID and name.
     *
     * @param uuid The UUID of the player.
     * @param name The name of the player.
     */
    public PlayerModel(UUID uuid, String name) {
        this(uuid, name, new PlayerStats());
    }

    /**
     * Retrieves the Bukkit Player object associated with this player model.
     *
     * @return The Bukkit Player object, or null if the player is offline.
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid) : null;
    }

    public NamedTextColor getChatColor() {
        if (this.getPlayer() == null) return null;
        if (this.getPlayer().hasPermission("mw.admin")) {
            return NamedTextColor.WHITE;
        }
        return NamedTextColor.GRAY;
    }

    public String getRank() {
        if (this.getPlayer() == null) return "";
        if (this.getPlayer().hasPermission("mw.admin")) {
            return MessageUtil.ADMIN_PREFIX.getStringMessage() + " ";
        }

        return "<gray>";
    }
}

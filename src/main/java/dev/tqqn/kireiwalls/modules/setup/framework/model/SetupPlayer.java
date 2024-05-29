package dev.tqqn.kireiwalls.modules.setup.framework.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public final class SetupPlayer {

    private final UUID uuid;
    @Setter private boolean isBuild;

    @Setter private Location selectionOne;
    @Setter private Location selectionTwo;

    public SetupPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.isBuild = false;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid) == null ? null : Bukkit.getPlayer(uuid);
    }
 }

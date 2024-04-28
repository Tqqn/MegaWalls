package dev.tqqn.kireiwalls.modules.game;

import lombok.Getter;
import org.bukkit.Location;

@Getter
public class GameSettings {
    private final String mapName;
    private final Location lobbyLocation;
    private final int lobbyCount;

    public GameSettings(String mapName, Location lobbyLocation, int lobbyCount) {
        this.mapName = mapName;
        this.lobbyLocation = lobbyLocation;
        this.lobbyCount = lobbyCount;
    }
}

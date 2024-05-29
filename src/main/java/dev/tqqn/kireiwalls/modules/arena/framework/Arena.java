package dev.tqqn.kireiwalls.modules.arena.framework;

import dev.tqqn.kireiwalls.modules.database.drivers.mongo.MongoItem;
import dev.tqqn.kireiwalls.modules.database.drivers.mongo.MongoObject;
import lombok.Getter;

@Getter
@MongoItem("maps")
public final class Arena extends MongoObject<String> {

    private final String mapName;
    private final String mapPrettyName;

    private final ArenaSettings arenaSettings;

    public Arena(String mapName, String mapPrettyName, ArenaSettings arenaSettings) {
        super(mapName);
        this.mapName = mapName;
        this.mapPrettyName = mapPrettyName;
        this.arenaSettings = arenaSettings;
    }
}

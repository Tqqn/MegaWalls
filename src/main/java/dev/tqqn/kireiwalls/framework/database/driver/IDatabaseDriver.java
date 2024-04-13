package dev.tqqn.kireiwalls.framework.database.driver;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;

import java.util.UUID;

public interface IDatabaseDriver {

    void connect(String database, String host, String port);

    void createPlayerTemplate(UUID uuid, String name);

    void savePlayer(PlayerModel playerModel);
}

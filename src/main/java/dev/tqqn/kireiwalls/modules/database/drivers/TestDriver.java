package dev.tqqn.kireiwalls.modules.database.drivers;

import dev.tqqn.kireiwalls.framework.database.driver.IDatabaseDriver;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;

import java.util.UUID;

public class TestDriver implements IDatabaseDriver {
    public TestDriver() {
    }

    public void connect(String database, String host, String port) {
    }

    public void createPlayerTemplate(UUID uuid, String name) {
    }

    public void savePlayer(PlayerModel playerModel) {
    }
}

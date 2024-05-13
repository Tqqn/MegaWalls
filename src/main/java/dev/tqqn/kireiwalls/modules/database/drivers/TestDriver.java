package dev.tqqn.kireiwalls.modules.database.drivers;

import dev.tqqn.kireiwalls.framework.database.driver.IDatabaseDriver;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.arena.ArenaSettings;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeamSettings;
import dev.tqqn.kireiwalls.framework.region.Cuboid;
import org.bukkit.Location;

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

    @Override
    public ArenaSettings getArenaSettings(String mapName) {
        return null;
    }

    @Override
    public GameTeamSettings getTeamSettings(String mapName, String teamName) {
        return null;
    }

    @Override
    public void saveArenaSettings(ArenaSettings arenaSettings) {

    }

    @Override
    public void saveTeamSettings(String teamName, GameTeamSettings gameTeamSettings) {

    }

    @Override
    public Location getLocation(String mapName, String key) {
        return null;
    }

    @Override
    public Cuboid getCuboid(String mapName, String key) {
        return null;
    }
}

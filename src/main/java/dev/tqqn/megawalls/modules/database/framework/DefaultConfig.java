package dev.tqqn.megawalls.modules.database.framework;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.region.framework.Cuboid;
import dev.tqqn.megawalls.modules.database.DatabaseModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * The DefaultConfig class provides methods to access configuration settings
 * related to the game environment and setup.
 */
public final class DefaultConfig {

    private final MegaWalls plugin;

    /**
     * Constructs a DefaultConfig object using the specified DatabaseModule.
     * Initializes the plugin instance and saves the default configuration if not already present.
     *
     * @param databaseModule The DatabaseModule instance.
     */
    public DefaultConfig(DatabaseModule databaseModule) {
        plugin = databaseModule.getPlugin();
        plugin.saveDefaultConfig();
    }

    public boolean isSetupMode() {
        return plugin.getConfig().getBoolean("setup-mode");
    }

    public int getLobbyTimer() {
        return plugin.getConfig().getInt("game.lobby-timer");
    }

    public Location getLobbyLocation() {
        return new Location(Bukkit.getWorld(plugin.getConfig().getString("game.lobby-location.world")),
                plugin.getConfig().getDouble("game.lobby-location.x"),
                plugin.getConfig().getDouble("game.lobby-location.y"),
                plugin.getConfig().getDouble("game.lobby-location.z"),
                (float) plugin.getConfig().getDouble("game.lobby-location.yaw"),
                (float) plugin.getConfig().getDouble("game.lobby-location.pitch")
        );
    }

    public Location getTeamSpawnLocation(String team) {
        return new Location(Bukkit.getWorld(plugin.getConfig().getString("teams." + team + ".spawn-location.world")),
                plugin.getConfig().getDouble("teams." + team + ".spawn-location.x"),
                plugin.getConfig().getDouble("teams." + team + ".spawn-location.y"),
                plugin.getConfig().getDouble("teams." + team + ".spawn-location.z"),
                (float) plugin.getConfig().getDouble("teams." + team + ".spawn-location.yaw"),
                (float) plugin.getConfig().getDouble("teams." + team + ".spawn-location.pitch"));
    }

    public Location getTeamWitherLocation(String team) {
        return new Location(Bukkit.getWorld(plugin.getConfig().getString("teams." + team + ".wither-location.world")),
                plugin.getConfig().getDouble("teams." + team + ".wither-location.x"),
                plugin.getConfig().getDouble("teams." + team + ".wither-location.y"),
                plugin.getConfig().getDouble("teams." + team + ".wither-location.z"),
                (float) plugin.getConfig().getDouble("teams." + team + ".wither-location.yaw"),
                (float) plugin.getConfig().getDouble("teams." + team + ".wither-location.pitch"));
    }

    public Cuboid getCuboid(String path) {
        return new Cuboid(
                new Location(Bukkit.getWorld(plugin.getConfig().getString(path + ".point-1.world")),
                        plugin.getConfig().getDouble( path + ".point-1.x"),
                        plugin.getConfig().getDouble(path + ".point-1.y"),
                        plugin.getConfig().getDouble(path + ".point-1.z")),
                new Location(Bukkit.getWorld(plugin.getConfig().getString(path + ".point-2.world")),
                        plugin.getConfig().getDouble(path + ".point-2.x"),
                        plugin.getConfig().getDouble(path + ".point-2.y"),
                        plugin.getConfig().getDouble(path + ".point-2.z")));
    }

    public Cuboid getTeamProtectionCuboid(String team) {
        return getCuboid("teams." + team + ".protection-region");
    }

    public Cuboid getMiddleCuboid() {
        return getCuboid("game.middle");
    }

    public Cuboid[] getWallCuboids() {
        List<Cuboid> cuboids = new ArrayList<>();
        int size = plugin.getConfig().getConfigurationSection("game.walls").getKeys(false).size();
        System.out.println(size);

        for (int i = 1; i < size+1; i++ ) {
            cuboids.add(getCuboid("game.walls.wall-" + i));
        }
        return cuboids.toArray(Cuboid[]::new);
    }



    public Cuboid getTeamWitherCuboid(String team) {
        return getCuboid("teams." + team + ".wither-region");
    }
}

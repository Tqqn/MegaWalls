package dev.tqqn.kireiwalls.framework.database;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.region.Cuboid;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DefaultConfig {

    private final KireiWalls plugin;

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
                (float) plugin.getConfig().getDouble("teams." + team + ".spawn-location.pitch")
                );
    }

    public Location getTeamWitherLocation(String team) {
        return new Location(Bukkit.getWorld(plugin.getConfig().getString("teams." + team + ".wither-location.world")),
                plugin.getConfig().getDouble("teams." + team + ".wither-location.x"),
                plugin.getConfig().getDouble("teams." + team + ".wither-location.y"),
                plugin.getConfig().getDouble("teams." + team + ".wither-location.z"),
                (float) plugin.getConfig().getDouble("teams." + team + ".wither-location.yaw"),
                (float) plugin.getConfig().getDouble("teams." + team + ".wither-location.pitch")
        );
    }

    public Cuboid getTeamProtectionCuboid(String team) {
        return new Cuboid(
                new Location(Bukkit.getWorld(plugin.getConfig().getString("teams." + team + ".protection-region.point-1.world")),
                        plugin.getConfig().getDouble("teams." + team + ".protection-region.point-1.x"),
                        plugin.getConfig().getDouble("teams." + team + ".protection-region.point-1.y"),
                        plugin.getConfig().getDouble("teams." + team + ".protection-region.point-1.z")),
                new Location(Bukkit.getWorld(plugin.getConfig().getString("teams." + team + ".protection-region.point-2.world")),
                        plugin.getConfig().getDouble("teams." + team + ".protection-region.point-2.x"),
                        plugin.getConfig().getDouble("teams." + team + ".protection-region.point-2.y"),
                        plugin.getConfig().getDouble("teams." + team + ".protection-region.point-2.z"))
        );
    }

    public Cuboid getTeamWitherCuboid(String team) {
        return new Cuboid(
                new Location(Bukkit.getWorld(plugin.getConfig().getString("teams." + team + ".wither-region.point-1.world")),
                        plugin.getConfig().getDouble("teams." + team + ".wither-region.point-1.x"),
                        plugin.getConfig().getDouble("teams." + team + ".wither-region.point-1.y"),
                        plugin.getConfig().getDouble("teams." + team + ".wither-region.point-1.z")),
                new Location(Bukkit.getWorld(plugin.getConfig().getString("teams." + team + ".wither-region.point-2.world")),
                        plugin.getConfig().getDouble("teams." + team + ".wither-region.point-2.x"),
                        plugin.getConfig().getDouble("teams." + team + ".wither-region.point-2.y"),
                        plugin.getConfig().getDouble("teams." + team + ".wither-region.point-2.z"))
        );
    }
}

package dev.tqqn.kireiwalls.modules.region.framework;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * The Cuboid class represents a cuboid region in the game world defined by two diagonal points.
 * It provides various methods to work with and manipulate cuboids.
 * @Author: https://www.spigotmc.org/threads/region-cuboid.329859/
 */
public final class Cuboid {

    private final int xMin;
    private final int xMax;
    private final int yMin;
    private final int yMax;
    private final int zMin;
    private final int zMax;
    @Getter private final World world;

    /**
     * Constructs a Cuboid object with two diagonal points defining the boundaries of the cuboid.
     *
     * @param point1 The first diagonal point.
     * @param point2 The second diagonal point.
     */
    public Cuboid(final Location point1, final Location point2) {
        this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
        this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
        this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
        this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
        this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
        this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());
        this.world = point1.getWorld();
    }

    public Location getPoint1() {
        return new Location(this.world, this.xMin, this.yMin, this.zMin);
    }

    public Location getPoint2() {
        return new Location(this.world, this.xMax, this.yMax, this.zMax);
    }

    public boolean isIn(final Location loc) {
        return loc.getWorld() == this.world && loc.getBlockX() >= this.xMin && loc.getBlockX() <= this.xMax && loc.getBlockY() >= this.yMin && loc.getBlockY() <= this.yMax && loc
                .getBlockZ() >= this.zMin && loc.getBlockZ() <= this.zMax;
    }

    public List<Location> getHollowCube(double particleDistance) {
        List<Location> result = new ArrayList<>();
        World world = getPoint1().getWorld();
        double minX = Math.min(getPoint1().getX(), getPoint2().getX());
        double minY = Math.min(getPoint1().getY(), getPoint2().getY());
        double minZ = Math.min(getPoint1().getZ(), getPoint2().getZ());
        double maxX = Math.max(getPoint1().getX(), getPoint2().getX());
        double maxY = Math.max(getPoint1().getY(), getPoint2().getY());
        double maxZ = Math.max(getPoint1().getZ(), getPoint2().getZ());

        for (double x = minX; x <= maxX; x+=particleDistance) {
            for (double y = minY; y <= maxY; y+=particleDistance) {
                for (double z = minZ; z <= maxZ; z+=particleDistance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }
                }
            }
        }

        return result;
    }
}
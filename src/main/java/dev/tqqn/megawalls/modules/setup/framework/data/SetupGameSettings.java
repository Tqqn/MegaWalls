package dev.tqqn.megawalls.modules.setup.framework.data;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.arena.framework.Arena;
import dev.tqqn.megawalls.modules.arena.framework.ArenaSettings;
import dev.tqqn.megawalls.modules.region.framework.Cuboid;
import dev.tqqn.megawalls.modules.database.DatabaseModule;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public final class SetupGameSettings implements SetupSavable {

    private Location selectionPoint1;
    private Location selectionPoint2;

    private Location lobbyLocation;

    private List<Cuboid> wallCuboids;
    private Cuboid middle;

    public SetupGameSettings() {
        this.wallCuboids = new ArrayList<>();
    }

    @Override
    public boolean save(Player player) {

        if (lobbyLocation == null || wallCuboids == null || middle == null) return false;

        DatabaseModule databaseModule = MegaWalls.getInstance().getModuleManager().getModule(DatabaseModule.class);
        Arena arena = new Arena(player.getLocation().getWorld().getName(), player.getLocation().getWorld().getName(), new ArenaSettings(lobbyLocation, wallCuboids.toArray(new Cuboid[0]), middle));
        databaseModule.getMongoDriver().saveAsync(arena);
        return true;
    }

}

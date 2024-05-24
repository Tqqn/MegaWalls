package dev.tqqn.kireiwalls.modules.arena;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.arena.Arena;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.utils.WorldUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import java.io.IOException;
import java.util.logging.Level;

@Getter
public final class ArenaModule extends AbstractModule {

    private Arena currentArena;
    private DatabaseModule databaseModule;

    public ArenaModule(KireiWalls plugin) {
        super(plugin, "Arena");
    }

    @Override
    protected void onLoad() {
        databaseModule = getPlugin().getModuleManager().getModule(DatabaseModule.class);
        WorldUtils.asyncCopy("DRAGONKEEP");
    }

    @Override
    public void onEnable() {
        new WorldCreator("temp_DRAGONKEEP").createWorld();
        currentArena = databaseModule.getMongoDriver().read(Arena.class, "DRAGONKEEP");
        currentArena.getArenaSettings().initRegion();
        Bukkit.getScheduler().runTask(KireiWalls.getInstance(), () -> Bukkit.unloadWorld("world", false));
    }

    @Override
    public void onDisable() {
        Bukkit.unloadWorld("temp_" + currentArena.getMapName(), false);
        System.out.println(currentArena.getMapName());

        try {
            WorldUtils.deleteWorld("temp_" + currentArena.getMapName());
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not delete temp world.");
        }
    }

    public void switchMap(String newMapName) {
        Arena oldArena = currentArena;
        try {
            currentArena = databaseModule.getMongoDriver().read(Arena.class, newMapName);
        } catch (NullPointerException e) {
            currentArena = oldArena;
        }
    }
}

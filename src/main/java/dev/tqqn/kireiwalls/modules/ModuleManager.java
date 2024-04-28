package dev.tqqn.kireiwalls.modules;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.modules.classes.ClassModule;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.teams.TeamModule;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.modules.region.RegionModule;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModuleManager {
    private final Map<Class<? extends AbstractModule>, AbstractModule> modules = new LinkedHashMap<>();

    public ModuleManager(KireiWalls plugin) {
        this.modules.put(DatabaseModule.class, new DatabaseModule(plugin));
        this.modules.put(PlayerModule.class, new PlayerModule(plugin));
        this.modules.put(GameModule.class, new GameModule(plugin, (DatabaseModule)this.modules.get(DatabaseModule.class)));
        this.modules.put(RegionModule.class, new RegionModule(plugin));
        this.modules.put(TeamModule.class, new TeamModule(plugin));
        this.modules.put(ClassModule.class, new ClassModule(plugin));
        this.modules.put(ScoreboardModule.class, new ScoreboardModule(plugin));
    }

    public void init() {
        this.registerModules();
    }

    public void disable() {
        this.unregisterModules();
    }

    private void registerModules() {
        this.modules.forEach((key, value) -> {
            value.enable();
        });
    }

    private void unregisterModules() {
        this.modules.forEach((key, value) -> {
            value.disable();
        });
        this.modules.clear();
    }

    public AbstractModule getModule(Class<? extends AbstractModule> moduleClass) {
        return this.modules.get(moduleClass);
    }
}

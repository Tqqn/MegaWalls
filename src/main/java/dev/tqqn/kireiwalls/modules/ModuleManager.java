package dev.tqqn.kireiwalls.modules;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.teams.TeamModule;
import dev.tqqn.kireiwalls.modules.menu.MenuModule;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.modules.region.RegionModule;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModuleManager {

    private final Map<Class<? extends AbstractModule>, AbstractModule> modules;

    public ModuleManager(KireiWalls plugin) {
        modules = new LinkedHashMap<>();
        modules.put(DatabaseModule.class, new DatabaseModule(plugin));
        modules.put(PlayerModule.class, new PlayerModule(plugin));
        modules.put(GameModule.class, new GameModule(plugin, (DatabaseModule) modules.get(DatabaseModule.class)));
        modules.put(RegionModule.class, new RegionModule(plugin));
        modules.put(TeamModule.class, new TeamModule(plugin));
        modules.put(MenuModule.class, new MenuModule(plugin));
        modules.put(ScoreboardModule.class, new ScoreboardModule(plugin));
    }

    public void init() {
        registerModules();
    }

    public void disable() {
        unregisterModules();
    }

    private void registerModules() {
        modules.forEach((key, value) -> value.enable());
    }

    private void unregisterModules() {
        modules.forEach((key, value) -> value.disable());
        modules.clear();
    }

    public AbstractModule getModule(Class<? extends AbstractModule> moduleClass) {
        return modules.get(moduleClass);
    }
}

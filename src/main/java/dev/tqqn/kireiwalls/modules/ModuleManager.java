package dev.tqqn.kireiwalls.modules;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.modules.arena.ArenaModule;
import dev.tqqn.kireiwalls.modules.chest.ChestModule;
import dev.tqqn.kireiwalls.modules.classes.ClassModule;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.teams.TeamModule;
import dev.tqqn.kireiwalls.modules.menu.MenuModule;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.modules.region.RegionModule;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;
import dev.tqqn.kireiwalls.modules.setup.SetupModule;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The ModuleManager class manages the initialization and disabling of modules within the plugin.
 */
public final class ModuleManager {

    private final Map<Class<? extends AbstractModule>, AbstractModule> modules = new LinkedHashMap<>();

    public ModuleManager(KireiWalls plugin) {
        if (plugin.isSetup()) {
            this.modules.put(DatabaseModule.class, new DatabaseModule(plugin));
            this.modules.put(MenuModule.class, new MenuModule(plugin));
            this.modules.put(SetupModule.class, new SetupModule(plugin));
            return;
        }
        this.modules.put(DatabaseModule.class, new DatabaseModule(plugin));
        this.modules.put(ArenaModule.class, new ArenaModule(plugin));
        this.modules.put(PlayerModule.class, new PlayerModule(plugin));
        this.modules.put(GameModule.class, new GameModule(plugin, (DatabaseModule) this.modules.get(DatabaseModule.class)));
        this.modules.put(RegionModule.class, new RegionModule(plugin));
        this.modules.put(TeamModule.class, new TeamModule(plugin));
        this.modules.put(ClassModule.class, new ClassModule(plugin));
        this.modules.put(ScoreboardModule.class, new ScoreboardModule(plugin));
        this.modules.put(MenuModule.class, new MenuModule(plugin));
        this.modules.put(ChestModule.class, new ChestModule(plugin));
    }

    public void load() {
        this.modules.values().forEach(AbstractModule::load);
    }

    /**
     * Initializes all modules.
     */
    public void init() {
        this.registerModules();
    }

    /**
     * Disables all modules.
     */
    public void disable() {
        this.unregisterModules();
    }

    /**
     * Registers all modules by enabling them.
     */
    private void registerModules() {
        this.modules.values().forEach(AbstractModule::enable);
    }

    /**
     * Unregisters all modules by disabling them and clearing the module map.
     */
    private void unregisterModules() {
        this.modules.values().forEach(AbstractModule::disable);
        this.modules.clear();
    }

    /**
     * Retrieves a module instance by its class.
     *
     * @param moduleClass The class of the module to retrieve.
     * @return The module instance.
     */

    public <M extends AbstractModule> M getModule(Class<M> moduleClass) {
        return moduleClass.cast(this.modules.get(moduleClass));
    }
}

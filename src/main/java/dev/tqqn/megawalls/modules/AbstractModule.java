package dev.tqqn.megawalls.modules;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import dev.tqqn.megawalls.MegaWalls;
import lombok.Getter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The AbstractModule class represents a module in the plugin.
 * It provides methods to enable, disable, and manage listeners and commands for the module.
 */
public abstract class AbstractModule {

    @Getter private final MegaWalls plugin;
    @Getter private PaperCommandManager commandManager;
    @Getter private final ModuleLogger logger;

    private final Set<Listener> activeListeners = new HashSet<>();
    private final Set<BaseCommand> commands = new HashSet<>();

    @Getter private final String name;

    /**
     * Constructs a new AbstractModule object with the specified plugin and name.
     *
     * @param plugin The main plugin instance.
     * @param name   The name of the module.
     */
    public AbstractModule(MegaWalls plugin, String name) {
        this.plugin = plugin;
        this.logger = new ModuleLogger(plugin, name);
        this.name = name;
    }

    /**
     * Loads the module.
     */
    public void load() {
        logger.log(Level.INFO, "Is loading...");
        onLoad();
        logger.log(Level.INFO, "Finished loading!");
    }

    /**
     * Enables the module by registering listeners and commands.
     */
    public void enable() {
        logger.log(Level.INFO, "Is enabling...");
        this.commandManager = plugin.getCommandManager();
        onEnable();
        registerListeners();
        registerCommands();
        logger.log(Level.INFO, "Finished enabling!");
    }

    /**
     * Disables the module by unregistering listeners and commands.
     */
    public void disable() {
        logger.log(Level.INFO, "Is disabling...");
        onDisable();
        unRegisterListeners();
        logger.log(Level.INFO, "Finished disabling!");
    }

    /**
     * Called when the module is being loaded.
     */
    protected void onLoad() {
    }

    /**
     * Called when the module is being enabled.
     */
    protected void onEnable() {
    }

    /**
     * Called when the module is being disabled.
     */
    protected void onDisable() {
    }

    public void register(Object object) {
        if (object instanceof Listener listener) {
            activeListeners.add(listener);
            return;
        }

        if (object instanceof BaseCommand baseCommand) {
            commands.add(baseCommand);
        }
    }

    /**
     * Registers listeners for the module.
     */
    private void registerListeners() {
        if (activeListeners.isEmpty()) return;
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        activeListeners.forEach(listener -> {
            pluginManager.registerEvents(listener, plugin);
            logger.log(Level.INFO, "Has registered listener: " + listener.getClass());
        });
    }

    /**
     * Registers commands for the module.
     */
    private void registerCommands() {
        if (commands.isEmpty()) return;
        commands.forEach(baseCommand -> {
            commandManager.registerCommand(baseCommand);
            logger.log(Level.INFO, "Has registered command: " + baseCommand.getName());
        });
    }

    /**
     * Unregisters listeners for the module.
     */
    private void unRegisterListeners() {
        if (activeListeners.isEmpty()) return;
        activeListeners.forEach(listener -> {
            HandlerList.unregisterAll(listener);
            plugin.getLogger().info("Has unregistered listener: " + listener.getClass());
        });
    }

    public static class ModuleLogger extends Logger {

        ModuleLogger(Plugin plugin, String prefix) {
            super("Mega Walls - Module - " + prefix, null);
            setParent(plugin.getLogger());
            setLevel(Level.ALL);
        }

        @Override
        public void log(Level level, String message) {
            super.log(level, message);
        }
    }
}

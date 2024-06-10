package dev.tqqn.kireiwalls.modules;

import dev.tqqn.kireiwalls.KireiWalls;
import lombok.Getter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The AbstractModule class represents a module in the plugin.
 * It provides methods to enable, disable, and manage listeners and commands for the module.
 */
public abstract class AbstractModule {

    @Getter private final KireiWalls plugin;

    private final Set<Listener> listeners = new HashSet<>();
    private final Map<String, CommandExecutor> commands = new HashMap<>();

    @Getter private final String name;
    @Getter private final String prefix;

    /**
     * Constructs a new AbstractModule object with the specified plugin and name.
     *
     * @param plugin The main plugin instance.
     * @param name   The name of the module.
     */
    public AbstractModule(KireiWalls plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.prefix = "Module: " + name;
    }

    protected void onLoad() {
        // Empty Constructor to override!
    }

    /**
     * Called when the module is being enabled.
     */
    protected void onEnable() {
        // Empty Constructor to override!
    }

    /**
     * Called when the module is being disabled.
     */
    protected void onDisable() {
        // Empty Constructor to override!
    }

    public void load() {
        plugin.getLogger().info(prefix + " is loading...");
        onLoad();
        plugin.getLogger().info(prefix + " finished loading!");
    }

    /**
     * Enables the module by registering listeners and commands.
     */
    public void enable() {
        plugin.getLogger().info(prefix + " is enabling...");
        onEnable();
        registerListeners();
        registerCommands();
        plugin.getLogger().info(prefix + " finished enabling!");
    }

    /**
     * Disables the module by unregistering listeners and commands.
     */
    public void disable() {
        plugin.getLogger().info(prefix + " is disabling...");
        onDisable();
        unRegisterListeners();
        plugin.getLogger().info(prefix + " finished disabling!");
    }

    /**
     * Registers listeners for the module.
     */
    private void registerListeners() {
        if (listeners.isEmpty()) return;
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        listeners.forEach(listener -> {
            pluginManager.registerEvents(listener, plugin);
            plugin.getLogger().info(prefix + " has registered listener: " + listener);
        });
    }

    /**
     * Registers commands for the module.
     */
    private void registerCommands() {
        if (commands.isEmpty()) return;
        commands.forEach((commandString, command) ->{
            plugin.getCommand(commandString).setExecutor(command);
            plugin.getLogger().info(prefix + " has registered command: " + commandString);
        });
    }

    /**
     * Unregisters listeners for the module.
     */
    private void unRegisterListeners() {
        if (listeners.isEmpty()) return;
        listeners.forEach(listener -> {
            HandlerList.unregisterAll(listener);
            plugin.getLogger().info(prefix + " has unregistered listener: " + listener);
        });
    }

    public void addComponent(Class<?> clazz) {
        addComponent(clazz, "");
    }

    /**
     * Adds a component (listener or command) to the module.
     *
     * @param clazz The class representing the component.
     * @param name  The name of the component.
     */
    public void addComponent(Class<?> clazz, String name) {
        if (Listener.class.isAssignableFrom(clazz)) {
            try {
                Listener listener = (Listener) clazz.getConstructor().newInstance();
                listeners.add(listener);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                getPlugin().getLogger().info(prefix + " unable to register listener: " + clazz.getName());
                e.printStackTrace();
            }
        }

        if (CommandExecutor.class.isAssignableFrom(clazz)) {
            try {
                CommandExecutor command = (CommandExecutor) clazz.getConstructor().newInstance();
                commands.put(name, command);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                getPlugin().getLogger().info(prefix + " unable to register command: " + clazz.getName());
                e.printStackTrace();
            }

        }
    }
}

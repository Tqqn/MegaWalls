package dev.tqqn.kireiwalls.framework;

import dev.tqqn.kireiwalls.KireiWalls;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractModule {

    @Getter private final KireiWalls plugin;

    private final Set<Listener> listeners = new HashSet<>();
    private final Map<String, CommandExecutor> commands = new HashMap<>();

    @Getter private final String name;
    @Getter private final String prefix;

    public AbstractModule(KireiWalls plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.prefix = "Module: " + name;
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public void enable() {
        plugin.getLogger().info(prefix + " is loading...");
        onEnable();
        registerListeners();
        registerCommands();
        plugin.getLogger().info(prefix + " finished loading!");
    }

    public void disable() {
        plugin.getLogger().info(prefix + " is disabling...");
        onDisable();
        unRegisterListeners();
        plugin.getLogger().info(prefix + " finished disabling!");
    }

    private void registerListeners() {
        if (listeners.isEmpty()) return;
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        listeners.forEach(listener -> {
            pluginManager.registerEvents(listener, plugin);
            plugin.getLogger().info(prefix + " has registered listener: " + listener);
        });
    }

    private void registerCommands() {
        if (commands.isEmpty()) return;
        commands.forEach((commandString, command) ->{
            plugin.getCommand(commandString).setExecutor(command);
            plugin.getLogger().info(prefix + " has registered command: " + commandString);
        });
    }

    private void unRegisterListeners() {
        if (listeners.isEmpty()) return;
        listeners.forEach(listener -> {
            HandlerList.unregisterAll(listener);
            plugin.getLogger().info(prefix + " has unregistered listener: " + listener);
        });
    }

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

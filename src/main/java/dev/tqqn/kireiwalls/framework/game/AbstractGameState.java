package dev.tqqn.kireiwalls.framework.game;

import dev.tqqn.kireiwalls.modules.game.GameModule;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractGameState extends BukkitRunnable {

    @Getter private final GameModule gameModule;
    @Getter private final GameStates gameStates;
    @Getter private final String name;
    @Getter protected static int timer;

    private final Set<Listener> listeners;

    public AbstractGameState(GameModule gameModule, GameStates gameStates, int count, String name) {
        this.gameModule = gameModule;
        this.gameStates = gameStates;
        timer = count;
        this.name = name;
        this.listeners = new HashSet<>();
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public void enable() {
        getGameModule().getPlugin().getLogger().info("State: " + name + " is loading...");
        onEnable();
        registerListeners();
        getGameModule().getPlugin().getLogger().info("State: " + name + " finished loading!");
    }

    public void disable() {
        getGameModule().getPlugin().getLogger().info("State: " + name + " is disabling...");
        onDisable();
        unRegisterListeners();
        getGameModule().getPlugin().getLogger().info("State: " + name + " finished disabling!");
    }

    private void registerListeners() {
        if (listeners.isEmpty()) return;
        PluginManager pluginManager = getGameModule().getPlugin().getServer().getPluginManager();
        listeners.forEach(listener -> {
            pluginManager.registerEvents(listener, getGameModule().getPlugin());
            getGameModule().getPlugin().getLogger().info("State: " + name + " has registered listener: " + listener);
        });
    }

    private void unRegisterListeners() {
        if (listeners.isEmpty()) return;
        listeners.forEach(listener -> {
            HandlerList.unregisterAll(listener);
            getGameModule().getPlugin().getLogger().info("State: " + name + " has unregistered listener: " + listener);
        });
    }

    public void addListener(Class<?> clazz) {
        if (Listener.class.isAssignableFrom(clazz)) {
            try {
                Listener listener = (Listener) clazz.getConstructor().newInstance();
                listeners.add(listener);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                gameModule.getPlugin().getLogger().info(gameModule.getPlugin().getPrefix() + " unable to register listener: " + clazz.getName());
            }
        }
    }

}

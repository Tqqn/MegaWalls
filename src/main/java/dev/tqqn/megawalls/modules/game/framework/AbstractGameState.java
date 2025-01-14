package dev.tqqn.megawalls.modules.game.framework;

import dev.tqqn.megawalls.modules.game.GameModule;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * The AbstractGameState class represents the abstract base class for game states, providing
 * common functionality for enabling, disabling, and managing listeners.
 */
public abstract class AbstractGameState extends BukkitRunnable implements Listener {

    @Getter private final GameModule gameModule;
    @Getter private final GameStates gameStates;
    @Getter private final String name;
    @Setter @Getter protected static int timer;

    private final Set<Listener> listeners;

    /**
     * Constructs an AbstractGameState object with the specified attributes.
     *
     * @param gameModule The GameModule associated with the state.
     * @param gameStates The game state.
     * @param name The name of the game state.
     */
    public AbstractGameState(GameModule gameModule, GameStates gameStates, String name) {
        this.gameModule = gameModule;
        this.gameStates = gameStates;
        this.name = name;
        this.listeners = new HashSet<>();
    }

    public abstract void onEnable();
    public abstract void onDisable();

    /** Enables the game state. */
    public void enable() {
        getGameModule().getPlugin().getLogger().info("State: " + name + " is loading...");
        onEnable();
        registerListeners();
        getGameModule().getPlugin().getLogger().info("State: " + name + " finished loading!");
    }

    /** Disables the game state. */
    public void disable() {
        getGameModule().getPlugin().getLogger().info("State: " + name + " is disabling...");
        onDisable();
        unRegisterListeners();
        getGameModule().getPlugin().getLogger().info("State: " + name + " finished disabling!");
    }

    /** Registers the listeners for the state. */
    private void registerListeners() {
        if (listeners.isEmpty()) return;
        PluginManager pluginManager = getGameModule().getPlugin().getServer().getPluginManager();
        listeners.forEach(listener -> {
            pluginManager.registerEvents(listener, getGameModule().getPlugin());
            getGameModule().getPlugin().getLogger().info("State: " + name + " has registered listener: " + listener);
        });
    }

    /** Unregisters the listeners for the state. */
    private void unRegisterListeners() {
        if (listeners.isEmpty()) return;
        listeners.forEach(listener -> {
            HandlerList.unregisterAll(listener);
            getGameModule().getPlugin().getLogger().info("State: " + name + " has unregistered listener: " + listener);
        });
    }

    /**
     * Adds a listener to the state.
     *
     * @param clazz The class of the listener to add.
     */
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

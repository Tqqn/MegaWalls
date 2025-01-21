package dev.tqqn.megawalls;

import co.aikar.commands.PaperCommandManager;
import dev.tqqn.megawalls.modules.ModuleManager;
import dev.tqqn.megawalls.nms.ReflectionLayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents the main class of the MegaWalls plugin.
 */
@Getter
public final class MegaWalls extends JavaPlugin {

    @Getter private static MegaWalls instance;
    private PaperCommandManager commandManager;

    private ModuleManager moduleManager;
    @Getter private static ReflectionLayer reflectionLayer;

    @Getter private static final String prefix = "[MegaWalls] ";
    private boolean isSetup;

    @Override
    public void onLoad() {
        instance = this;
        isSetup = getConfig().getBoolean("setup");
        moduleManager = new ModuleManager(this);
        moduleManager.load();
    }

    @Override
    public void onEnable() {
        commandManager = new PaperCommandManager(this);

        moduleManager.init();

        if (isSetup) return;

        findReflectionLayer();
    }

    @Override
    public void onDisable() {
        moduleManager.disable();
    }

    /**
     * Finds the appropriate reflection layer for the server version.
     */
    private void findReflectionLayer() {
        String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName();
        String version = bukkitVersion.substring(bukkitVersion.lastIndexOf('.') + 1);
        try {
            Class<?> nmsClass = Class.forName("dev.tqqn.megawalls.nms." + version + "." + version);
            Bukkit.getLogger().info("Using reflection layer for version " + version);
            reflectionLayer = (ReflectionLayer) nmsClass.getConstructors()[0].newInstance();
        } catch (Exception ignored) {
            Bukkit.getLogger().info("This version is not supported - " + version);
            Bukkit.getServer().shutdown();
        }
    }
}

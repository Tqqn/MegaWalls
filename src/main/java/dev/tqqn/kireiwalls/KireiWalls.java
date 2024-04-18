package dev.tqqn.kireiwalls;

import dev.tqqn.kireiwalls.modules.ModuleManager;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.nms.ReflectionLayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


@Getter
public final class KireiWalls extends JavaPlugin {

    @Getter
    private static KireiWalls instance;
    private ModuleManager moduleManager;
    @Getter private static ReflectionLayer reflectionLayer;

    private final String prefix = "[KireiWalls] ";

    @Override
    public void onLoad() {
        moduleManager = new ModuleManager(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        moduleManager.init();
        findReflectionLayer();
        initScoreboardTask();
    }

    @Override
    public void onDisable() {
        moduleManager.disable();
    }

    private void findReflectionLayer() {
        String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName();
        String version = bukkitVersion.substring(bukkitVersion.lastIndexOf('.') + 1);
        try {
            Class<?> nmsClass = Class.forName("dev.tqqn.kireiwalls.nms." + version + "." + version);
            Bukkit.getLogger().info("Using reflection layer for version " + version);
            reflectionLayer = (ReflectionLayer) nmsClass.getConstructors()[0].newInstance(this);
        } catch (Exception ignored) {
            Bukkit.getLogger().info("This version is not supported - " + version);
            Bukkit.getServer().shutdown();
        }
    }

    private void initScoreboardTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (PlayerModule.getPlayerModel(player.getUniqueId()).getCurrentScoreboard() == null) return;
                PlayerModule.getPlayerModel(player.getUniqueId()).getCurrentScoreboard().update();
            }
        }, 0, 10L);
    }
}

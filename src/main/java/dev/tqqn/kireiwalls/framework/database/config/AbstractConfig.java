package dev.tqqn.kireiwalls.framework.database.config;

import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AbstractConfig {

    @Getter private final DatabaseModule databaseModule;

    private final String configName;
    private File customConfigFile;

    @Getter private FileConfiguration fileConfiguration;

    public AbstractConfig(DatabaseModule databaseModule, String configName) {
        this.databaseModule = databaseModule;
        this.configName = configName;

        loadConfig();
    }

    private void loadConfig() {
        customConfigFile = new File(databaseModule.getPlugin().getDataFolder(), configName);
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            databaseModule.getPlugin().saveResource(configName, false);
        }

        fileConfiguration = new YamlConfiguration();

        try {
            fileConfiguration.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    public void saveCustomConfig() {
        try {
            fileConfiguration.save(customConfigFile);
        } catch (IOException ignored) {}
    }

    public void saveValueToConfig(String path, Object value) {
        fileConfiguration.set(path, value);
    }
}


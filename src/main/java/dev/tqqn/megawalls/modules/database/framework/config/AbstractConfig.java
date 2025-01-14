package dev.tqqn.megawalls.modules.database.framework.config;

import dev.tqqn.megawalls.modules.database.DatabaseModule;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * AbstractConfig provides a base class for handling configurations in a plugin, facilitating loading, saving, and manipulation of configuration files.
 */
public class AbstractConfig {

    @Getter private final DatabaseModule databaseModule;

    private final String configName;
    private File customConfigFile;

    @Getter private FileConfiguration fileConfiguration;

    /**
     * Constructs an AbstractConfig object.
     *
     * @param databaseModule The database module to associate with this configuration.
     * @param configName     The name of the configuration file.
     */
    public AbstractConfig(DatabaseModule databaseModule, String configName) {
        this.databaseModule = databaseModule;
        this.configName = configName;

        loadConfig();
    }

    /**
     * Loads the configuration file.
     * If the file does not exist, it will be created with default values.
     */
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
            getDatabaseModule().getLogger().log(Level.SEVERE, exception.getMessage());
        }
    }

    /**
     * Saves the custom configuration file.
     */
    public void saveCustomConfig() {
        try {
            fileConfiguration.save(customConfigFile);
        } catch (IOException ignored) {}
    }

    /**
     * Saves a value to the configuration file.
     *
     * @param path  The path within the configuration file to save the value.
     * @param value The value to save.
     */
    public void saveValueToConfig(String path, Object value) {
        fileConfiguration.set(path, value);
    }
}


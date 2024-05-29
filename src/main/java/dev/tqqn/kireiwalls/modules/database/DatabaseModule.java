package dev.tqqn.kireiwalls.modules.database;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.database.DefaultConfig;
import dev.tqqn.kireiwalls.framework.database.listeners.PlayerLoadListeners;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.database.drivers.MongoDriver;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * The DatabaseModule class extends AbstractModule and represents a module responsible for database-related operations.
 * It manages connections to the database, player data storage, and other database-related tasks.
 */

@Getter
public final class DatabaseModule extends AbstractModule {

    private MongoDriver mongoDriver;
    private DefaultConfig defaultConfig;
    private PlayerModule playerModule;

    public DatabaseModule(KireiWalls plugin) {
        super(plugin, "Database");
    }

    @Override
    protected void onLoad() {
        this.mongoDriver = new MongoDriver(this);
        this.defaultConfig = new DefaultConfig(this);
        this.mongoDriver.connect("mw", "dev-toon-mongodb-1", "27017");
    }

    @Override
    protected void onEnable() {
        if (getPlugin().isSetup()) return;

        this.addComponent(PlayerLoadListeners.class, "");

        this.playerModule = this.getPlugin().getModuleManager().getModule(PlayerModule.class);
    }

    @Override
    protected void onDisable() {
        Bukkit.getOnlinePlayers().forEach((player) -> {
            Bukkit.getLogger().info("Saving player: " + player.getName() + "!");
            savePlayer(PlayerModule.getPlayerModel(player.getUniqueId()));
            player.kick();
            Bukkit.getLogger().info("Finished saving player: " + player.getName() + ". Kicked player from the server.");
        });
    }

    /**
     * Saves player data to the database asynchronously.
     *
     * @param playerModel The PlayerModel containing the player data to be saved.
     */
    public void savePlayer(PlayerModel playerModel) {
        this.mongoDriver.saveAsync(playerModel);
    }

    /**
     * Retrieves a PlayerModel from the database using the UUID and name of the player.
     *
     * @param uuid The UUID of the player.
     * @param name The name of the player.
     * @return The PlayerModel object containing the player data.
     */
    public PlayerModel getPlayer(UUID uuid, String name) {
        return new PlayerModel(uuid, name);
    }
}

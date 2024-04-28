package dev.tqqn.kireiwalls.modules.database;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.database.DefaultConfig;
import dev.tqqn.kireiwalls.framework.database.driver.IDatabaseDriver;
import dev.tqqn.kireiwalls.framework.database.listeners.PlayerLoadListeners;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.database.drivers.TestDriver;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public final class DatabaseModule extends AbstractModule {
    private DefaultConfig defaultConfig;
    private final IDatabaseDriver databaseDriver = new TestDriver();
    private PlayerModule playerModule;

    public DatabaseModule(KireiWalls plugin) {
        super(plugin, "Database");
    }

    public void onEnable() {
        this.addComponent(PlayerLoadListeners.class, "");
        this.databaseDriver.connect("players", "dev-toon-mongodb-1", "27017");
        this.defaultConfig = new DefaultConfig(this);
        this.playerModule = (PlayerModule)this.getPlugin().getModuleManager().getModule(PlayerModule.class);
    }

    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach((player) -> {
            Bukkit.getLogger().info("Saving player: " + player.getName() + "!");
            savePlayer(PlayerModule.getPlayerModel(player.getUniqueId()));
            player.kick();
            Bukkit.getLogger().info("Finished saving player: " + player.getName() + ". Kicked player from the server.");
        });
    }

    public void createPlayerTemplate(UUID uuid, String name) {
        CompletableFuture.runAsync(() -> {
            this.databaseDriver.createPlayerTemplate(uuid, name);
        });
    }

    public void savePlayer(PlayerModel playerModel) {
        CompletableFuture.runAsync(() -> {
            this.databaseDriver.savePlayer(playerModel);
        });
    }

    public PlayerModel getPlayer(UUID uuid, String name) {
        return new PlayerModel(uuid, name);
    }

}

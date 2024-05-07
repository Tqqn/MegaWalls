package dev.tqqn.kireiwalls.framework.database.driver;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;

import java.util.UUID;

/**
 * The IDatabaseDriver interface defines methods for interacting with a database driver.
 * Implementations of this interface are responsible for establishing connections to databases,
 * creating player templates, and saving player models.
 */
public interface IDatabaseDriver {

    /**
     * Connects to the specified database using the provided host and port.
     *
     * @param database The name of the database to connect to.
     * @param host     The host address of the database server.
     * @param port     The port number on which the database server is listening.
     */
    void connect(String database, String host, String port);

    /**
     * Creates a player template in the database based on the given UUID and name.
     *
     * @param uuid The UUID of the player.
     * @param name The name of the player.
     */
    void createPlayerTemplate(UUID uuid, String name);

    /**
     * Saves the provided PlayerModel object to the database.
     *
     * @param playerModel The PlayerModel object to save.
     */
    void savePlayer(PlayerModel playerModel);
}

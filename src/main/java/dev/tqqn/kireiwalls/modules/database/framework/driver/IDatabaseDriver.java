package dev.tqqn.kireiwalls.modules.database.framework.driver;

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
}

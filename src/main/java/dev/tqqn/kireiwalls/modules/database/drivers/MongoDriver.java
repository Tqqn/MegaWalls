package dev.tqqn.kireiwalls.modules.database.drivers;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dev.tqqn.kireiwalls.framework.database.driver.IDatabaseDriver;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.database.models.PlayerStats;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.UUID;

/**
 * The MongoDriver class implements the IDatabaseDriver interface and provides functionality
 * for connecting to and interacting with a MongoDB database.
 */
@Getter
public final class MongoDriver implements IDatabaseDriver {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> playerCollection;
    private final DatabaseModule databaseModule;

    public MongoDriver(DatabaseModule databaseModule) {
        this.databaseModule = databaseModule;
    }

    /**
     * Initializes the player collection in the MongoDB database.
     * If the collection does not exist, it creates a new one.
     */
    public void initPlayerCollection() {
        try {
            this.mongoDatabase.createCollection("players");
        } catch (MongoCommandException ignored) {
        }

        this.playerCollection = this.mongoDatabase.getCollection("players");
    }

    @Override
    public void connect(String database, String host, String port) {
        this.mongoClient = new MongoClient(new MongoClientURI("mongodb://" + host + ":" + port));
        this.mongoDatabase = this.mongoClient.getDatabase(database);
        this.initPlayerCollection();
    }

    @Override
    public void createPlayerTemplate(UUID uuid, String name) {
        Document statsDocument = new Document();
        statsDocument.put("kills", 0);
        statsDocument.put("assists", 0);
        statsDocument.put("deaths", 0);
        statsDocument.put("final_kills", 0);
        statsDocument.put("final_assists", 0);
        statsDocument.put("final_deaths", 0);
        statsDocument.put("wins", 0);
        statsDocument.put("losses", 0);
        statsDocument.put("wither_damage", 0);
        Document playerDocument = new Document();
        playerDocument.put("_id", uuid.toString());
        playerDocument.put("uuid", uuid.toString());
        playerDocument.put("name", name);
        playerDocument.append("stats", statsDocument);
        this.playerCollection.insertOne(playerDocument);
    }

    @Override
    public void savePlayer(PlayerModel playerModel) {
        PlayerStats playerStats = playerModel.getPlayerStats();
        Bson playerUpdates = Updates.set("name", playerModel.getName());
        Bson statsUpdates = Updates.combine(Updates.set("stats.kills", playerStats.getStat(PlayerStats.StatType.KILLS)), Updates.set("stats.assists", playerStats.getStat(PlayerStats.StatType.ASSISTS)), Updates.set("stats.deaths", playerStats.getStat(PlayerStats.StatType.DEATHS)), Updates.set("stats.final_kills", playerStats.getStat(PlayerStats.StatType.FINAL_KILLS)), Updates.set("stats.final_assists", playerStats.getStat(PlayerStats.StatType.FINAL_ASSISTS)), Updates.set("stats.final_deaths", playerStats.getStat(PlayerStats.StatType.FINAL_DEATH)), Updates.set("stats.wins", 1), Updates.set("stats.losses", 1), Updates.set("stats.wither_damage", 1));
        this.playerCollection.updateOne(Filters.eq("_id", playerModel.getUuid().toString()), playerUpdates);
        this.playerCollection.updateOne(Filters.eq("_id", playerModel.getUuid().toString()), statsUpdates);
    }
}

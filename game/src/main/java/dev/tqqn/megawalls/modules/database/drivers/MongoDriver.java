package dev.tqqn.megawalls.modules.database.drivers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import dev.tqqn.megawalls.modules.database.framework.adapters.CuboidAdapter;
import dev.tqqn.megawalls.modules.database.framework.adapters.LocationAdapter;
import dev.tqqn.megawalls.modules.database.framework.adapters.UUIDAdapter;
import dev.tqqn.megawalls.modules.database.framework.adapters.WorldAdapter;
import dev.tqqn.megawalls.modules.database.framework.driver.IDatabaseDriver;
import dev.tqqn.megawalls.modules.region.framework.Cuboid;
import dev.tqqn.megawalls.modules.database.DatabaseModule;
import dev.tqqn.megawalls.common.database.MongoItem;
import dev.tqqn.megawalls.common.database.MongoObject;
import lombok.Getter;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import static java.util.Objects.requireNonNull;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

/**
 * The MongoDriver class implements the IDatabaseDriver interface and provides functionality
 * for connecting to and interacting with a MongoDB database.
 */
@Getter
public final class MongoDriver implements IDatabaseDriver {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private final Gson gson;
    private final JsonWriterSettings WRITER_SETTINGS = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();

    private final Executor executors = Executors.newCachedThreadPool();

    private final DatabaseModule databaseModule;

    public MongoDriver(DatabaseModule databaseModule) {
        this.databaseModule = databaseModule;
        this.gson = new GsonBuilder().registerTypeAdapter(Cuboid.class, new CuboidAdapter()).registerTypeAdapter(World.class, new WorldAdapter()).registerTypeAdapter(UUID.class, new UUIDAdapter()).registerTypeAdapter(Location.class, new LocationAdapter()).enableComplexMapKeySerialization().create();
    }

    @Override
    public void connect(String database, String host, String port) {
        this.mongoClient = new MongoClient(new MongoClientURI("mongodb://" + host + ":" + port));
        this.mongoDatabase = mongoClient.getDatabase(database);
    }

    public <O extends MongoObject<?>> void saveAsync(O object) {
        CompletableFuture.runAsync(() -> {
            Document document = Document.parse(gson.toJson(object));
            if (getCollection(object.getClass()).find(eq("_id", document.get("_id"))).first() == null) {
                getCollection(object.getClass()).insertOne(document);
                return;
            }
            getCollection(object.getClass()).replaceOne(eq("_id", document.get("_id")), document, new ReplaceOptions().upsert(true));
        }, executors).exceptionally((exception) -> {
            databaseModule.getLogger().log(Level.SEVERE, "Could not save " + object.getClass(), exception);
            return null;
        });
    }

    public <O extends MongoObject<?>, K> O read(Class<O> clazz, K key) {
        MongoCollection<Document> mongoCollection = getCollection(clazz);
        Document document = mongoCollection.find(eq("_id", key)).first();

        if (document == null) return null;

        return gson.fromJson(document.toJson(WRITER_SETTINGS), clazz);
    }

    public <O extends MongoObject<?>, K> CompletableFuture<O> readAsync(Class<O> clazz, K key) {
        return CompletableFuture.supplyAsync(() -> read(clazz, key));
    }

    public <O extends MongoObject<?>> void updateAsync(O object, String field, Object value) {
        CompletableFuture.runAsync(() -> getCollection(object.getClass()).updateOne(eq("_id", object.getKey().toString()), set(field, value)));
    }

    private MongoCollection<Document> getCollection(Class<?> clazz) {
        MongoItem item = clazz.getAnnotation(MongoItem.class);
        requireNonNull(item, String.format("Class '%s' does not have the MongoEntity annotation.", clazz.getSimpleName()));

        return mongoDatabase.getCollection(item.value());
    }

}

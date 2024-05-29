package dev.tqqn.kireiwalls.modules.database.framework.adapters;

import com.google.gson.*;
import dev.tqqn.kireiwalls.modules.region.framework.Cuboid;
import org.bukkit.Location;

import java.lang.reflect.Type;

public class CuboidAdapter implements JsonSerializer<Cuboid>, JsonDeserializer<Cuboid> {

    private final LocationAdapter locationAdapter = new LocationAdapter();

    @Override
    public JsonElement serialize(Cuboid cuboid, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("point-1", locationAdapter.serialize(cuboid.getPoint1(), null, jsonSerializationContext));
        jsonObject.add("point-2", locationAdapter.serialize(cuboid.getPoint2(), null, jsonSerializationContext));
        return jsonObject;
    }

    @Override
    public Cuboid deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Location point1 = locationAdapter.deserialize(jsonObject.get("point-1"), null, jsonDeserializationContext);
        Location point2 = locationAdapter.deserialize(jsonObject.get("point-2"), null, jsonDeserializationContext);
        return new Cuboid(point1, point2);
    }


}

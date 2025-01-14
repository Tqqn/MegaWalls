package dev.tqqn.megawalls.modules.database.framework.adapters;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.Map;

public final class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        final Type locationType = TypeToken.get(Map.class).getType();
        Map<String, Object> serializedLocation = context.deserialize(jsonElement, locationType);
        return Location.deserialize(serializedLocation);
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        Map<String, Object> serialize = location.serialize();
        String worldName = serialize.get("world").toString();
        serialize.put("world", "temp_" + worldName);
        return context.serialize(serialize);
    }
}

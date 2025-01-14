package dev.tqqn.megawalls.modules.database.framework.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;

public class WorldAdapter extends TypeAdapter<World> {

    @Override
    public World read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return Bukkit.getWorld(jsonReader.nextString());
    }

    @Override
    public void write(JsonWriter jsonWriter, World world) throws IOException {
        if (world == null) {//   null check just in case
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(world.getName());
    }
}

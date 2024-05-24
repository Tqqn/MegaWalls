package dev.tqqn.kireiwalls.framework.database.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

public class UUIDAdapter extends TypeAdapter<UUID> {

    @Override
    public UUID read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return UUID.fromString(jsonReader.nextString());
    }

    @Override
    public void write(JsonWriter jsonWriter, UUID uuid) throws IOException {
        if (uuid == null) {//   null check just in case
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(uuid.toString());
    }
}

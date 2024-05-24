package dev.tqqn.kireiwalls.modules.database.drivers.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public abstract class MongoObject<T> {

    @SerializedName("_id")

    private final T key;

    protected MongoObject(T key) {
        this.key = key;
    }
}

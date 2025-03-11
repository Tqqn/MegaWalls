package dev.tqqn.megawalls.common.classes.levels;

import dev.tqqn.megawalls.common.database.MongoObject;
import lombok.Getter;

@Getter
public final class ClassUpgradeValues extends MongoObject<String> {

    public ClassUpgradeValues(String key) {
        super(key);
    }
}

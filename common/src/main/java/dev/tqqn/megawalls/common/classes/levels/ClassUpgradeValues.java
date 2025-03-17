package dev.tqqn.megawalls.common.classes.levels;

import dev.tqqn.megawalls.common.classes.levels.types.kits.ClassKit;
import dev.tqqn.megawalls.common.database.MongoObject;
import lombok.Getter;

@Getter
public final class ClassUpgradeValues extends MongoObject<String> {

    private final ClassKit classKit;

    public ClassUpgradeValues(String key) {
        super(key);
        this.classKit = new ClassKit();
    }
}

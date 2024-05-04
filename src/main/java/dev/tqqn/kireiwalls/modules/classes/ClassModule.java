package dev.tqqn.kireiwalls.modules.classes;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.game.classes.AbstractClass;
import lombok.Getter;

import java.util.*;

@Getter
public class ClassModule extends AbstractModule {
    @Getter
    private static final List<AbstractClass> classes = new ArrayList<>();

    public ClassModule(KireiWalls plugin) {
        super(plugin, "Class");
    }

    public void onEnable() {
        classes.add(new Herobrine());
        classes.add(new Skeleton());
        classes.add(new Zombie());
    }

    public void onDisable() {
    }

}

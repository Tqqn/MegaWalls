package dev.tqqn.kireiwalls.modules.classes;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.game.classes.AbstractClass;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ClassModule extends AbstractModule {
    private final Set<AbstractClass> classes = new HashSet<>();

    public ClassModule(KireiWalls plugin) {
        super(plugin, "Class");
    }

    public void onEnable() {
        this.classes.add(new Herobrine());
        this.classes.add(new Skeleton());
        this.classes.add(new Zombie());
    }

    public void onDisable() {
    }

}

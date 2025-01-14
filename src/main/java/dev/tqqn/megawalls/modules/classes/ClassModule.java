package dev.tqqn.megawalls.modules.classes;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.classes.framework.AbstractClass;
import dev.tqqn.megawalls.modules.classes.framework.listener.ClassesListener;
import lombok.Getter;

import java.util.*;

/**
 * The ClassModule class extends AbstractModule and represents a module responsible for managing classes in the plugin.
 * It provides functionality to enable and disable the module, as well as manage a list of abstract classes.
 */
@Getter
public final class ClassModule extends AbstractModule {

    @Getter
    private static final List<AbstractClass> classes = new ArrayList<>();

    public ClassModule(MegaWalls plugin) {
        super(plugin, "Class");
    }

    @Override
    protected void onEnable() {
        addComponent(ClassesListener.class);
        classes.add(new Herobrine());
        classes.add(new Skeleton());
        classes.add(new Zombie());

        for (AbstractClass abstractClass : classes) {
            abstractClass.initKitItems();
        }
    }

    @Override
    protected void onDisable() {
        classes.clear();
    }
}

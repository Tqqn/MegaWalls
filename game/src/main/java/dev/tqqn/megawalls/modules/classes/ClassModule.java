package dev.tqqn.megawalls.modules.classes;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.classes.framework.objects.AbstractClass;
import dev.tqqn.megawalls.modules.classes.framework.listener.ClassesListener;
import dev.tqqn.megawalls.modules.game.GameModule;
import lombok.Getter;
import org.bukkit.NamespacedKey;

import java.util.*;

/**
 * The ClassModule class extends AbstractModule and represents a module responsible for managing classes in the plugin.
 * It provides functionality to enable and disable the module, as well as manage a list of abstract classes.
 */
@Getter
public final class ClassModule extends AbstractModule {

    public static final NamespacedKey POTION_HEAL_KEY = new NamespacedKey(MegaWalls.getInstance(), "heal");
    public static final NamespacedKey KIT_ITEM_KEY = new NamespacedKey(MegaWalls.getInstance(), "kit");
    public static final NamespacedKey CLASS_ABILITY_ITEM_KEY = new NamespacedKey(MegaWalls.getInstance(), "ability");

    @Getter
    private static final List<AbstractClass> classes = new ArrayList<>();

    public ClassModule(MegaWalls plugin) {
        super(plugin, "Class");
    }

    @Override
    protected void onEnable() {
        register(new ClassesListener(getPlugin().getModuleManager().getModule(GameModule.class)));
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

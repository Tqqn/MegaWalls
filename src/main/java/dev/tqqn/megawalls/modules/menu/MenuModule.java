package dev.tqqn.megawalls.modules.menu;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.menu.framework.listeners.MenuListener;

/**
 * The MenuModule class represents a module for managing menus.
 */
public final class MenuModule extends AbstractModule {

    public MenuModule(MegaWalls plugin) {
        super(plugin, "Menu");
    }

    @Override
    public void onEnable() {
        addComponent(MenuListener.class);
    }
}

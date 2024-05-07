package dev.tqqn.kireiwalls.modules.menu;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.menu.listeners.MenuListener;

/**
 * The MenuModule class represents a module for managing menus.
 */
public final class MenuModule extends AbstractModule {

    public MenuModule(KireiWalls plugin) {
        super(plugin, "Menu");
    }

    @Override
    public void onEnable() {
        addComponent(MenuListener.class, "");
    }

    @Override
    public void onDisable() {
    }
}

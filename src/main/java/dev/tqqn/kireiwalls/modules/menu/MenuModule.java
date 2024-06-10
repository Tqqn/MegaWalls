package dev.tqqn.kireiwalls.modules.menu;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.AbstractModule;
import dev.tqqn.kireiwalls.modules.menu.framework.listeners.MenuListener;

/**
 * The MenuModule class represents a module for managing menus.
 */
public final class MenuModule extends AbstractModule {

    public MenuModule(KireiWalls plugin) {
        super(plugin, "Menu");
    }

    @Override
    public void onEnable() {
        addComponent(MenuListener.class);
    }
}

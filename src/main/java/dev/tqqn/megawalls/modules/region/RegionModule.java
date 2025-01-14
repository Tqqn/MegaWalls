package dev.tqqn.megawalls.modules.region;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.region.framework.listeners.RegionListener;

/**
 * The RegionModule class manages region-related functionality.
 */
public final class RegionModule extends AbstractModule {

    public RegionModule(MegaWalls plugin) {
        super(plugin, "Region");
    }

    @Override
    public void onEnable() {
        this.addComponent(RegionListener.class);
    }
}

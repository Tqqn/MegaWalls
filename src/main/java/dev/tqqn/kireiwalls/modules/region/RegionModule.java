package dev.tqqn.kireiwalls.modules.region;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.AbstractModule;
import dev.tqqn.kireiwalls.modules.region.framework.listeners.RegionListener;

/**
 * The RegionModule class manages region-related functionality.
 */
public final class RegionModule extends AbstractModule {

    public RegionModule(KireiWalls plugin) {
        super(plugin, "Region");
    }

    @Override
    public void onEnable() {
        this.addComponent(RegionListener.class);
    }
}

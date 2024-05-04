package dev.tqqn.kireiwalls.modules.region;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.region.listeners.RegionListener;

public class RegionModule extends AbstractModule {

    public RegionModule(KireiWalls plugin) {
        super(plugin, "Region");
    }

    public void onEnable() {
        this.addComponent(RegionListener.class, "");
    }

    public void onDisable() {
    }
}

package dev.tqqn.kireiwalls.modules.region;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.region.listeners.PlayerMoveListener;

public class RegionModule extends AbstractModule {
    public RegionModule(KireiWalls plugin) {
        super(plugin, "Region");
    }

    @Override
    public void onEnable() {
        addComponent(PlayerMoveListener.class, "");
    }

    @Override
    public void onDisable() {

    }
}

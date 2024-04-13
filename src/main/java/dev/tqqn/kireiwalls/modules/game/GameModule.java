package dev.tqqn.kireiwalls.modules.game;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.modules.game.commands.DebugCommand;

public class GameModule extends AbstractModule {


    public GameModule(KireiWalls plugin) {
        super(plugin, "Game");
    }

    @Override
    public void onEnable() {
        addComponent(DebugCommand.class, "debug");
    }

    @Override
    public void onDisable() {

    }
}

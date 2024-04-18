package dev.tqqn.kireiwalls.modules.game.states.active;

import dev.tqqn.kireiwalls.framework.game.AbstractGameState;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.listeners.ActiveListeners;
import dev.tqqn.kireiwalls.modules.game.teams.TeamModule;

public class ActiveState extends AbstractGameState {

    public ActiveState(GameModule gameModule) {
        super(gameModule, GameStates.ACTIVE, "Active");
    }

    @Override
    public void onEnable() {
        addListener(ActiveListeners.class);
        TeamModule.getGameTeams().forEach((key, value) -> value.spawnWither());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void run() {

    }
}

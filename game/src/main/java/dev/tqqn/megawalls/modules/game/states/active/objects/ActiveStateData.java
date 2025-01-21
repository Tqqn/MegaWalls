package dev.tqqn.megawalls.modules.game.states.active.objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class ActiveStateData {

    private boolean allWitherDeath = false;
    private boolean initHealth = false;
    private boolean hasEnded = false;

}

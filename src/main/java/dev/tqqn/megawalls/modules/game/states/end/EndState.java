package dev.tqqn.megawalls.modules.game.states.end;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.database.DatabaseModule;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.framework.AbstractGameState;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.teams.TeamModule;
import dev.tqqn.megawalls.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public final class EndState extends AbstractGameState {

    private final DatabaseModule databaseModule;

    private int timer = 10;

    /**
     * Constructs an AbstractGameState object with the specified attributes.
     *
     * @param gameModule The GameModule associated with the state.
     */
    public EndState(GameModule gameModule, DatabaseModule databaseModule) {
        super(gameModule, GameStates.END, "End");
        this.databaseModule = databaseModule;
    }

    @Override
    public void onEnable() {
        this.runTaskTimer(MegaWalls.getInstance(), 0, 20L);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void run() {
        if (timer <= 0) {
            cancel();
            endGameLogic();
            return;
        }
        if (timer == 10) {
            Bukkit.broadcast(ChatUtil.format("<red>This game will end in <bold>10</bold> seconds."));
        }

        if (timer <= 5 && timer > 0) {
            Bukkit.broadcast(ChatUtil.format("<red>This game will end in <bold>" + timer + " </bold>seconds."));
        }

        timer--;
    }

    private void endGameLogic() {
        TeamModule.getGameTeams().values().forEach((gameTeam) -> gameTeam.getGameWither().kill());

        getGameModule().getIngamePlayers().forEach((playerModel) -> {
            this.databaseModule.savePlayer(playerModel);

            final Player target = playerModel.getPlayer();
            if (target != null) {
                target.closeInventory();
                target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
                target.setHealth(playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                target.kick(ChatUtil.format("<red>Game ended!"));
            }
        });
        Bukkit.getServer().shutdown();
    }
}

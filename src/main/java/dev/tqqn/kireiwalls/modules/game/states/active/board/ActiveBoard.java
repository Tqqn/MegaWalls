package dev.tqqn.kireiwalls.modules.game.states.active.board;

import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerStats;
import dev.tqqn.kireiwalls.modules.teams.framework.GameTeam;
import dev.tqqn.kireiwalls.modules.scoreboard.framework.PluginScoreboard;
import dev.tqqn.kireiwalls.modules.game.states.active.ActiveState;
import dev.tqqn.kireiwalls.modules.teams.TeamModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import dev.tqqn.kireiwalls.utils.MessageUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * The ActiveBoard class represents a scoreboard displayed to players during active gameplay.
 */
public final class ActiveBoard extends PluginScoreboard {

    private final Map<String, GameTeam> teams = TeamModule.getGameTeams();

    /**
     * Constructs an ActiveBoard object for the specified player model.
     *
     * @param playerModel The player model.
     */
    public ActiveBoard(PlayerModel playerModel) {
        super("Active", playerModel);
        LocalDateTime date = LocalDateTime.now();
        this.setDisplayName(playerModel.getGameTeam().getLegacyColor() + MessageUtil.SCOREBOARD_TITLE.getStringMessage());
        this.addLines("§7" + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        this.addLines(" ");
        if (ActiveState.getCurrentCycle() == null) {
            this.addLines("§fWalls fall in: §a0:00");
        } else {
            this.addLines("§fGame End: §a0:00");
        }

        this.addLines("§e0 §8/ §90 §8/ §c0 §8/ §a0");
        this.addLines(" ");
        this.addLines(teams.get("Blue").getGameWither().getScoreboardStatus());
        this.addLines(teams.get("Green").getGameWither().getScoreboardStatus());
        this.addLines(teams.get("Red").getGameWither().getScoreboardStatus());
        this.addLines(teams.get("Yellow").getGameWither().getScoreboardStatus());
        this.addLines(" ");
        this.addLines("§a0 §fKills §a0 §fAssists");
        this.addLines("§a0 §fFinals §a0 §fF.Assists");
        this.addLines(String.format("§6%s §fCoins", getPlayerModel().getCoins()));
        this.addLines(" ");
        this.addLines(MessageUtil.SCOREBOARD_UNDERNAME.getStringMessage());
    }

    @Override
    public void update() {
        if (ActiveState.getCurrentCycle() == ActiveState.Cycle.PREPARE) {
            this.updateLine("§fWalls falling in: §a" + ChatUtil.convertSecondsToHMmSs(ActiveState.getCycleTimer()), 12);
        } else {
            this.updateLine("§fGame End: §a" + ChatUtil.convertSecondsToHMmSs(ActiveState.getTimer()), 12);
        }

        this.updateLine("§e0 §8/ §90 §8/ §c0 §8/ §a0", 11);
        this.updateLine((teams.get("Blue")).getGameWither().getScoreboardStatus(), 9);
        this.updateLine((teams.get("Green")).getGameWither().getScoreboardStatus(), 8);
        this.updateLine((teams.get("Red")).getGameWither().getScoreboardStatus(), 7);
        this.updateLine((teams.get("Yellow")).getGameWither().getScoreboardStatus(), 6);
        this.updateLine("§a" + getPlayerModel().getPlayerStats().getStat(PlayerStats.StatType.KILLS) + " §fKills §a" + getPlayerModel().getPlayerStats().getStat(PlayerStats.StatType.ASSISTS) + " §fAssists", 4);
        this.updateLine("§a" + getPlayerModel().getPlayerStats().getStat(PlayerStats.StatType.FINAL_KILLS) + " §fFinals §a" + getPlayerModel().getPlayerStats().getStat(PlayerStats.StatType.FINAL_ASSISTS) + " §fF.Assists", 3);
        this.updateLine(String.format("§6%s §fCoins", getPlayerModel().getCoins()), 2);
    }
}

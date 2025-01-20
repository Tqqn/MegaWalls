package dev.tqqn.megawalls.modules.game.states.active.board;

import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerStats;
import dev.tqqn.megawalls.modules.game.states.end.EndState;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import dev.tqqn.megawalls.modules.scoreboard.framework.PluginScoreboard;
import dev.tqqn.megawalls.modules.game.states.active.ActiveState;
import dev.tqqn.megawalls.modules.teams.TeamModule;
import dev.tqqn.megawalls.utils.ChatUtil;
import dev.tqqn.megawalls.utils.MessageUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * The ActiveBoard class represents a scoreboard displayed to players during active gameplay.
 */
public final class ActiveBoard extends PluginScoreboard {

    private static final ActiveState activeState = (ActiveState) getGameModule().getCurrentState();

    private final Map<TeamModule.TeamStaticData, GameTeam> teams = TeamModule.getGameTeams();

    /**
     * Constructs an ActiveBoard object for the specified player model.
     *
     * @param playerModel The player model.
     */
    public ActiveBoard(PlayerModel playerModel) {
        super("Active", playerModel);
        LocalDateTime date = LocalDateTime.now();
        this.setDisplayName(playerModel.getTempPlayerData().getGameTeam().getTeamData().getLegacyColor() + MessageUtil.SCOREBOARD_TITLE.getStringMessage());
        this.addLines("§7" + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        this.addLines(" ");
        if (activeState.getCurrentCycle() == ActiveState.Cycle.PREPARE) {
            this.addLines("§fWalls fall in: §a0:00");
        } else {
            this.addLines("§fGame End: §a0:00");
        }

        this.addLines("§e0 §8/ §90 §8/ §c0 §8/ §a0");
        this.addLines(" ");
        this.addLines(teams.get(TeamModule.TeamStaticData.BLUE).getGameWither().getScoreboardStatus());
        this.addLines(teams.get(TeamModule.TeamStaticData.GREEN).getGameWither().getScoreboardStatus());
        this.addLines(teams.get(TeamModule.TeamStaticData.RED).getGameWither().getScoreboardStatus());
        this.addLines(teams.get(TeamModule.TeamStaticData.YELLOW).getGameWither().getScoreboardStatus());
        this.addLines(" ");
        this.addLines("§a0 §fKills §a0 §fAssists");
        this.addLines("§a0 §fFinals §a0 §fF.Assists");
        this.addLines(String.format("§6%s §fCoins", getPlayerModel().getTempPlayerData().getCoins()));
        this.addLines(" ");
        this.addLines(MessageUtil.SCOREBOARD_UNDERNAME.getStringMessage());
    }

    @Override
    public void update() {
        if (!(getGameModule().getCurrentState() instanceof EndState)) {
            if (activeState.getCurrentCycle() == ActiveState.Cycle.PREPARE) {
                this.updateLine("§fWalls falling in: §a" + ChatUtil.convertSecondsToHMmSs(activeState.getCycleTimer()), 12);
            } else {
                this.updateLine("§fGame End: §a" + ChatUtil.convertSecondsToHMmSs(ActiveState.getTimer()), 12);
            }
        } else {
            this.updateLine("§cGame has ended!", 12);
        }

        this.updateLine("§e0 §8/ §90 §8/ §c0 §8/ §a0", 11);
        this.updateLine((teams.get(TeamModule.TeamStaticData.BLUE)).getGameWither().getScoreboardStatus(), 9);
        this.updateLine((teams.get(TeamModule.TeamStaticData.GREEN)).getGameWither().getScoreboardStatus(), 8);
        this.updateLine((teams.get(TeamModule.TeamStaticData.RED)).getGameWither().getScoreboardStatus(), 7);
        this.updateLine((teams.get(TeamModule.TeamStaticData.YELLOW)).getGameWither().getScoreboardStatus(), 6);
        this.updateLine("§a" + getPlayerModel().getTempPlayerData().getPlayerStats().getStat(PlayerStats.StatType.KILLS) + " §fKills §a" + getPlayerModel().getTempPlayerData().getPlayerStats().getStat(PlayerStats.StatType.ASSISTS) + " §fAssists", 4);
        this.updateLine("§a" + getPlayerModel().getTempPlayerData().getPlayerStats().getStat(PlayerStats.StatType.FINAL_KILLS) + " §fFinals §a" + getPlayerModel().getTempPlayerData().getPlayerStats().getStat(PlayerStats.StatType.FINAL_ASSISTS) + " §fF.Assists", 3);
        this.updateLine(String.format("§6%s §fCoins", getPlayerModel().getTempPlayerData().getCoins()), 2);
    }
}

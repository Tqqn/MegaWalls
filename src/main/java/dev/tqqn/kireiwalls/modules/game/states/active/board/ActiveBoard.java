package dev.tqqn.kireiwalls.modules.game.states.active.board;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.framework.scoreboard.PluginScoreboard;
import dev.tqqn.kireiwalls.modules.game.teams.TeamModule;
import dev.tqqn.kireiwalls.utils.MessageUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ActiveBoard extends PluginScoreboard {

    private final Map<String, GameTeam> teams;

    public ActiveBoard(PlayerModel playerModel) {
        super("Active", playerModel);
        teams = TeamModule.getGameTeams();
        LocalDateTime date = LocalDateTime.now();
        setDisplayName(MessageUtil.SCOREBOARD_TITLE.getStringMessage()); // 15
        addLines("§7" + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); // 14
        addLines(" "); //13
        addLines("§fGame End: §a10:00"); // 12
        addLines("§e0 §8/ §90 §8/ §c0 §8/ §a0"); // 11
        addLines(" "); // 10
        addLines(teams.get("Blue").getGameWither().getScoreboardStatus()); // 9
        addLines(teams.get("Green").getGameWither().getScoreboardStatus()); // 8
        addLines(teams.get("Red").getGameWither().getScoreboardStatus()); // 7
        addLines(teams.get("Yellow").getGameWither().getScoreboardStatus()); // 6
        addLines(" "); // 5
        addLines("§a0 §fKills §a0 §fAssists"); // 4
        addLines("§a0 §fFinals §a0 §fF.Assists"); // 3
        addLines(String.format("§6%s §fCoins", getPlayerModel().getCoins())); // 2
        addLines(" "); // 1
        addLines("§edev.tqqn.kireiwalls"); // 0
    }

    @Override
    public void update() {
        updateLine("§fGame End: §a10:00", 12);
        updateLine("§e0 §8/ §90 §8/ §c0 §8/ §a0", 11);
        updateLine(teams.get("Blue").getGameWither().getScoreboardStatus(), 9);
        updateLine(teams.get("Green").getGameWither().getScoreboardStatus(), 8);
        updateLine(teams.get("Red").getGameWither().getScoreboardStatus(), 7);
        updateLine(teams.get("Yellow").getGameWither().getScoreboardStatus(), 6);
        updateLine("§a0 §fKills §a0 §fAssists", 4);
        updateLine("§a0 §fFinals §a0 §fF.Assists", 3);
        updateLine(String.format("§6%s §fCoins", getPlayerModel().getCoins()), 2);
    }
}

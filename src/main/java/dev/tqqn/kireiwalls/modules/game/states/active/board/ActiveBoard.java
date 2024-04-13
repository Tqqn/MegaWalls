package dev.tqqn.kireiwalls.modules.game.states.active.board;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.scoreboard.PluginScoreboard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActiveBoard extends PluginScoreboard {

    public ActiveBoard(PlayerModel playerModel) {
        super("Active", playerModel);
        LocalDateTime date = LocalDateTime.now();
        setDisplayName("§9§lKIREI WALLS"); // 15
        addLines("§7" + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); // 14
        addLines(" "); //13
        addLines("§fGame End: §a10:00"); // 12
        addLines("§e0 §8/ §90 §8/ §c0 §8/ §a0"); // 11
        addLines(" "); // 10
        addLines("§9[B] Wither ❤: 1000"); // 9
        addLines("§a[G] Wither ❤: 1000"); // 8
        addLines("§c[R] Wither ❤: 1000"); // 7
        addLines("§e[Y] Wither ❤: 1000"); // 6
        addLines(" "); // 5
        addLines("§a0 §fKills §a0 §fAssists"); // 4
        addLines("§a0 §fFinals §a0 §fF.Assists"); // 3
        addLines("§60 §fCoins"); // 2
        addLines(" "); // 1
        addLines("§edev.tqqn.kireiwalls"); // 0
    }

    @Override
    public void update() {
        updateLine("§fGame End: §a10:00", 12);
        updateLine("§e0 §8/ §90 §8/ §c0 §8/ §a0", 11);
        updateLine("§9[B] Wither ❤: 1000", 9);
        updateLine("§a[G] Wither ❤: 1000", 8);
        updateLine("§c[R] Wither ❤: 1000", 7);
        updateLine("§e[Y] Wither ❤: 1000", 6);
        updateLine("§a0 §fKills §a0 §fAssists", 4);
        updateLine("§a0 §fFinals §a0 §fF.Assists", 3);
        updateLine(String.format("§6%s §fCoins", getPlayerModel().getCoins()), 2);
    }
}

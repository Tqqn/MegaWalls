package dev.tqqn.kireiwalls.modules.game.states.lobby.board;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.scoreboard.PluginScoreboard;
import dev.tqqn.kireiwalls.modules.game.states.lobby.LobbyState;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import dev.tqqn.kireiwalls.utils.MessageUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LobbyBoard extends PluginScoreboard {

    public LobbyBoard(PlayerModel playerModel) {
        super("Lobby", playerModel);
        LocalDateTime date = LocalDateTime.now();
        setDisplayName(MessageUtil.SCOREBOARD_TITLE.getStringMessage()); // 12
        addLines("§7" + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); // 11
        addLines(" "); // 10
        addLines("§fMap: §aDragonkeep"); // 9
        addLines("§fPlayers: §a1/100"); // 8
        addLines(" "); // 7
        addLines("§fStarting in §a05:00 §fif"); // 6
        addLines("§a29 §fmore players join"); // 5
        addLines(" "); // 4
        addLines("§fSelected Class:"); // 3
        addLines("§aHerobrine"); // 2
        addLines(" "); // 1
        addLines("§edev.tqqn.kireiwalls"); // 0
    }

    @Override
    public void update() {
        updateLine("§fStarting in §a" + ChatUtil.convertSecondsToHMmSs(LobbyState.getTimer()) +" §fif", 6);
    }

}

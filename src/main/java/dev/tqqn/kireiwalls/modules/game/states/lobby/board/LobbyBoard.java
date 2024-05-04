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
        this.setDisplayName(MessageUtil.SCOREBOARD_TITLE.getStringMessage());
        this.addLines("§7" + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        this.addLines(" ");
        this.addLines("§fMap: §aDragonkeep");
        this.addLines("§fPlayers: §a1/100");
        this.addLines(" ");
        this.addLines("§fStarting in §a05:00 §fif");
        this.addLines("§a29 §fmore players join");
        this.addLines(" ");
        this.addLines("§fSelected Class:");
        this.addLines("§aHerobrine");
        this.addLines(" ");
        this.addLines("§edev.tqqn.kireiwalls");
    }

    public void update() {
        this.updateLine("§fStarting in §a" + ChatUtil.convertSecondsToHMmSs(LobbyState.getTimer()) + " §fif", 6);
        String selectedClass = "§aRandom!";

        if (getPlayerModel().getCurrentClass() != null) {
            selectedClass = "§a" + getPlayerModel().getCurrentClass().getName();
        }

        this.updateLine(selectedClass, 2);
    }
}

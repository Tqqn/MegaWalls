package dev.tqqn.megawalls.modules.game.states.lobby.board;

import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.scoreboard.framework.PluginScoreboard;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.states.lobby.LobbyState;
import dev.tqqn.megawalls.utils.ChatUtil;
import dev.tqqn.megawalls.utils.MessageUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The LobbyBoard class represents the scoreboard displayed in the lobby for players.
 */
public final class LobbyBoard extends PluginScoreboard {

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
        this.addLines("§aRandom");
        this.addLines(" ");
        this.addLines(MessageUtil.SCOREBOARD_UNDERNAME.getStringMessage());
    }

    @Override
    public void update() {
        this.updateLine("§fPlayers: §a" + getGameModule().getInGamePlayers().size() +  "/100", 8);
        this.updateLine("§fStarting in §a" + ChatUtil.convertSecondsToHMmSs(LobbyState.getTimer()) + " §fif", 6);
        String selectedClass = "§aRandom!";

        if (getPlayerModel().getTempPlayerData().getCurrentClass() != null) {
            selectedClass = "§a" + getPlayerModel().getTempPlayerData().getCurrentClass().getName();
        }

        this.updateLine(selectedClass, 2);
    }
}

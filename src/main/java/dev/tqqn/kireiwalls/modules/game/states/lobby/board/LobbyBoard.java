package dev.tqqn.kireiwalls.modules.game.states.lobby.board;

import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.scoreboard.PluginScoreboard;
import dev.tqqn.kireiwalls.utils.MessageUtil;
import lombok.Getter;
import org.bukkit.entity.Player;

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
        addLines("§fStarting in §a05:00 §if"); // 6
        addLines("§a29 §fmore players join"); // 5
        addLines(" "); // 4
        addLines("§fSelected Class:"); // 3
        addLines("§aHerobrine"); // 2
        addLines(" "); // 1
        addLines("§edev.tqqn.kireiwalls"); // 0
    }

    public void updateLineType(Player player, UpdateType updateType, String... placeholder) {
        //updateLine(player, updateType.format(placeholder), updateType.line);
    }

    @Override
    public void update() {

    }


    @Getter
    public enum UpdateType {
        MAP(9,"§fMap: §a%s"),
        PLAYERS(8, "§fPlayers: §a%s/100"),
        TIME(6, "§fStarting in §a%s §if"),
        PLAYERS_NEEDED(5, "§a%s §fmore players join"),
        CLASS(2, "§a%s");

        private final int line;
        private final String value;
        UpdateType(int line, String value) {
            this.line = line;
            this.value = value;
        }

        public String format(String... placeholder) {
            return String.format(value, placeholder);
        }
    }
}

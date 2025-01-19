package dev.tqqn.megawalls.modules.teams;

import com.google.common.collect.ImmutableMap;
import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import dev.tqqn.megawalls.modules.teams.framework.GameTeamSettings;
import dev.tqqn.megawalls.modules.database.DatabaseModule;
import dev.tqqn.megawalls.modules.arena.ArenaModule;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * The TeamModule class manages the game teams.
 */
public final class TeamModule extends AbstractModule {

    private static EnumMap<TeamStaticData, GameTeam> gameTeams;
    private GameTeam RED;
    private GameTeam BLUE;
    private GameTeam GREEN;
    private GameTeam YELLOW;

    public TeamModule(MegaWalls plugin) {
        super(plugin, "Team");
    }

    @Override
    public void onEnable() {
        final DatabaseModule databaseModule = this.getPlugin().getModuleManager().getModule(DatabaseModule.class);
        ArenaModule arenaModule = this.getPlugin().getModuleManager().getModule(ArenaModule.class);
        gameTeams = new EnumMap<>(TeamStaticData.class);
        try {
            this.RED = new GameTeam(TeamStaticData.RED, databaseModule.getMongoDriver().readAsync(GameTeamSettings.class, arenaModule.getCurrentArena().getMapName() + "_RED").get());
            this.BLUE = new GameTeam(TeamStaticData.BLUE, databaseModule.getMongoDriver().readAsync(GameTeamSettings.class, arenaModule.getCurrentArena().getMapName() + "_BLUE").get());
            this.GREEN = new GameTeam(TeamStaticData.GREEN, databaseModule.getMongoDriver().readAsync(GameTeamSettings.class, arenaModule.getCurrentArena().getMapName() + "_GREEN").get());
            this.YELLOW = new GameTeam(TeamStaticData.YELLOW, databaseModule.getMongoDriver().readAsync(GameTeamSettings.class, arenaModule.getCurrentArena().getMapName() + "_YELLOW").get());
        } catch (InterruptedException | ExecutionException e) {
            getLogger().warning("Something went wrong getting the GameTeamSettings from the DB!");
            Bukkit.shutdown();
        }
        gameTeams.put(RED.getTeamData(), RED);
        gameTeams.put(BLUE.getTeamData(), BLUE);
        gameTeams.put(GREEN.getTeamData(), GREEN);
        gameTeams.put(YELLOW.getTeamData(), YELLOW);
    }

    @Override
    public void onDisable() {
        gameTeams.values().forEach((gameTeam) -> {
            if (gameTeam.getGameWither() != null) {
                gameTeam.getGameWither().kill();
            }
        });
    }

    /**
     * Determines which team has fewer players.
     *
     * @return The team with fewer players.
     */
    public GameTeam whichTeamIsSmaller() {
        GameTeam gameTeam = null;

        for (GameTeam gameTeam1 : gameTeams.values()) {
            if (gameTeam == null) {
                gameTeam = gameTeam1;
            }

            if (gameTeam.getCurrentPlayers().size() > gameTeam1.getCurrentPlayers().size()) {
                gameTeam = gameTeam1;
            }
        }
        return gameTeam;
    }

    public static Map<TeamStaticData, GameTeam> getGameTeams() {
        return ImmutableMap.copyOf(gameTeams);
    }

    public GameTeam getTeam(TeamStaticData gameTeam) {
        return gameTeams.get(gameTeam);
    }

    @Getter
    public enum TeamStaticData {
        RED("Red", "100_red", "[R]", "<red>", "§c", NamedTextColor.RED),
        BLUE("Blue", "98_blue", "[B]", "<blue>", "§9", NamedTextColor.BLUE),
        GREEN("Green", "97_green", "[G]", "<green>", "§a", NamedTextColor.GREEN),
        YELLOW("Yellow", "96_yellow", "[Y]", "<yellow>", "§e", NamedTextColor.YELLOW);

        private final String prettyName;
        private final String tagName;
        private final String prefix;
        private final String color;
        private final String legacyColor;
        private final NamedTextColor namedTextColor;

        TeamStaticData(String prettyName, String tagName, String prefix, String color, String legacyColor, NamedTextColor namedTextColor) {
            this.prettyName = prettyName;
            this.tagName = tagName;
            this.prefix = prefix;
            this.color = color;
            this.legacyColor = legacyColor;
            this.namedTextColor = namedTextColor;
        }
    }
}

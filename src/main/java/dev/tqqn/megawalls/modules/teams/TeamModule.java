package dev.tqqn.megawalls.modules.teams;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import dev.tqqn.megawalls.modules.teams.framework.GameTeamSettings;
import dev.tqqn.megawalls.modules.database.DatabaseModule;
import dev.tqqn.megawalls.modules.arena.ArenaModule;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * The TeamModule class manages the game teams.
 */
public final class TeamModule extends AbstractModule {

    private DatabaseModule databaseModule;
    @Getter private static Map<String, GameTeam> gameTeams;
    @Getter private GameTeam RED;
    @Getter private GameTeam BLUE;
    @Getter private GameTeam GREEN;
    @Getter private GameTeam YELLOW;

    public TeamModule(MegaWalls plugin) {
        super(plugin, "Team");
    }

    @Override
    public void onEnable() {
        this.databaseModule = this.getPlugin().getModuleManager().getModule(DatabaseModule.class);
        ArenaModule arenaModule = this.getPlugin().getModuleManager().getModule(ArenaModule.class);
        gameTeams = new HashMap<>();
        try {
            this.RED = new GameTeam("RED", "Red", "100_red", "R", "<red>", "§c", NamedTextColor.RED, this.databaseModule.getMongoDriver().readAsync(GameTeamSettings.class, arenaModule.getCurrentArena().getMapName() + "_RED").get());
            this.BLUE = new GameTeam("BLUE", "Blue", "98_blue", "B", "<blue>", "§9", NamedTextColor.BLUE, this.databaseModule.getMongoDriver().readAsync(GameTeamSettings.class, arenaModule.getCurrentArena().getMapName() + "_BLUE").get());
            this.GREEN = new GameTeam("GREEN", "Green", "99_green", "G", "<green>", "§a", NamedTextColor.GREEN, this.databaseModule.getMongoDriver().readAsync(GameTeamSettings.class, arenaModule.getCurrentArena().getMapName() + "_GREEN").get());
            this.YELLOW = new GameTeam("YELLOW", "Yellow", "97_yellow", "Y", "<yellow>", "§e", NamedTextColor.YELLOW, this.databaseModule.getMongoDriver().readAsync(GameTeamSettings.class, arenaModule.getCurrentArena().getMapName() + "_YELLOW").get());
        } catch (InterruptedException | ExecutionException e) {
            Bukkit.getLogger().warning("Something went wrong getting the GameTeamSettings from the DB!");
            Bukkit.shutdown();
        }
        gameTeams.putAll(Map.of("Red", this.RED, "Blue", this.BLUE, "Green", this.GREEN, "Yellow", this.YELLOW));
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

}

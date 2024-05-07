package dev.tqqn.kireiwalls.modules.game.teams;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.Map;

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

    public TeamModule(KireiWalls plugin) {
        super(plugin, "Team");
    }

    @Override
    public void onEnable() {
        this.databaseModule = (DatabaseModule)this.getPlugin().getModuleManager().getModule(DatabaseModule.class);
        gameTeams = new HashMap<>();
        this.RED = new GameTeam("RED", "Red", "100_red", "R", "<red>", "§c", NamedTextColor.RED, this.databaseModule.getDefaultConfig().getTeamProtectionCuboid("red"), this.databaseModule.getDefaultConfig().getTeamWitherCuboid("red"), this.databaseModule.getDefaultConfig().getTeamSpawnLocation("red"), this.databaseModule.getDefaultConfig().getTeamWitherLocation("red"));
        this.BLUE = new GameTeam("BLUE", "Blue", "98_blue", "B", "<blue>", "§9", NamedTextColor.BLUE, this.databaseModule.getDefaultConfig().getTeamProtectionCuboid("blue"), this.databaseModule.getDefaultConfig().getTeamWitherCuboid("blue"), this.databaseModule.getDefaultConfig().getTeamSpawnLocation("blue"), this.databaseModule.getDefaultConfig().getTeamWitherLocation("blue"));
        this.GREEN = new GameTeam("GREEN", "Green", "99_green", "G", "<green>", "§a", NamedTextColor.GREEN, this.databaseModule.getDefaultConfig().getTeamProtectionCuboid("green"), this.databaseModule.getDefaultConfig().getTeamWitherCuboid("green"), this.databaseModule.getDefaultConfig().getTeamSpawnLocation("green"), this.databaseModule.getDefaultConfig().getTeamWitherLocation("green"));
        this.YELLOW = new GameTeam("YELLOW", "Yellow", "97_yellow", "Y", "<yellow>", "§e", NamedTextColor.YELLOW, this.databaseModule.getDefaultConfig().getTeamProtectionCuboid("yellow"), this.databaseModule.getDefaultConfig().getTeamWitherCuboid("yellow"), this.databaseModule.getDefaultConfig().getTeamSpawnLocation("yellow"), this.databaseModule.getDefaultConfig().getTeamWitherLocation("yellow"));
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

package dev.tqqn.kireiwalls.modules.game.teams;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.framework.region.types.TeamProtectionRegion;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamModule extends AbstractModule {

    private DatabaseModule databaseModule;
    private List<GameTeam> gameTeams;

    @Getter private GameTeam RED;
    @Getter private GameTeam BLUE;
    @Getter private GameTeam GREEN;
    @Getter private GameTeam YELLOW;

    public TeamModule(KireiWalls plugin) {
        super(plugin, "Team");

    }

    @Override
    public void onEnable() {
        this.databaseModule = (DatabaseModule) getPlugin().getModuleManager().getModule(DatabaseModule.class);
        this.gameTeams = new ArrayList<>();
        this.RED = new GameTeam("RED", "Red", "R", "<red>", new TeamProtectionRegion("RED", databaseModule.getDefaultConfig().getTeamProtectionCuboid("red")), databaseModule.getDefaultConfig().getTeamSpawnLocation("red"), databaseModule.getDefaultConfig().getTeamWitherLocation("red"));
        this.BLUE = new GameTeam("BLUE", "Blue", "B", "<blue>", new TeamProtectionRegion("BLUE", databaseModule.getDefaultConfig().getTeamProtectionCuboid("blue")), databaseModule.getDefaultConfig().getTeamSpawnLocation("blue"), databaseModule.getDefaultConfig().getTeamWitherLocation("blue"));
        this.GREEN = new GameTeam("GREEN", "Green", "G", "<green>", new TeamProtectionRegion("GREEN", databaseModule.getDefaultConfig().getTeamProtectionCuboid("green")), databaseModule.getDefaultConfig().getTeamSpawnLocation("green"), databaseModule.getDefaultConfig().getTeamWitherLocation("green"));
        this.YELLOW = new GameTeam("YELLOW", "Yellow", "Y", "<yellow>", new TeamProtectionRegion("YELLOW", databaseModule.getDefaultConfig().getTeamProtectionCuboid("yellow")), databaseModule.getDefaultConfig().getTeamSpawnLocation("yellow"), databaseModule.getDefaultConfig().getTeamWitherLocation("yellow"));
        gameTeams.addAll(Arrays.asList(RED, BLUE, GREEN, YELLOW));
    }

    @Override
    public void onDisable() {
    }

}

package dev.tqqn.kireiwalls.framework.setup.data;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.teams.GameTeamSettings;
import dev.tqqn.kireiwalls.framework.region.Cuboid;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.setup.SetupModule;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
public final class SetupTeamSettings implements SetupSavable {

    private final SetupModule.Teams team;
    @Setter private Cuboid teamProtectionCuboid;
    @Setter private Cuboid witherProtectionCuboid;
    @Setter private Location spawnLocation;
    @Setter private Location witherLocation;


    public SetupTeamSettings(SetupModule.Teams team) {
        this.team = team;
    }

    @Override
    public boolean save(Player player) {
        if (teamProtectionCuboid == null || witherProtectionCuboid == null || spawnLocation == null || witherLocation == null) return false;

        DatabaseModule databaseModule = KireiWalls.getInstance().getModuleManager().getModule(DatabaseModule.class);
        GameTeamSettings gameTeamSettings = new GameTeamSettings(player.getLocation().getWorld().getName(), team.name(), teamProtectionCuboid, witherProtectionCuboid, spawnLocation, witherLocation);
        databaseModule.getMongoDriver().saveAsync(gameTeamSettings);
        return true;
    }
}

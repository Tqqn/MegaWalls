package dev.tqqn.kireiwalls.framework.region.types;

import dev.tqqn.kireiwalls.framework.region.AbstractRegion;
import dev.tqqn.kireiwalls.framework.region.Cuboid;
import org.bukkit.entity.Player;

public class TeamProtectionRegion extends AbstractRegion {

    public TeamProtectionRegion(String name, Cuboid cuboid) {
        super(name, cuboid, RegionType.PROTECTION);
    }

    @Override
    public void onEntry(Player player) {
        player.sendMessage("You entered the protective region.");
    }

    @Override
    public void onExit(Player player) {
        player.sendMessage("You exited the protective region.");
    }
}

package dev.tqqn.megawalls.modules.game.states.active.runnables;

import dev.tqqn.megawalls.common.classes.ClassAbilityParticles;
import dev.tqqn.megawalls.modules.classes.ClassModule;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.game.GameModule;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

@RequiredArgsConstructor
public final class AbilityReadyRunnable extends BukkitRunnable {

    private final GameModule gameModule;
    private final ClassModule classModule;

    @Override
    public void run() {
        for (PlayerModel playerModel : gameModule.getInGamePlayers()) {
            final Player player = playerModel.getPlayer();
            final ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR || itemStack.getItemMeta() == null) return;
            if (!itemStack.getItemMeta().getPersistentDataContainer().has(ClassModule.CLASS_ABILITY_ITEM_KEY)) continue;
            if (!classModule.getClass(playerModel.getTempPlayerData().getCurrentClass()).canUseAbility(playerModel)) continue;

            final Location playerLocation = playerModel.getPlayer().getLocation();
            ClassAbilityParticles.DEFAULT_GREEN.spawnParticle(calculateHandLocation(playerLocation));
        }
    }

    private Location calculateHandLocation(Location playerLoc) {
        Location playerLocation = playerLoc.clone();
        float angle = playerLocation.getYaw() / 60;
        playerLocation = playerLocation.add(0, 0.6, 0);
        return playerLocation.subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(0.5));
    }
}

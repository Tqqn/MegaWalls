package dev.tqqn.kireiwalls.modules.classes.runnables;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.classes.Zombie;
import dev.tqqn.kireiwalls.utils.cooldown.CooldownUtil;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

public final class ZombieStrenghtRunnable extends BukkitRunnable {

    private final PlayerModel playerModel;
    private final Zombie zombie;
    @Getter private int counter;

    public ZombieStrenghtRunnable(PlayerModel playerModel, Zombie zombie) {
        this.playerModel = playerModel;
        this.zombie = zombie;
        this.counter = 5;
    }

    @Override
    public void run() {
        KireiWalls.getReflectionLayer().sendZombieParticle(playerModel);
        counter--;

        if (playerModel.getPlayer() == null) {
            cancel();
            return;
        }

        if (counter <= 0) {
            zombie.getIsStrenght().remove(playerModel.getUuid());
            zombie.getStrenghtRunnableMap().remove(playerModel.getUuid());
            CooldownUtil.setCooldown(playerModel.getPlayer(), "zombiestrenght", 10);
            cancel();
        }
    }
}

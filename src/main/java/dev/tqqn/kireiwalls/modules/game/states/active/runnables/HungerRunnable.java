package dev.tqqn.kireiwalls.modules.game.states.active.runnables;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.ActiveState;
import dev.tqqn.kireiwalls.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public final class HungerRunnable extends BukkitRunnable {

    private final ActiveState activeState;

    private int hungerCount = 10;
    private int messageCount = 15;

    public HungerRunnable(ActiveState activeState) {
        this.activeState = activeState;
    }

    @Override
    public void run() {
        for (PlayerModel playerModel : GameModule.getIngamePlayers()) {
            if (playerModel.getPlayer() == null) continue;
            if (playerModel.getTempPlayerData().isSpectatorMode()) continue;
            final Player player = playerModel.getPlayer();
            if (!activeState.getGameModule().getArenaModule().getCurrentArena().getArenaSettings().getMiddleCuboid().isIn(player.getLocation())) {
                if (hungerCount == 10) {
                    Bukkit.getScheduler().runTask(KireiWalls.getInstance(), () -> player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 220, activeState.getCurrentHungerStage().getLevel())));
                    hungerCount = 0;
                }

                if (messageCount == 15) {
                    Bukkit.getScheduler().runTask(KireiWalls.getInstance(), () -> player.sendMessage(MessageUtil.HUNGER_NOTIFY.getMessage()));
                    messageCount = 0;
                }
            } else {
                Bukkit.getScheduler().runTask(KireiWalls.getInstance(), () -> player.removePotionEffect(PotionEffectType.HUNGER));
            }
        }
        hungerCount++;
        messageCount++;
    }
}

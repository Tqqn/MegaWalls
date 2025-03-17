package dev.tqqn.megawalls.modules.game.states.active.runnables;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.game.states.active.ActiveState;
import dev.tqqn.megawalls.common.utils.MessageUtil;
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
        if (activeState.getCurrentCycle() != ActiveState.Cycle.DM) return;
        for (PlayerModel playerModel : activeState.getGameModule().getInGamePlayers()) {
            if (playerModel.getPlayer() == null) continue;
            if (playerModel.getTempPlayerData().isSpectatorMode()) continue;
            final Player player = playerModel.getPlayer();
            if (!activeState.getGameModule().getArenaModule().getCurrentArena().getArenaSettings().getMiddleCuboid().isIn(player.getLocation())) {
                if (hungerCount == 10) {
                    Bukkit.getScheduler().runTask(MegaWalls.getInstance(), () -> player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 220, activeState.getCurrentHungerStage().getLevel())));
                    hungerCount = 0;
                }

                if (messageCount == 15) {
                    Bukkit.getScheduler().runTask(MegaWalls.getInstance(), () -> player.sendMessage(MessageUtil.HUNGER_NOTIFY.getMessage()));
                    messageCount = 0;
                }
            } else {
                Bukkit.getScheduler().runTask(MegaWalls.getInstance(), () -> player.removePotionEffect(PotionEffectType.HUNGER));
            }
        }
        hungerCount++;
        messageCount++;
    }
}

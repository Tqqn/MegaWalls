package dev.tqqn.kireiwalls;

import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.modules.ModuleManager;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.modules.game.states.active.ActiveState;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.nms.ReflectionLayer;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import lombok.Getter;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Represents the main class of the KireiWalls plugin.
 */
@Getter
public final class KireiWalls extends JavaPlugin {

    @Getter
    private static KireiWalls instance;
    private ModuleManager moduleManager;
    @Getter private static ReflectionLayer reflectionLayer;

    private final String prefix = "[KireiWalls] ";

    @Override
    public void onLoad() {
        moduleManager = new ModuleManager(this);
    }

    @Override
    public void onEnable() {
        instance = this;

        moduleManager.init();
        findReflectionLayer();
        initScoreboardTask();

        Bukkit.getScheduler().runTask(this, () -> {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

            if (scoreboard.getObjective("onTab") != null) {
                scoreboard.getObjective("onTab").unregister();
            }
            if (scoreboard.getObjective("underName") != null) {
                scoreboard.getObjective("underName").unregister();
            }

            for (Team team : scoreboard.getTeams()) {
                team.unregister();
            }
        });
    }

    @Override
    public void onDisable() {
        moduleManager.disable();
    }

    /**
     * Finds the appropriate reflection layer for the server version.
     */
    private void findReflectionLayer() {
        String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName();
        String version = bukkitVersion.substring(bukkitVersion.lastIndexOf('.') + 1);
        try {
            Class<?> nmsClass = Class.forName("dev.tqqn.kireiwalls.nms." + version + "." + version);
            Bukkit.getLogger().info("Using reflection layer for version " + version);
            reflectionLayer = (ReflectionLayer) nmsClass.getConstructors()[0].newInstance();
        } catch (Exception ignored) {
            Bukkit.getLogger().info("This version is not supported - " + version);
            Bukkit.getServer().shutdown();
        }
    }

    /**
     * Initializes the scoreboard update task.
     */
    private void initScoreboardTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (PlayerModule.getPlayerModel(player.getUniqueId()).getCurrentScoreboard() == null) return;
                PlayerModule.getPlayerModel(player.getUniqueId()).getCurrentScoreboard().update();
                if (GameModule.getCurrentState().getGameStates() == GameStates.ACTIVE) {
                    updateHealth(player);
                }
            }
        }, 0, 10L);
    }

    /**
     * Updates player health on the scoreboard.
     *
     * @param player The player whose health to update.
     */
    private void updateHealth(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective underName = scoreboard.getObjective("underName");

        int maxHealth = (int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

        if (underName != null) {
            Score healthScore = underName.getScore(player);

            healthScore.numberFormat(NumberFormat.styled(Style.style().color(TextColor.color(ChatUtil.getHealthColor(maxHealth, (int) player.getHealth()))).build()));

            underName.getScore(player).setScore((int) player.getHealth());
        }

        if (!(ActiveState.getCurrentCycle() == ActiveState.Cycle.DM)) return;

        Objective onTab = scoreboard.getObjective("onTab");

        if (onTab != null) {
            Score healthScore = onTab.getScore(player);

            healthScore.numberFormat(NumberFormat.styled(Style.style().color(TextColor.color(ChatUtil.getHealthColor(maxHealth, (int) player.getHealth()))).build()));

            onTab.getScore(player).setScore((int) player.getHealth());
        }
    }

}

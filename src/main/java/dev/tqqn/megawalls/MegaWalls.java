package dev.tqqn.megawalls;

import co.aikar.commands.PaperCommandManager;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.ModuleManager;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.states.active.ActiveState;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.nms.ReflectionLayer;
import dev.tqqn.megawalls.utils.ChatUtil;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import lombok.Getter;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Represents the main class of the MegaWalls plugin.
 */
@Getter
public final class MegaWalls extends JavaPlugin {

    @Getter private static MegaWalls instance;
    private PaperCommandManager commandManager;

    private ModuleManager moduleManager;
    @Getter private static ReflectionLayer reflectionLayer;

    @Getter private static final String prefix = "[MegaWalls] ";
    private boolean isSetup;

    @Override
    public void onLoad() {
        commandManager = new PaperCommandManager(this);

        isSetup = getConfig().getBoolean("setup");
        moduleManager = new ModuleManager(this);
        moduleManager.load();
    }

    @Override
    public void onEnable() {
        instance = this;

        moduleManager.init();

       registerScoreboardTeam();

        if (isSetup) return;

        findReflectionLayer();
        initScoreboardTask();
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
            Class<?> nmsClass = Class.forName("dev.tqqn.megawalls.nms." + version + "." + version);
            Bukkit.getLogger().info("Using reflection layer for version " + version);
            reflectionLayer = (ReflectionLayer) nmsClass.getConstructors()[0].newInstance();
        } catch (Exception ignored) {
            Bukkit.getLogger().info("This version is not supported - " + version);
            Bukkit.getServer().shutdown();
        }
    }

    private void registerScoreboardTeam() {
        Bukkit.getScheduler().runTask(this, () -> {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

            final Objective onTab = scoreboard.getObjective("onTab");
            final Objective underName = scoreboard.getObjective("underName");

            if (onTab != null) {
                onTab.unregister();
            }
            if (underName != null) {
                underName.unregister();
            }

            for (Team team : scoreboard.getTeams()) {
                team.unregister();
            }
        });
    }

    /**
     * Initializes the scoreboard update task.
     */
    private void initScoreboardTask() {
        final GameModule gameModule = getModuleManager().getModule(GameModule.class);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (PlayerModule.getPlayerModel(player.getUniqueId()).getTempPlayerData().getCurrentScoreboard() == null) return;
                PlayerModule.getPlayerModel(player.getUniqueId()).getTempPlayerData().getCurrentScoreboard().update();
                if (gameModule.isState(GameStates.ACTIVE)) {
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

        final AttributeInstance genericMaxHealthAtt = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        int maxHealth = 0;

        if (genericMaxHealthAtt != null) {
            maxHealth = (int) genericMaxHealthAtt.getBaseValue();
        }

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

package dev.tqqn.kireiwalls.framework.game.teams.wither;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.modules.game.GameModule;
import dev.tqqn.kireiwalls.nms.framework.ICustomWither;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * The GameWither class represents a game wither entity associated with a specific game team.
 * It manages the health, status, and boss bar of the wither.
 */
public final class GameWither {

    @Getter private final GameTeam gameTeam;

    @Getter private int health;
    @Getter @Setter private WitherStatus witherStatus;
    @Getter private Entity bukkitEntity;
    private ICustomWither nmsEntity;
    @Getter private final BossBar witherBar;
    private final String bossBarName;

    /**
     * Constructs a GameWither object associated with the specified game team.
     *
     * @param gameTeam The game team associated with the wither.
     */
    public GameWither(GameTeam gameTeam) {
        this.gameTeam = gameTeam;
        this.health = 1000;
        this.witherStatus = WitherStatus.PROTECTED;
        this.nmsEntity = KireiWalls.getReflectionLayer().createCustomWither(gameTeam);
        this.bukkitEntity = nmsEntity.getEntity();
        bukkitEntity.setCustomNameVisible(true);
        bukkitEntity.setGlowing(true);
        bukkitEntity.setPersistent(false);

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.registerNewTeam(gameTeam.getName());
        team.color(gameTeam.getNamedTextColor());
        team.addEntry(bukkitEntity.getUniqueId().toString());

        bukkitEntity.setMetadata("Team", new FixedMetadataValue(KireiWalls.getInstance(), gameTeam.getName()));

        bukkitEntity.customName(ChatUtil.format(gameTeam.getColor() + gameTeam.getPrefix() + " WITHER <gray>- " + gameTeam.getColor() + health + "/1000"));

        bossBarName = ChatUtil.translateLegacy(gameTeam.getLegacyColor() + gameTeam.getPrefix() + " WITHER &7- " + gameTeam.getLegacyColor() + health + "/1000");

        this.witherBar = Bukkit.createBossBar(bossBarName, BarColor.valueOf(gameTeam.getName()), BarStyle.SEGMENTED_20);
        this.witherBar.setProgress(1);
    }

    /**
     * Removes the wither entity and updates the wither status.
     */
    public void kill() {
        if (bukkitEntity != null) {
            this.bukkitEntity.remove();
        }
        this.bukkitEntity = null;
        this.witherStatus = WitherStatus.DEATH;
        this.nmsEntity = null;
        this.witherBar.removeAll();
        for (PlayerModel playerModel : this.gameTeam.getAlivePlayers()) {
            playerModel.setProtected(false);
        }
    }

    /**
     * Gets the scoreboard status of the wither.
     *
     * @return The scoreboard status of the wither.
     */
    public String getScoreboardStatus() {
        if (witherStatus == WitherStatus.DEATH) {
            if (gameTeam.getAlivePlayers().isEmpty()) {
                return "§7" + gameTeam.getPrettyName() + " eliminated!";
            }
            return gameTeam.getLegacyColor() + gameTeam.getPrefix() + " Players: " + gameTeam.getAlivePlayers().size();
        }
        return gameTeam.getLegacyColor() + gameTeam.getPrefix() + " Wither ❤: " + health;
    }

    /**
     * Updates the health of the wither name.
     */
    public void updateHealth() {
        bukkitEntity.customName(ChatUtil.format(gameTeam.getColor() + gameTeam.getPrefix() + " WITHER <gray>- " + gameTeam.getColor() + health + "/1000"));
    }

    /**
     * Updates the boss bar of the wither entity.
     */
    public void updateWitherBar() {
        witherBar.setTitle(bukkitEntity.getName());
        witherBar.setProgress((double) health / 1000);
    }

    /**
     * Inflicts damage to the wither entity.
     *
     * @param damage The amount of damage to inflict.
     */
    public void damage(int damage) {
        if (this.health <= 500) {
            nmsEntity.setPowered(false);
        }

        if ((this.health - damage) <= 0) {
            for (PlayerModel playerModel : GameModule.getIngamePlayers()) {
                playerModel.awardWitherDamage(this);
            }
            if (!Bukkit.isPrimaryThread()) {
                Bukkit.getScheduler().runTask(KireiWalls.getInstance(), this::kill);
                return;
            }
            kill();
            return;
        }
        this.health = health - damage;
        updateHealth();
        updateWitherBar();
    }

    /**
     * The status of the wither entity.
     */
    public enum WitherStatus {
        PROTECTED,
        NON_PROTECTED,
        DEATH
    }
}

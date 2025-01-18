package dev.tqqn.megawalls.modules.teams.framework.wither;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.teams.TeamModule;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.nms.framework.ICustomWither;
import dev.tqqn.megawalls.utils.ChatUtil;
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
    @Getter private final TeamModule.TeamStaticData teamStaticData;

    @Getter private int health;
    @Getter @Setter private WitherStatus witherStatus;
    @Getter private Entity bukkitEntity;
    private ICustomWither nmsEntity;
    @Getter private final BossBar witherBar;

    /**
     * Constructs a GameWither object associated with the specified game team.
     *
     * @param gameTeam The game team associated with the wither.
     */
    public GameWither(GameTeam gameTeam) {
        this.gameTeam = gameTeam;
        this.teamStaticData = gameTeam.getTeamData();
        this.health = 1000;
        this.witherStatus = WitherStatus.PROTECTED;
        this.nmsEntity = MegaWalls.getReflectionLayer().createCustomWither(gameTeam);
        this.bukkitEntity = nmsEntity.getEntity();
        bukkitEntity.setCustomNameVisible(true);
        bukkitEntity.setGlowing(true);
        bukkitEntity.setPersistent(false);

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.registerNewTeam(teamStaticData.name());
        team.color(teamStaticData.getNamedTextColor());
        team.addEntry(bukkitEntity.getUniqueId().toString());

        bukkitEntity.setMetadata("Team", new FixedMetadataValue(MegaWalls.getInstance(), teamStaticData.name()));

        bukkitEntity.customName(ChatUtil.format(teamStaticData.getColor() + teamStaticData.getPrefix() + " WITHER <gray>- " + teamStaticData.getColor() + health + "/1000"));

        String bossBarName = ChatUtil.translateLegacy(teamStaticData.getLegacyColor() + teamStaticData.getPrefix() + " WITHER &7- " + teamStaticData.getLegacyColor() + health + "/1000");

        this.witherBar = Bukkit.createBossBar(bossBarName, BarColor.valueOf(teamStaticData.name()), BarStyle.SEGMENTED_20);
        this.witherBar.setProgress(1);
    }

    /**
     * Removes the wither entity and updates the wither status.
     */
    public void kill() {
        if (bukkitEntity != null) {
            if (!Bukkit.isPrimaryThread()) {
                Bukkit.getScheduler().runTask(MegaWalls.getInstance(), () -> bukkitEntity.remove());
            } else {
                bukkitEntity.remove();
            }
        }

        this.bukkitEntity = null;
        this.witherStatus = WitherStatus.DEATH;
        this.nmsEntity = null;
        this.witherBar.removeAll();
        for (PlayerModel playerModel : this.gameTeam.getAlivePlayers()) {
            playerModel.getTempPlayerData().setProtected(false);
        }
    }

    public void setGod(boolean value) {
        nmsEntity.setGod(value);
    }

    /**
     * Gets the scoreboard status of the wither.
     *
     * @return The scoreboard status of the wither.
     */
    public String getScoreboardStatus() {
        if (witherStatus == WitherStatus.DEATH) {
            if (gameTeam.getAlivePlayers().isEmpty()) {
                return "§7" + teamStaticData.getPrefix() + " eliminated!";
            }
            return teamStaticData.getLegacyColor() + teamStaticData.getPrefix() + " Players: " + gameTeam.getAlivePlayers().size();
        }
        return teamStaticData.getLegacyColor() + teamStaticData.getPrefix() + " Wither ❤: " + health;
    }

    /**
     * Updates the health of the wither name.
     */
    public void updateHealth() {
        bukkitEntity.customName(ChatUtil.format(teamStaticData.getColor() + teamStaticData.getPrefix() + " WITHER <gray>- " + teamStaticData.getColor() + health + "/1000"));
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
                playerModel.getTempPlayerData().awardWitherDamage(this);
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

package dev.tqqn.kireiwalls.framework.game.teams.wither;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.nms.framework.ICustomWither;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

public class GameWither {

    private final GameTeam gameTeam;

    @Getter private int health;
    @Getter @Setter private WitherStatus witherStatus;
    @Getter private Entity bukkitEntity;
    private ICustomWither nmsEntity;

    public GameWither(GameTeam gameTeam) {
        this.gameTeam = gameTeam;
        this.health = 1000;
        this.witherStatus = WitherStatus.PROTECTED;
        this.nmsEntity = KireiWalls.getReflectionLayer().createCustomWither(gameTeam);
        this.bukkitEntity = nmsEntity.getEntity();
        bukkitEntity.setCustomNameVisible(true);
        bukkitEntity.setGlowing(true);
        bukkitEntity.setPersistent(false);

        //Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        //Team team = board.registerNewTeam(gameTeam.getName());
        //team.color(gameTeam.getNamedTextColor());
        //team.addEntry(witherEntity.getUniqueId().toString());

        bukkitEntity.setMetadata("Team", new FixedMetadataValue(KireiWalls.getInstance(), gameTeam.getName()));

        bukkitEntity.customName(ChatUtil.format(gameTeam.getColor() + gameTeam.getPrefix() + " WITHER <gray>- " + gameTeam.getColor() + health + "/1000"));
    }

    public void kill() {
        if (bukkitEntity != null) {
            this.bukkitEntity.remove();
        }
        this.bukkitEntity = null;
        this.witherStatus = WitherStatus.DEATH;
        this.nmsEntity = null;
    }

    public String getScoreboardStatus() {
        if (witherStatus == WitherStatus.DEATH) {
            return gameTeam.getLegacyColor() + gameTeam.getPrefix() + " Players: 1";
        }
        return gameTeam.getLegacyColor() + gameTeam.getPrefix() + " Wither ❤: " + health;
    }

    public void updateHealth() {
        bukkitEntity.customName(ChatUtil.format(gameTeam.getColor() + gameTeam.getPrefix() + " WITHER <gray>- " + gameTeam.getColor() + health + "/1000"));
    }

    public void damage(int damage) {
        if (this.health <= 500) {
            nmsEntity.setPowered(false);
        }

        if ((this.health - damage) <= 0) {
            bukkitEntity.remove();
            this.witherStatus = WitherStatus.DEATH;
            return;
        }
        this.health = health - damage;
        updateHealth();
    }

    public enum WitherStatus {
        PROTECTED,
        NON_PROTECTED,
        DEATH
    }

}

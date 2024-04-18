package dev.tqqn.kireiwalls.framework.game.teams.wither;

import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.nms.v1_20_R3.objects.CustomWither;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;

public class GameWither {

    private final GameTeam gameTeam;

    @Getter private int health;
    @Getter @Setter private WitherStatus witherStatus;
    @Getter private Entity bukkitEntity;
    private CustomWither nmsEntity;

    public GameWither(GameTeam gameTeam) {
        this.gameTeam = gameTeam;
        this.health = 1000;
        this.witherStatus = WitherStatus.PROTECTED;
        this.nmsEntity = new CustomWither(gameTeam);
        this.bukkitEntity = nmsEntity.getBukkitEntity();
        bukkitEntity.setCustomNameVisible(true);
        bukkitEntity.setGlowing(true);
        bukkitEntity.setPersistent(false);

        //Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        //Team team = board.registerNewTeam(gameTeam.getName());
        //team.color(gameTeam.getNamedTextColor());
        //team.addEntry(witherEntity.getUniqueId().toString());

        bukkitEntity.customName(ChatUtil.format(gameTeam.getColor() + gameTeam.getPrefix() + " WITHER <gray>- " + gameTeam.getColor() + health + "/1000"));
    }

    public void kill() {
        this.bukkitEntity.remove();
        this.witherStatus = WitherStatus.DEATH;
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

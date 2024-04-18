package dev.tqqn.kireiwalls.nms.v1_20_R3.objects;

import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;

public class CustomWither extends WitherBoss {

    private final GameTeam gameTeam;


    public CustomWither(GameTeam gameTeam) {
        super(EntityType.WITHER, ((CraftWorld) gameTeam.getGameTeamSettings().getWitherLocation().getWorld()).getHandle());
        this.gameTeam = gameTeam;
        setPos(gameTeam.getGameTeamSettings().getWitherLocation().getX(), gameTeam.getGameTeamSettings().getWitherLocation().getY(), gameTeam.getGameTeamSettings().getWitherLocation().getZ());
        this.setNoAi(false);
        this.bossEvent.setVisible(false);
        level().addFreshEntity(this);
        setPowered(true);
    }

    public void setPowered(boolean powered) {
        if (powered) this.setHealth(getHealth() / 2);
        if (!powered) this.setHealth(getMaxHealth());
    }


    @Override
    protected void customServerAiStep() {
    }

    @Override
    public boolean hurt(DamageSource damagesource, float damage) {

        boolean cooldown = true;

        if ((float) this.invulnerableTime > (float) this.invulnerableDuration / 2.0F && !damagesource.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
            if (damage <= this.lastHurt) {
                return false;
            }

            this.lastHurt = damage;
            cooldown = false;
        } else {
            this.lastHurt = damage;
            this.invulnerableTime = this.invulnerableDuration;
            this.hurtDuration = 10;
            this.hurtTime = this.hurtDuration;
        }

        if (cooldown) {
            this.level().broadcastDamageEvent(this, damagesource);
            this.playSound(getHurtSound(damagesource), this.getSoundVolume(), this.getVoicePitch());
            gameTeam.getGameWither().damage(Math.round(damage));
            return false;
        }
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }
}

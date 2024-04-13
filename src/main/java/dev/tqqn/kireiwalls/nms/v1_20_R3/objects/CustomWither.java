package dev.tqqn.kireiwalls.nms.v1_20_R3.objects;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;

public class CustomWither extends WitherBoss {

    public CustomWither(EntityType<? extends WitherBoss> entitytypes, Level world) {
        super(entitytypes, world);
    }
}

package dev.tqqn.kireiwalls.nms.framework;

import org.bukkit.entity.Entity;

public interface ICustomWither {

    void setPowered(boolean powered);
    Entity getEntity();
}

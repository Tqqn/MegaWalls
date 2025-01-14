package dev.tqqn.megawalls.nms.framework;

import org.bukkit.entity.Entity;

/**
 * The ICustomWither interface defines methods for custom withers.
 */
public interface ICustomWither {

    /**
     * Sets whether the wither is powered.
     *
     * @param powered True if the wither is powered, false otherwise.
     */
    void setPowered(boolean powered);

    /**
     * Gets the entity associated with this custom wither.
     *
     * @return The entity representing the custom wither.
     */
    Entity getEntity();
}

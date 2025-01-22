package dev.tqqn.megawalls.common.classes;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public enum ClassAbilityParticles {

    DEFAULT_GREEN(Particle.REDSTONE, new Particle.DustOptions(Color.LIME, 5)),
    SMOKE(Particle.SMOKE_NORMAL, null);

    private final Particle particle;
    private final Particle.DustOptions dustOptions;

    ClassAbilityParticles(Particle particle, Particle.DustOptions dustOptions) {
        this.particle = particle;
        this.dustOptions = dustOptions;
    }

    public void spawnParticle(Location location) {
        if (dustOptions == null) {
            location.getWorld().spawnParticle(particle, location, 5);
        } else {
            location.getWorld().spawnParticle(particle, location, 5, dustOptions);
        }
    }
}

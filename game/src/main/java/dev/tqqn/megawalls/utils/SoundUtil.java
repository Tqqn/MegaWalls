package dev.tqqn.megawalls.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Utility enum for managing sound effects.
 */
public enum SoundUtil {

    COUNTDOWN_SOUND(Sound.BLOCK_DISPENSER_FAIL, 0.5f, 1.5f),
    ENDERCHEST_OPEN(Sound.BLOCK_ENDER_CHEST_OPEN, 0.3f, 1.5f),
    ENDERMAN_TELEPORT(Sound.ENTITY_ENDERMAN_TELEPORT, 0.3f, 1.5f);

    private final Sound sound;
    private final float volume;
    private final float pitch;

    SoundUtil(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * Plays the sound at the specified location for the given player.
     *
     * @param player   The player to hear the sound.
     * @param location The location where the sound is played.
     */
    private void playSound(Player player, Location location) {
        player.playSound(location, this.sound, this.volume, this.pitch);
    }

    public void playSoundForPlayer(Player player) {
        player.playSound(player, this.sound, this.volume, this.pitch);
    }

    /**
     * Plays the sound at the player's current location.
     *
     * @param player The player to hear the sound.
     */
    public void playSoundOnPlayerLoc(Player player) {
        playSound(player, player.getLocation());
    }

    /**
     * Plays the sound at the current location of every online player.
     */

    public void playSoundForEveryPlayerLoc() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playSound(player, player.getLocation());
        }
    }
}

package dev.tqqn.kireiwalls.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum SoundUtil {

    COUNTDOWN_SOUND(Sound.BLOCK_DISPENSER_FAIL, 0.5f, 1.5f);

    private final Sound sound;
    private final float volume;
    private final float pitch;

    SoundUtil(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    private void playSound(Player player, Location location) {
        player.playSound(location, this.sound, this.volume, this.pitch);
    }

    public void playSoundOnPlayerLoc(Player player) {
        playSound(player, player.getLocation());
    }

    public void playSoundForEveryPlayerLoc() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playSound(player, player.getLocation());
        }
    }

}

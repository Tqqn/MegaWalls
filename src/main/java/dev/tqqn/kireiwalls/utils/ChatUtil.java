package dev.tqqn.kireiwalls.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public final class ChatUtil {

    private ChatUtil() {
        // Private constructor to prevent instantiation.
    }

    /**
     * Formats a message using MiniMessage.
     *
     * @param message The message to be formatted.
     * @return The formatted message as a Component.
     */
    public static Component format(String message) {
        MiniMessage extendedInstances = MiniMessage.builder().build();
        return extendedInstances.deserialize(message);
    }

    /**
     * Translates legacy color codes in a string message.
     *
     * @param message The message containing legacy color codes.
     * @return The message with color codes translated.
     */
    public static String translateLegacy(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Translates legacy color codes in an array of strings.
     *
     * @param strings The array of strings containing legacy color codes.
     * @return The array of strings with color codes translated.
     */
    public static String[] translateLegacy(String[] strings) {
        ArrayList<String> msgArray = new ArrayList<>();
        for (String msg : strings) {
            msgArray.add(translateLegacy(msg));
        }
        return msgArray.toArray(new String[0]);
    }

    /**
     * Static Method that converts seconds into a formatted clock.
     *
     * @param seconds long
     */
    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        return String.format("%02d:%02d", m, s);
    }

    /**
     * Gets the color code for health based on current and maximum health.
     *
     * @param maxHealth     The maximum health value.
     * @param currentHealth The current health value.
     * @return The color code for health.
     */
    public static int getHealthColor(float maxHealth, float currentHealth) {
        if (currentHealth >= maxHealth) {
            return 43520; // Dark_Green
        } else if (currentHealth > maxHealth * 3f / 4f) {
            return 5635925; // Green
        } else if (currentHealth > maxHealth / 2f) {
            return 16755200; // Yellow
        } else if (currentHealth > maxHealth / 4f) {
            return 16733525; // RED
        } else {
            return 11141120; // Dark_red
        }
    }
}

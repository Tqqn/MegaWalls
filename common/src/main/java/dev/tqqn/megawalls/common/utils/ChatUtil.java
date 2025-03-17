package dev.tqqn.megawalls.common.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public final class ChatUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder().build();

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
        return MINI_MESSAGE.deserialize(message).decoration(TextDecoration.ITALIC, false);
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

    public static Component centerMessage(String message) {
        Component component = ChatUtil.format(message);

        int messagePxSize = 0;

        TextComponent textComponent = (TextComponent) component;
        String componentString = PlainTextComponentSerializer.plainText().serialize(textComponent);

        for (char c : componentString.toCharArray()) {
            DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
            messagePxSize += dFI.getLength();
            messagePxSize++;
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return ChatUtil.format(sb.toString()).append(component);
    }

}

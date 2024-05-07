package dev.tqqn.kireiwalls.utils;

import net.kyori.adventure.text.Component;

/**
 * Utility enum for managing message templates.
 */
public enum MessageUtil {

    NO_PERMISSION("<redYou do not have <blue>%s <red>permission to do this."),
    PLAYER_JOIN("%s%s <yellow>has joined the game (<aqua%s<yellow>/<aqua>%s<yellow>)!"),
    PLAYER_QUIT("%s%s <yellow> has left the game!"),
    ALL_WITHERS_DEAD("<red>All Withers are dead."),
    DM_COUNTDOWN("<red>Death-Match starting in <green>%s<red>."),
    SCOREBOARD_TITLE("§lKIREI WALLS"),
    KILLED_BY_KILLER_HAND("%s%s <white>was killed by %s%s"),
    KILLED_BY_KILLER_BOW("%s%s <white>was shot and killed by %s%s"),
    KILLED_NO_KILLER("%s%s <white>died."),
    KILL("<red><bold>KILL <reset>"),
    ASSIST("<red><bold>ASSIST <reset>"),
    FINAL_KILL("<aqua><bold>FINAL KILL <reset>"),
    FINAL_ASSIST("<agua><bold>FINAL KILL <red>ASSIST <reset>"),
    COINS_EARNED("<gold>+%s coins! ");

    private final String message;

    MessageUtil(String message) { this.message = message; }

    /**
     * Gets the string representation of the message with optional placeholders.
     *
     * @param placeholder The placeholders to replace in the message template.
     * @return The formatted message.
     */
    public String getStringMessage(String... placeholder) {
        return String.format(message, placeholder);
    }

    /**
     * Gets the string representation of the message without any placeholders.
     *
     * @return The formatted message.
     */
    public String getStringMessage() {
        return message;
    }

    /**
     * Gets the message as a Component object.
     *
     * @return The message as a Component.
     */
    public Component getMessage() { return ChatUtil.format(message); }

    /**
     * Gets the message as a Component object with optional placeholders.
     *
     * @param placeholder The placeholders to replace in the message template.
     * @return The formatted message as a Component.
     */
    public Component getMessage(String... placeholder) { return ChatUtil.format(String.format(message, placeholder)); }
}

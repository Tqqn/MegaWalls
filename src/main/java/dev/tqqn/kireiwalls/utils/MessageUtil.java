package dev.tqqn.kireiwalls.utils;

import net.kyori.adventure.text.Component;

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

    public String getStringMessage(String... placeholder) {
        return String.format(message, placeholder);
    }

    public String getStringMessage() {
        return message;
    }

    public Component getMessage() { return ChatUtil.format(message); }

    public Component getMessage(String... placeholder) { return ChatUtil.format(String.format(message, placeholder)); }
}

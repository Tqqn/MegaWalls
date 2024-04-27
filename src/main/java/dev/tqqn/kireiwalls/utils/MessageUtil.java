package dev.tqqn.kireiwalls.utils;

import net.kyori.adventure.text.Component;

public enum MessageUtil {

    NO_PERMISSION("<redYou do not have <blue>%s <red>permission to do this."),
    PLAYER_JOIN("%s%s <yellow>has joined the game (<aqua%s<yellow>/<aqua>%s<yellow>)!"),
    PLAYER_QUIT("%s%s <yellow> has left the game!"),
    ALL_WITHERS_DEAD("<red>All Withers are dead."),
    DM_COUNTDOWN("<red>Death-Match starting in <green>%s<red>."),
    SCOREBOARD_TITLE("§9§lKIREI WALLS");

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

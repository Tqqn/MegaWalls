package dev.tqqn.megawalls.utils;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

@Getter
public enum Notify {

    SUCCESS("<dark_green>✔ ", "<dark_green>", "<green>"),
    ERROR("<dark_red>✖ ", "<dark_red>", "<red>"),
    INFO("<blue>♦ ", "<#00a2ff>", "<#4a6eff>");

    private final String prefix;
    private final String primaryColor;
    private final String defaultColor;

    Notify(String prefix, String primaryColor, String defaultColor) {
        this.prefix = prefix;
        this.primaryColor = primaryColor;
        this.defaultColor = defaultColor;
    }

    public String getMessage(String message) {
        message = message
                .replace("<primary>", primaryColor)
                .replace("<default>", "<reset>" + defaultColor);
        return prefix + defaultColor + message;
    }

    public Component component(String message) {
        return ChatUtil.format(getMessage(message));
    }

    public void chat(CommandSender sender, String message) {
        sender.sendMessage(component(message));
    }

    public void chat(CommandSender sender, String message, Object... args) {
        chat(sender, String.format(message.replaceAll("%s", "<primary>%s<default>"), args));
    }
}

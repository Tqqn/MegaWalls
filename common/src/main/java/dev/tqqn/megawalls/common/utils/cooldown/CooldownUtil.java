package dev.tqqn.megawalls.common.utils.cooldown;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CooldownUtil {

    private static final Map<String, Long> data = new HashMap<>();

    public static void setCooldown(Player player, String key, int seconds) {
        long delay = System.currentTimeMillis() + (seconds * 1000L);
        data.put(player.getName() + key, delay);
    }

    public static boolean checkCooldown(Player player, String key) {
        return !data.containsKey(player.getName() + key) || data.get(player.getName() + key) <= System.currentTimeMillis();
    }

    public static long getRemainder(Player player, String key) {
        return (data.get(player.getName() + key) - System.currentTimeMillis()) / 1000;
        //return data.get(player.getName() + key) - System.currentTimeMillis() / 1000;
        //return Math.round(();
    }

    public static void removeCooldown(Player player, String key) {
        data.remove(player.getName() + key);
    }
}
//   10000
// 1003000
//  993000
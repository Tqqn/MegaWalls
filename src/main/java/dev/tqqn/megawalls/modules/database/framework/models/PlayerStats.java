package dev.tqqn.megawalls.modules.database.framework.models;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * The PlayerStats class represents statistics related to a player's performance in a game.
 * It encapsulates various types of statistics such as kills, assists, deaths, final kills, final assists,
 * final deaths, and wither damage.
 */
public final class PlayerStats {

    private final Map<StatType, Integer> stats;

    /**
     * Constructs a PlayerStats object with the specified statistics values.
     */
    public PlayerStats() {
        stats = new HashMap<>();
        stats.put(StatType.KILLS, 0);
        stats.put(StatType.ASSISTS, 0);
        stats.put(StatType.DEATHS, 0);
        stats.put(StatType.FINAL_KILLS, 0);
        stats.put(StatType.FINAL_ASSISTS, 0);
        stats.put(StatType.FINAL_DEATH, 0);
        stats.put(StatType.WITHER_DAMAGE, 0);
    }

    /**
     * Retrieves the value of the specified statistic type.
     *
     * @param statType The type of statistic to retrieve.
     * @return The value of the specified statistic type.
     */
    public int getStat(StatType statType) {
        return stats.get(statType);
    }

    /**
     * Increases the value of the specified statistic type by one.
     *
     * @param statType The type of statistic to increase.
     */
    public void increaseStat(StatType statType) {
        stats.put(statType, stats.get(statType)+1);
    }

    /**
     * Enumeration representing different types of statistics.
     */
    @Getter
    public enum StatType {
        KILLS(),
        ASSISTS(),
        FINAL_KILLS(),
        FINAL_ASSISTS(),
        WITHER_DAMAGE(),
        DEATHS(),
        FINAL_DEATH();
    }
}

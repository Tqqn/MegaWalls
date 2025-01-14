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
     *
     * @param kills        The number of kills.
     * @param assists      The number of assists.
     * @param deaths       The number of deaths.
     * @param finalKills   The number of final kills.
     * @param finalAssists The number of final assists.
     * @param finalDeath   The number of final deaths.
     * @param witherDamage The amount of wither damage dealt.
     */
    public PlayerStats(int kills, int assists, int deaths, int finalKills, int finalAssists, int finalDeath, int witherDamage) {
        stats = new HashMap<>();
        stats.put(StatType.KILLS, kills);
        stats.put(StatType.ASSISTS, assists);
        stats.put(StatType.DEATHS, deaths);
        stats.put(StatType.FINAL_KILLS, finalKills);
        stats.put(StatType.FINAL_ASSISTS, finalAssists);
        stats.put(StatType.FINAL_DEATH, finalDeath);
        stats.put(StatType.WITHER_DAMAGE, witherDamage);
    }

    /**
     * Constructs a PlayerStats object with default statistics values initialized to zero.
     */
    public PlayerStats() {
        this(0, 0, 0, 0, 0, 0,0);
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

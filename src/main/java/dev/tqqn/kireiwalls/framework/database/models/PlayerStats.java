package dev.tqqn.kireiwalls.framework.database.models;

import lombok.Getter;

import java.util.HashMap;

public class PlayerStats {

    private final HashMap<StatType, Integer> stats;

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

    public PlayerStats() {
        this(0, 0, 0, 0, 0, 0,0);
    }

    public int getStat(StatType statType) {
        return stats.get(statType);
    }

    public void increaseStat(StatType statType) {
        stats.put(statType, stats.get(statType)+1);
    }


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

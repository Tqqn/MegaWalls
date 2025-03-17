package dev.tqqn.megawalls.common.utils.cooldown;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CooldownData {

    private final String key;
    private final UUID uuid;
    private final Long cooldown;

    private CooldownData(String key, UUID uuid, Long cooldown) {
        this.key = key;
        this.uuid = uuid;
        this.cooldown = cooldown;
    }

    public static CooldownData getData(String key, UUID uuid, Long cooldown) {
        return new CooldownData(key, uuid, cooldown);
    }
}

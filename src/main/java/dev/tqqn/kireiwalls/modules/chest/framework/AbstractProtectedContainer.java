package dev.tqqn.kireiwalls.modules.chest.framework;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class AbstractProtectedContainer {

    private final UUID ownerUUID;

    public AbstractProtectedContainer(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public boolean isOwner(UUID uuid) {
        return ownerUUID.equals(uuid);
    }
}

package dev.tqqn.megawalls.modules.chest;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.chest.framework.AbstractProtectedContainer;
import dev.tqqn.megawalls.modules.chest.framework.ChestItem;
import dev.tqqn.megawalls.modules.chest.framework.listeners.ChestListeners;
import dev.tqqn.megawalls.modules.chest.framework.types.GatheringChest;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

public class ChestModule extends AbstractModule {

    private final Map<Location, AbstractProtectedContainer> chestsMap = new HashMap<>();

    private final Set<ChestItem> gatheringChestItemsTemplate;
    @Getter final private int defaultChestSpawnRate;

    /**
     * Constructs a new AbstractModule object with the specified plugin and name.
     *
     * @param plugin The main plugin instance.
     */
    public ChestModule(MegaWalls plugin) {
        super(plugin, "Chest");
        this.gatheringChestItemsTemplate = new HashSet<>();
        this.defaultChestSpawnRate = 10;
    }

    @Override
    protected void onEnable() {
        register(new ChestListeners(this, getPlugin().getModuleManager().getModule(GameModule.class)));
        this.gatheringChestItemsTemplate.addAll(List.of(
                new ChestItem(ItemBuilder.getBuilder(Material.REDSTONE).build(), 0.5, 5, 10),
                new ChestItem(ItemBuilder.getBuilder(Material.COBBLESTONE).build(), 1.0, 10, 20),
                new ChestItem(ItemBuilder.getBuilder(Material.COOKED_BEEF).build(), 0.2, 3, 7),
                new ChestItem(ItemBuilder.getBuilder(Material.COOKED_CHICKEN).build(), 0.4, 3, 7),
                new ChestItem(ItemBuilder.getBuilder(Material.COOKED_COD).build(), 0.5, 3, 7)
        ));
    }

    @Override
    protected void onDisable() {
        gatheringChestItemsTemplate.clear();
    }

    public void spawnChest(Material brokenBlock, Location spawnLocation, UUID ownerUUID) {
        GatheringChest gatheringChest = new GatheringChest(spawnLocation, new HashSet<>(gatheringChestItemsTemplate), ownerUUID);
        gatheringChest.spawn(brokenBlock);
        chestsMap.put(spawnLocation, gatheringChest);
    }

    public void removeChest(Location location) {
        chestsMap.remove(location);
    }

    public AbstractProtectedContainer getProtectedChest(Location location) {
        return chestsMap.get(location);
    }

    public boolean isProtected(Location location, UUID possibleOwner) {
        if (chestsMap.containsKey(location)) return false;
        return !chestsMap.get(location).isOwner(possibleOwner);
    }
}

package dev.tqqn.megawalls.modules.enderchest;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.enderchest.commands.EnderChestCommand;
import dev.tqqn.megawalls.modules.enderchest.listeners.EnderChestListeners;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.utils.ChatUtil;
import dev.tqqn.megawalls.utils.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class EnderChestModule extends AbstractModule {

    public static final NamespacedKey ENDER_CHEST_KEY = new NamespacedKey(MegaWalls.getInstance(), "enderchest");

    private static final Map<UUID, Inventory> playerEnderChests = new HashMap<>();

    /**
     * The EnderchestModule manages the ingame enderchest of players.
     */
    public EnderChestModule(MegaWalls plugin) {
        super(plugin, "EnderChest");
    }

    @Override
    public void onEnable() {
        register(new EnderChestListeners(this, getPlugin().getModuleManager().getModule(GameModule.class)));
        register(new EnderChestCommand(this));
    }

    public void openEnderChest(Player player) {
        final Inventory enderChest = playerEnderChests.get(player.getUniqueId());
        if (enderChest == null) return;

        SoundUtil.ENDERCHEST_OPEN.playSoundForPlayer(player);
        SoundUtil.ENDERMAN_TELEPORT.playSoundForPlayer(player);
        player.openInventory(enderChest);
    }

    public void addEnderChest(Player player) {
        playerEnderChests.put(player.getUniqueId(), Bukkit.createInventory(player, 9*3, ChatUtil.format("<dark_purple>Ender Chest")));
    }

    public void removeEnderChest(Player player) {
        playerEnderChests.remove(player.getUniqueId());
    }

    public Inventory getEnderChest(Player player) {
        return playerEnderChests.get(player.getUniqueId());
    }
}

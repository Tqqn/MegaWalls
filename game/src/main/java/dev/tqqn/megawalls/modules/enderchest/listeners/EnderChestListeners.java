package dev.tqqn.megawalls.modules.enderchest.listeners;

import dev.tqqn.megawalls.modules.classes.ClassModule;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.enderchest.EnderChestModule;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.game.framework.events.GamePlayerKilledEvent;
import dev.tqqn.megawalls.modules.menu.framework.Menu;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import dev.tqqn.megawalls.modules.teams.framework.wither.GameWither;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public final class EnderChestListeners implements Listener {

    private final EnderChestModule enderChestModule;
    private final GameModule gameModule;

    @EventHandler
    public void onItemMoveOtherInv(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof Menu) return;
        if (event.getInventory().getType() != InventoryType.CHEST && event.getInventory().getType() != InventoryType.ENDER_CHEST) return;

        ItemStack itemStack = (event.getClick() == ClickType.NUMBER_KEY ? event.getWhoClicked().getInventory().getItem(event.getHotbarButton()) : event.getCurrentItem());
        if (itemStack == null) return;
        if (itemStack.getType() == Material.AIR) return;


        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        if (!itemMeta.getPersistentDataContainer().has(ClassModule.KIT_ITEM_KEY)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        final ItemStack itemStack = event.getItem();

        if (!itemStack.getItemMeta().getPersistentDataContainer().has(EnderChestModule.ENDER_CHEST_KEY)) return;

        final Player player = event.getPlayer();
        enderChestModule.openEnderChest(player);
    }

    @EventHandler
    public void onDeath(GamePlayerKilledEvent event) {
        if (!gameModule.isState(GameStates.ACTIVE)) return;
        final PlayerModel killedPlayerModel = event.getKilledPlayer();
        final GameTeam gameTeam = killedPlayerModel.getTempPlayerData().getGameTeam();
        if (gameTeam == null) return;
        if (gameTeam.getGameWither().getWitherStatus() != GameWither.WitherStatus.DEATH) return;

        final Inventory enderChest = enderChestModule.getEnderChest(killedPlayerModel.getPlayer());
        if (enderChest == null) return;
        final Location deathLocation = event.getDeathLocation();
        final World world = deathLocation.getWorld();
        for (ItemStack itemStack : enderChest.getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getType() == Material.AIR) continue;
            world.dropItem(deathLocation, itemStack);
        }

        enderChestModule.removeEnderChest(killedPlayerModel.getPlayer());
    }
}

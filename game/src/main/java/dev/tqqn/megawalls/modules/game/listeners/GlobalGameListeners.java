package dev.tqqn.megawalls.modules.game.listeners;

import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.game.GameModule;
import dev.tqqn.megawalls.modules.game.states.active.ActiveState;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.utils.ChatUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public final class GlobalGameListeners implements Listener {

    private final GameModule gameModule;

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        event.setCancelled(true);

        final PlayerModel playerModel = PlayerModule.getPlayerModel(event.getPlayer().getUniqueId());
        ChatUtil.sendPlayerMessage(playerModel, event.message());
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!gameModule.isState(GameStates.ACTIVE)) return;
        final ActiveState activeState = (ActiveState) gameModule.getCurrentState();
        final ActiveState.Cycle currentCycle = activeState.getCurrentCycle();
        if (currentCycle == ActiveState.Cycle.PRE_DM || currentCycle == ActiveState.Cycle.PREPARE || currentCycle == ActiveState.Cycle.END) event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();

        ItemMeta itemMeta = item.getItemMeta();

        if (!itemMeta.hasLocalizedName()) return;
        if (!itemMeta.getLocalizedName().equals("kit")) return;
        if (item.getType() != Material.POTION) {
            event.setCancelled(true);
            return;
        }

        event.getItemDrop().remove();
        final Player player = event.getPlayer();

        if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) == null) {
            event.setCancelled(true);
            return;
        }

        final ItemStack heldItem = player.getInventory().getItem(player.getInventory().getHeldItemSlot());

        if (heldItem.isSimilar(item)) {
            heldItem.setAmount(heldItem.getAmount()+1);
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getItemInHand().getType() == Material.AIR) return;
        ItemStack item = event.getItemInHand();
        ItemMeta itemMeta = item.getItemMeta();

        if (!itemMeta.hasLocalizedName()) return;
        if (itemMeta.getLocalizedName().equals("kit")) event.setCancelled(true);
    }

}

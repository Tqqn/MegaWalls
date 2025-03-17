package dev.tqqn.megawalls.modules.setup.listeners;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.region.framework.Cuboid;
import dev.tqqn.megawalls.modules.setup.framework.data.SetupTeamSettings;
import dev.tqqn.megawalls.modules.setup.SetupModule;
import dev.tqqn.megawalls.modules.setup.framework.model.SetupPlayer;
import dev.tqqn.megawalls.modules.setup.menu.SetupMenu;
import dev.tqqn.megawalls.common.utils.ChatUtil;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class SetupListener implements Listener {

    private final SetupModule setupModule;

    public SetupListener() {
        this.setupModule = MegaWalls.getInstance().getModuleManager().getModule(SetupModule.class);

    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        SetupPlayer setupPlayer = null;
        final Player player = event.getPlayer();
        if (SetupModule.getSetupPlayer(player.getUniqueId()) == null) {
            setupModule.addSetupPlayer(player);
            setupPlayer = SetupModule.getSetupPlayer(player.getUniqueId());
        }

        if (setupPlayer == null) return;

        if (!setupPlayer.isBuild()) {
            player.getInventory().clear();
            player.setGameMode(GameMode.CREATIVE);
            setupModule.giveSetupItems(player);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        final SetupPlayer setupPlayer = SetupModule.getSetupPlayer(event.getPlayer().getUniqueId());

        if (!setupPlayer.isBuild()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            final SetupPlayer setupPlayer = SetupModule.getSetupPlayer(player.getUniqueId());

            if (!setupPlayer.isBuild()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final SetupPlayer setupPlayer = SetupModule.getSetupPlayer(event.getPlayer().getUniqueId());

        if (!setupPlayer.isBuild()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        final ItemStack itemStack = event.getItem();
        final PersistentDataContainer itemPDC = itemStack.getItemMeta().getPersistentDataContainer();

        final Player player = event.getPlayer();

        final SetupPlayer setupPlayer = SetupModule.getSetupPlayer(player.getUniqueId());

        if (itemPDC.has(SetupModule.SETUP_SELECTION_AXE_KEY)) {
            String whatPos = null;
            if (event.getClickedBlock() == null) return;
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                setupPlayer.setSelectionOne(event.getClickedBlock().getLocation());
                whatPos = "<green>Selected Position-1.";
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                setupPlayer.setSelectionTwo(event.getClickedBlock().getLocation());
                whatPos = "<green>Selected Position-2.";
            }

            if (whatPos == null) return;

            player.sendMessage(ChatUtil.format(whatPos));
        }

        if (itemPDC.has(SetupModule.SETUP_LOBBY_SPAWN_LOC_KEY)) {
            setupModule.getSetupGameSettings().setLobbyLocation(player.getLocation());
            player.sendMessage(ChatUtil.format("<green>Selected the Lobby Location."));
        }

        if (itemPDC.has(SetupModule.SETUP_GAME_SETUP_KEY)) {
            setupModule.giveGameSetupItems(player);
            player.sendMessage(ChatUtil.format("<green>Enabled Game Setup."));
        }

        if (itemPDC.has(SetupModule.SETUP_GAME_WALL_CUBOID_KEY)) {
            if (setupPlayer.getSelectionOne() == null || setupPlayer.getSelectionTwo() == null) {
                player.sendMessage(ChatUtil.format("<red>One or two selections are null. Please select a cuboid with your axe!"));
                return;
            }
            setupModule.getSetupGameSettings().getWallCuboids().add(new Cuboid(setupPlayer.getSelectionOne(), setupPlayer.getSelectionTwo()));
            event.getPlayer().sendMessage(ChatUtil.format("<green>Added Cuboid to wall Cuboids. Currently added: <red>" + setupModule.getSetupGameSettings().getWallCuboids().size()));
            setupPlayer.setSelectionOne(null);
            setupPlayer.setSelectionTwo(null);
        }

        if (itemPDC.has(SetupModule.SETUP_GAME_MIDDLE_CUBOID_KEY)) {
            if (setupPlayer.getSelectionOne() == null || setupPlayer.getSelectionTwo() == null) {
                player.sendMessage(ChatUtil.format("<red>One or two selections are null. Please select a cuboid with your axe!"));
                return;
            }
            setupModule.getSetupGameSettings().setMiddle(new Cuboid(setupPlayer.getSelectionOne(), setupPlayer.getSelectionTwo()));
            event.getPlayer().sendMessage(ChatUtil.format("<green>Selected the Middle Cuboid."));
            setupPlayer.setSelectionOne(null);
            setupPlayer.setSelectionTwo(null);
        }

        if (itemPDC.has(SetupModule.SETUP_TEAM_SETUP_KEY)) {
            new SetupMenu(player).open();
        }

        if (itemPDC.has(SetupModule.SETUP_SAVE_KEY)) {
            boolean isSaved = setupModule.getSetupGameSettings().save(player);

            for (SetupTeamSettings setupTeamSettings : setupModule.getTeamSettingsMap().values()) {
                if (!setupTeamSettings.save(player)) {
                    isSaved = false;
                }
            }
            if (!isSaved) {
                player.sendMessage(ChatUtil.format("<red>Could not save. Because something is null."));
            } else {
                player.sendMessage(ChatUtil.format("<green>Saved settings!"));
            }
        }

        if (itemPDC.has(SetupModule.SETUP_WITHER_SPAWN_LOC_KEY)) {
            String team = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MegaWalls.getInstance(), "team"), PersistentDataType.STRING);
            setupModule.getTeamSettingsMap().get(team).setWitherLocation(player.getLocation());
            event.getPlayer().sendMessage(ChatUtil.format("<green>Selected the Wither Location of team: " + team + "."));
        }

        if (itemPDC.has(SetupModule.SETUP_TEAM_SPAWN_LOC_KEY)) {
            String team = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MegaWalls.getInstance(), "team"), PersistentDataType.STRING);
            setupModule.getTeamSettingsMap().get(team).setSpawnLocation(player.getLocation());
            event.getPlayer().sendMessage(ChatUtil.format("<green>Selected the Spawn Location of team: " + team + "."));
        }

        if (itemPDC.has(SetupModule.SETUP_TEAM_PROTECTION_CUBOID_KEY)) {
            if (setupPlayer.getSelectionOne() == null || setupPlayer.getSelectionTwo() == null) {
                player.sendMessage(ChatUtil.format("<red>One or two selections are null. Please select a cuboid with your axe!"));
                return;
            }
            String team = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MegaWalls.getInstance(), "team"), PersistentDataType.STRING);
            setupModule.getTeamSettingsMap().get(team).setTeamProtectionCuboid(new Cuboid(setupPlayer.getSelectionOne(), setupPlayer.getSelectionTwo()));
            event.getPlayer().sendMessage(ChatUtil.format("<green>Selected the Team Protection Cuboid of team: " + team + "."));
            setupPlayer.setSelectionOne(null);
            setupPlayer.setSelectionTwo(null);
        }

        if (itemPDC.has(SetupModule.SETUP_WITHER_CUBOID_KEY)) {
            if (setupPlayer.getSelectionOne() == null || setupPlayer.getSelectionTwo() == null) {
                player.sendMessage(ChatUtil.format("<red>One or two selections are null. Please select a cuboid with your axe!"));
                return;
            }
            String team = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MegaWalls.getInstance(), "team"), PersistentDataType.STRING);
            setupModule.getTeamSettingsMap().get(team).setWitherProtectionCuboid(new Cuboid(setupPlayer.getSelectionOne(), setupPlayer.getSelectionTwo()));
            event.getPlayer().sendMessage(ChatUtil.format("<green>Selected the Wither Protection Cuboid of team: " + team + "."));
            setupPlayer.setSelectionOne(null);
            setupPlayer.setSelectionTwo(null);
        }

        event.setCancelled(true);
    }
}

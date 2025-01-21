package dev.tqqn.megawalls.modules.setup.listeners;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.region.framework.Cuboid;
import dev.tqqn.megawalls.modules.setup.framework.data.SetupTeamSettings;
import dev.tqqn.megawalls.modules.setup.SetupModule;
import dev.tqqn.megawalls.modules.setup.framework.model.SetupPlayer;
import dev.tqqn.megawalls.modules.setup.menu.SetupMenu;
import dev.tqqn.megawalls.utils.ChatUtil;
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
import org.bukkit.persistence.PersistentDataType;

public final class SetupListener implements Listener {

    private final SetupModule setupModule;

    public SetupListener() {
        this.setupModule = MegaWalls.getInstance().getModuleManager().getModule(SetupModule.class);

    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        SetupPlayer setupPlayer = null;
        if (SetupModule.getSetupPlayer(event.getPlayer().getUniqueId()) == null) {
            setupModule.addSetupPlayer(event.getPlayer());
            setupPlayer = SetupModule.getSetupPlayer(event.getPlayer().getUniqueId());
        }

        if (setupPlayer == null) return;

        if (!setupPlayer.isBuild()) {
            event.getPlayer().getInventory().clear();
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            setupModule.giveSetupItems(event.getPlayer());
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
        if (!itemStack.getItemMeta().hasLocalizedName()) return;
        String localName = itemStack.getItemMeta().getLocalizedName();

        final Player player = event.getPlayer();

        final SetupPlayer setupPlayer = SetupModule.getSetupPlayer(player.getUniqueId());

        switch (localName) {
            case "selection_axe" -> {
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

            case "lobby_spawn_loc" -> {
                setupModule.getSetupGameSettings().setLobbyLocation(player.getLocation());
                player.sendMessage(ChatUtil.format("<green>Selected the Lobby Location."));
            }

            case "game_setup" -> {
                setupModule.giveGameSetupItems(player);
                player.sendMessage(ChatUtil.format("<green>Enabled Game Setup."));
            }

            case "game_wall_cuboid" -> {
                if (setupPlayer.getSelectionOne() == null || setupPlayer.getSelectionTwo() == null) {
                    player.sendMessage(ChatUtil.format("<red>One or two selections are null. Please select a cuboid with your axe!"));
                    return;
                }
                setupModule.getSetupGameSettings().getWallCuboids().add(new Cuboid(setupPlayer.getSelectionOne(), setupPlayer.getSelectionTwo()));
                event.getPlayer().sendMessage(ChatUtil.format("<green>Added Cuboid to wall Cuboids. Currently added: <red>" + setupModule.getSetupGameSettings().getWallCuboids().size()));
                setupPlayer.setSelectionOne(null);
                setupPlayer.setSelectionTwo(null);
            }

            case "game_middle_cuboid" -> {
                if (setupPlayer.getSelectionOne() == null || setupPlayer.getSelectionTwo() == null) {
                    player.sendMessage(ChatUtil.format("<red>One or two selections are null. Please select a cuboid with your axe!"));
                    return;
                }
                setupModule.getSetupGameSettings().setMiddle(new Cuboid(setupPlayer.getSelectionOne(), setupPlayer.getSelectionTwo()));
                event.getPlayer().sendMessage(ChatUtil.format("<green>Selected the Middle Cuboid."));
                setupPlayer.setSelectionOne(null);
                setupPlayer.setSelectionTwo(null);
            }

            case "team_setup" -> new SetupMenu(player).open();

            case "save" -> {
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

            case "wither_spawn_loc" -> {
                String team = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MegaWalls.getInstance(), "team"), PersistentDataType.STRING);
                setupModule.getTeamSettingsMap().get(team).setWitherLocation(player.getLocation());
                event.getPlayer().sendMessage(ChatUtil.format("<green>Selected the Wither Location of team: " + team + "."));
            }

            case "team_spawn_loc" -> {
                String team = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MegaWalls.getInstance(), "team"), PersistentDataType.STRING);
                setupModule.getTeamSettingsMap().get(team).setSpawnLocation(player.getLocation());
                event.getPlayer().sendMessage(ChatUtil.format("<green>Selected the Spawn Location of team: " + team + "."));
            }

            case "team_protection_cuboid" -> {
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

            case "wither_cuboid" -> {
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
        }
        event.setCancelled(true);
    }
}

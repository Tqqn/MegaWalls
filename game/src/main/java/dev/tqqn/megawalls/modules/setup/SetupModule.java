package dev.tqqn.megawalls.modules.setup;

import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.setup.framework.data.SetupGameSettings;
import dev.tqqn.megawalls.modules.setup.framework.data.SetupTeamSettings;
import dev.tqqn.megawalls.modules.setup.commands.BuildCommand;
import dev.tqqn.megawalls.modules.setup.commands.SelectCommand;
import dev.tqqn.megawalls.modules.setup.commands.SetupCommand;
import dev.tqqn.megawalls.modules.setup.listeners.SetupListener;
import dev.tqqn.megawalls.modules.setup.framework.model.SetupPlayer;
import dev.tqqn.megawalls.common.utils.ChatUtil;
import dev.tqqn.megawalls.common.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SetupModule extends AbstractModule {

    public static final NamespacedKey SETUP_SELECTION_AXE_KEY = new NamespacedKey(MegaWalls.getInstance(), "selection_axe");
    public static final NamespacedKey SETUP_GAME_SETUP_KEY = new NamespacedKey(MegaWalls.getInstance(), "game_setup");
    public static final NamespacedKey SETUP_TEAM_SETUP_KEY = new NamespacedKey(MegaWalls.getInstance(), "team_setup");
    public static final NamespacedKey SETUP_SAVE_KEY = new NamespacedKey(MegaWalls.getInstance(), "save");
    public static final NamespacedKey SETUP_GAME_MIDDLE_CUBOID_KEY = new NamespacedKey(MegaWalls.getInstance(), "game_middle_cuboid");
    public static final NamespacedKey SETUP_LOBBY_SPAWN_LOC_KEY = new NamespacedKey(MegaWalls.getInstance(), "lobby_spawn_loc");
    public static final NamespacedKey SETUP_GAME_WALL_CUBOID_KEY = new NamespacedKey(MegaWalls.getInstance(), "game_wall_cuboid");
    public static final NamespacedKey SETUP_TEAM_PROTECTION_CUBOID_KEY = new NamespacedKey(MegaWalls.getInstance(), "team_protection_cuboid");
    public static final NamespacedKey SETUP_WITHER_CUBOID_KEY = new NamespacedKey(MegaWalls.getInstance(), "wither_cuboid");
    public static final NamespacedKey SETUP_WITHER_SPAWN_LOC_KEY = new NamespacedKey(MegaWalls.getInstance(), "wither_spawn_loc");
    public static final NamespacedKey SETUP_TEAM_SPAWN_LOC_KEY = new NamespacedKey(MegaWalls.getInstance(), "team_spawn_loc");

    private final Map<UUID, ItemStack[]> lastInventories;
    private static final Map<UUID, SetupPlayer> CACHED_SETUP_PLAYERS = new HashMap<>();
    @Getter private final Map<String, SetupTeamSettings> teamSettingsMap;
    @Getter private final SetupGameSettings setupGameSettings;

     /**
     * Constructs a new AbstractModule object with the specified plugin and name.
     *
     * @param plugin The main plugin instance.
     */
    public SetupModule(MegaWalls plugin) {
        super(plugin, "Setup");
        this.lastInventories = new HashMap<>();
        this.teamSettingsMap = new HashMap<>();

        teamSettingsMap.put("RED", new SetupTeamSettings(Teams.RED));
        teamSettingsMap.put("BLUE", new SetupTeamSettings(Teams.BLUE));
        teamSettingsMap.put("GREEN", new SetupTeamSettings(Teams.GREEN));
        teamSettingsMap.put("YELLOW", new SetupTeamSettings(Teams.YELLOW));

        this.setupGameSettings = new SetupGameSettings();
    }

    @Override
    protected void onEnable() {
        register(new SetupListener());
        register(new SetupCommand());
        register(new SelectCommand());
        register(new BuildCommand());
    }

    public void enableWorld(String world) {
        new WorldCreator(world).createWorld();
    }

    public void teleportWorld(String worldName, Player player) {
        final World world = Bukkit.getWorld(worldName);
        if (world == null) return;
        player.teleport(world.getSpawnLocation());
    }

    public void addSetupPlayer(Player player) {
        CACHED_SETUP_PLAYERS.put(player.getUniqueId(), new SetupPlayer(player));
    }

    public void setBuild(SetupPlayer setupPlayer) {
        final Player player = setupPlayer.getPlayer();
        if (player == null) return;
        final UUID uuid = player.getUniqueId();

        if (!setupPlayer.isBuild()) {
            setupPlayer.setBuild(true);
            player.getInventory().clear();
            if (lastInventories.containsKey(uuid)) {
                player.getInventory().setContents(lastInventories.get(uuid));
                lastInventories.remove(uuid);
            }
            player.sendMessage(ChatUtil.format("<green>Enabled build-mode."));
        } else {
            setupPlayer.setBuild(false);
            lastInventories.put(uuid, player.getInventory().getArmorContents());
            player.getInventory().clear();
            giveSetupItems(player);
            player.sendMessage(ChatUtil.format("<red>Disabled build-mode."));
        }
    }

    public void giveSetupItems(Player player) {
        player.getInventory().setItem(0, ItemBuilder.getBuilder(Material.NETHERITE_AXE).setDisplayName("&cSelection Axe").addEmptyPDCTag(SETUP_SELECTION_AXE_KEY).build());
        player.getInventory().setItem(1, ItemBuilder.getBuilder(Material.RECOVERY_COMPASS).setDisplayName("Game-Setup").addEmptyPDCTag(SETUP_GAME_SETUP_KEY).build());
        player.getInventory().setItem(2, ItemBuilder.getBuilder(Material.TOTEM_OF_UNDYING).setDisplayName("&cTeam-Setup").addEmptyPDCTag(SETUP_TEAM_SETUP_KEY).build());
        player.getInventory().setItem(8, ItemBuilder.getBuilder(Material.GREEN_DYE).setDisplayName("&aSave!").addEmptyPDCTag(SETUP_SAVE_KEY).build());
    }

    public void giveGameSetupItems(Player player) {
        player.getInventory().clear();
        giveSetupItems(player);
        player.getInventory().setItem(3, ItemBuilder.getBuilder(Material.WHITE_STAINED_GLASS).setDisplayName("&cSelect Middle Cuboid").addEmptyPDCTag(SETUP_GAME_MIDDLE_CUBOID_KEY).build());
        player.getInventory().setItem(4, ItemBuilder.getBuilder(Material.NETHER_STAR).setDisplayName("&cSet Lobby Location").addEmptyPDCTag(SETUP_LOBBY_SPAWN_LOC_KEY).build());
        player.getInventory().setItem(5, ItemBuilder.getBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&cSelect Wall Cuboid").addEmptyPDCTag(SETUP_GAME_WALL_CUBOID_KEY).build());
    }

    public void giveTeamSetupItems(Player player, Teams team) {
        player.getInventory().clear();
        giveSetupItems(player);
        player.getInventory().setItem(4, ItemBuilder.getBuilder(Material.WHITE_STAINED_GLASS).setDisplayName("&cSave Team Protection Cuboid").addEmptyPDCTag(SETUP_TEAM_PROTECTION_CUBOID_KEY).addPDCTag(getPlugin(), "team", team.name()).build());
        player.getInventory().setItem(5, ItemBuilder.getBuilder(Material.BLACK_STAINED_GLASS).setDisplayName("&cSave Wither Cuboid").addEmptyPDCTag(SETUP_WITHER_CUBOID_KEY).addPDCTag(getPlugin(), "team", team.name()).build());
        player.getInventory().setItem(6, ItemBuilder.getBuilder(Material.WITHER_SKELETON_SKULL).setDisplayName("&cSet Wither Spawn Location").addEmptyPDCTag(SETUP_WITHER_SPAWN_LOC_KEY).addPDCTag(getPlugin(), "team", team.name()).build());
        player.getInventory().setItem(7, ItemBuilder.getBuilder(team.getBedMaterial()).setDisplayName("&cSet Team Spawn Location").addEmptyPDCTag(SETUP_TEAM_SPAWN_LOC_KEY).addPDCTag(getPlugin(), "team", team.name()).build());
    }

    public static SetupPlayer getSetupPlayer(UUID uuid) {
        return CACHED_SETUP_PLAYERS.get(uuid);
    }

    @Getter
    public enum Teams {
        RED("Red", Material.RED_BED),
        BLUE("Blue", Material.BLUE_BED),
        GREEN("Green", Material.GREEN_BED),
        YELLOW("Yellow", Material.YELLOW_BED);

        private final String name;
        private final Material bedMaterial;

        Teams(String name, Material bedMaterial) {
            this.name = name;
            this.bedMaterial = bedMaterial;
        }
    }
}

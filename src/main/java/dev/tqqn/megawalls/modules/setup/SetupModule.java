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
import dev.tqqn.megawalls.utils.ChatUtil;
import dev.tqqn.megawalls.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SetupModule extends AbstractModule {

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
        addComponent(SetupListener.class);
        addComponent(SelectCommand.class, "select");
        addComponent(SetupCommand.class, "setup");
        addComponent(BuildCommand.class, "build");
    }

    public void enableWorld(String world) {
        new WorldCreator(world).createWorld();
    }

    public void teleportWorld(String world, Player player) {
        if (Bukkit.getWorld(world) == null) return;
        player.teleport(Bukkit.getWorld(world).getSpawnLocation());
    }

    public void addSetupPlayer(Player player) {
        CACHED_SETUP_PLAYERS.put(player.getUniqueId(), new SetupPlayer(player));
    }

    public void setBuild(SetupPlayer setupPlayer) {
        if (setupPlayer.getPlayer() == null) return;

        if (!setupPlayer.isBuild()) {
            setupPlayer.setBuild(true);
            setupPlayer.getPlayer().getInventory().clear();
            if (lastInventories.containsKey(setupPlayer.getUuid())) {
                setupPlayer.getPlayer().getInventory().setContents(lastInventories.get(setupPlayer.getUuid()));
                lastInventories.remove(setupPlayer.getUuid());
            }
            setupPlayer.getPlayer().sendMessage(ChatUtil.format("<green>Enabled build-mode."));
        } else {
            setupPlayer.setBuild(false);
            lastInventories.put(setupPlayer.getUuid(), setupPlayer.getPlayer().getInventory().getArmorContents());
            setupPlayer.getPlayer().getInventory().clear();
            giveSetupItems(setupPlayer.getPlayer());
            setupPlayer.getPlayer().sendMessage(ChatUtil.format("<red>Disabled build-mode."));
        }
    }

    public void giveSetupItems(Player player) {
        player.getInventory().setItem(0, ItemBuilder.getBuilder(Material.NETHERITE_AXE).setDisplayName("&cSelection Axe").setLocalizedName("selection_axe").build());
        player.getInventory().setItem(1, ItemBuilder.getBuilder(Material.RECOVERY_COMPASS).setDisplayName("Game-Setup").setLocalizedName("game_setup").build());
        player.getInventory().setItem(2, ItemBuilder.getBuilder(Material.TOTEM_OF_UNDYING).setDisplayName("&cTeam-Setup").setLocalizedName("team_setup").build());
        player.getInventory().setItem(8, ItemBuilder.getBuilder(Material.GREEN_DYE).setDisplayName("&aSave!").setLocalizedName("save").build());
    }

    public void giveGameSetupItems(Player player) {
        player.getInventory().clear();
        giveSetupItems(player);
        player.getInventory().setItem(3, ItemBuilder.getBuilder(Material.WHITE_STAINED_GLASS).setDisplayName("&cSelect Middle Cuboid").setLocalizedName("game_middle_cuboid").build());
        player.getInventory().setItem(4, ItemBuilder.getBuilder(Material.NETHER_STAR).setDisplayName("&cSet Lobby Location").setLocalizedName("lobby_spawn_loc").build());
        player.getInventory().setItem(5, ItemBuilder.getBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("&cSelect Wall Cuboid").setLocalizedName("game_wall_cuboid").build());
    }

    public void giveTeamSetupItems(Player player, Teams team) {
        player.getInventory().clear();
        giveSetupItems(player);
        player.getInventory().setItem(4, ItemBuilder.getBuilder(Material.WHITE_STAINED_GLASS).setDisplayName("&cSave Team Protection Cuboid").setLocalizedName("team_protection_cuboid").addPDCTag("team", team.name()).build());
        player.getInventory().setItem(5, ItemBuilder.getBuilder(Material.BLACK_STAINED_GLASS).setDisplayName("&cSave Wither Cuboid").setLocalizedName("wither_cuboid").addPDCTag("team", team.name()).build());
        player.getInventory().setItem(6, ItemBuilder.getBuilder(Material.WITHER_SKELETON_SKULL).setDisplayName("&cSet Wither Spawn Location").setLocalizedName("wither_spawn_loc").addPDCTag("team", team.name()).build());
        player.getInventory().setItem(7, ItemBuilder.getBuilder(team.getBedMaterial()).setDisplayName("&cSet Team Spawn Location").setLocalizedName("team_spawn_loc").addPDCTag("team", team.name()).build());
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

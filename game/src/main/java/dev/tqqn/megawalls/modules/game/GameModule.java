package dev.tqqn.megawalls.modules.game;

import com.fastasyncworldedit.core.Fawe;
import com.google.common.collect.ImmutableSet;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockTypes;
import dev.tqqn.megawalls.MegaWalls;
import dev.tqqn.megawalls.modules.AbstractModule;
import dev.tqqn.megawalls.modules.classes.framework.AbstractClass;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.enderchest.EnderChestModule;
import dev.tqqn.megawalls.modules.game.framework.AbstractGameState;
import dev.tqqn.megawalls.modules.game.framework.GameStates;
import dev.tqqn.megawalls.modules.game.states.end.EndState;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import dev.tqqn.megawalls.modules.teams.framework.wither.GameWither;
import dev.tqqn.megawalls.modules.region.framework.Cuboid;
import dev.tqqn.megawalls.modules.arena.ArenaModule;
import dev.tqqn.megawalls.modules.classes.ClassModule;
import dev.tqqn.megawalls.modules.database.DatabaseModule;
import dev.tqqn.megawalls.modules.game.commands.*;
import dev.tqqn.megawalls.modules.game.listeners.GlobalGameListeners;
import dev.tqqn.megawalls.modules.game.states.active.ActiveState;
import dev.tqqn.megawalls.modules.game.states.lobby.LobbyState;
import dev.tqqn.megawalls.modules.teams.TeamModule;
import dev.tqqn.megawalls.modules.player.PlayerModule;
import dev.tqqn.megawalls.utils.ChatUtil;
import dev.tqqn.megawalls.utils.FinalItems;
import dev.tqqn.megawalls.utils.ItemBuilder;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import lombok.Getter;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The GameModule class manages the game state and settings.
 */
public final class GameModule extends AbstractModule {

    public static final NamespacedKey SKIN_SELECTOR_KEY = new NamespacedKey(MegaWalls.getInstance(), "skin_selector");
    public static final NamespacedKey CLASS_SELECTOR_KEY = new NamespacedKey(MegaWalls.getInstance(), "class_selector");

    @Getter private AbstractGameState currentState;
    private final Set<PlayerModel> inGamePlayers = new HashSet<>();
    private final Set<PlayerModel> spectators = new HashSet<>();

    private final DatabaseModule databaseModule;
    @Getter private ArenaModule arenaModule;
    private TeamModule teamModule;

    private EditSession[] wallsEditSession;

    public GameModule(MegaWalls plugin, DatabaseModule databaseModule) {
        super(plugin, "Game");
        currentState = new LobbyState(this);
        this.databaseModule = databaseModule;
    }

    @Override
    public void onEnable() {
        this.arenaModule = this.getPlugin().getModuleManager().getModule(ArenaModule.class);
        this.teamModule = this.getPlugin().getModuleManager().getModule(TeamModule.class);
        register(new GlobalGameListeners(this));
        register(new AdminCommand(teamModule));
        register(new GameCommands(this));
        currentState.enable();
        registerScoreboardTeam();
        initScoreboardTask();
    }

    @Override
    public void onDisable() {
        currentState.disable();
    }

    private void registerScoreboardTeam() {
        Bukkit.getScheduler().runTask(getPlugin(), () -> {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

            final Objective onTab = scoreboard.getObjective("onTab");
            final Objective underName = scoreboard.getObjective("underName");

            if (onTab != null) {
                onTab.unregister();
            }
            if (underName != null) {
                underName.unregister();
            }

            for (Team team : scoreboard.getTeams()) {
                team.unregister();
            }
        });
    }

    public Set<PlayerModel> getInGamePlayers() {
        return ImmutableSet.copyOf(inGamePlayers);
    }

    public void addInGamePlayer(PlayerModel playerModel) {
        inGamePlayers.add(playerModel);
    }

    public void removeInGamePlayer(PlayerModel playerModel) {
        inGamePlayers.remove(playerModel);
    }

    public Set<PlayerModel> getSpectators() {
        return ImmutableSet.copyOf(spectators);
    }

    public void addSpectator(PlayerModel playerModel) {
        spectators.add(playerModel);
    }

    public void removeSpectator(PlayerModel playerModel) {
        spectators.remove(playerModel);
    }

    private void initScoreboardTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (PlayerModule.getPlayerModel(player.getUniqueId()).getTempPlayerData().getCurrentScoreboard() == null) continue;
                PlayerModule.getPlayerModel(player.getUniqueId()).getTempPlayerData().getCurrentScoreboard().update();
                if (isState(GameStates.ACTIVE)) {
                    updateHealth(player);
                }
            }
        }, 0, 10L);
    }

    /**
     * Updates player health on the scoreboard.
     *
     * @param player The player whose health to update.
     */
    private void updateHealth(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective underName = scoreboard.getObjective("underName");

        final AttributeInstance genericMaxHealthAtt = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        int maxHealth = 0;

        if (genericMaxHealthAtt != null) {
            maxHealth = (int) genericMaxHealthAtt.getBaseValue();
        }

        if (underName != null) {
            Score healthScore = underName.getScore(player);

            healthScore.numberFormat(NumberFormat.styled(Style.style().color(TextColor.color(ChatUtil.getHealthColor(maxHealth, (int) player.getHealth()))).build()));

            underName.getScore(player).setScore((int) player.getHealth());
        }

        if (!(currentState instanceof ActiveState activeState)) return;
        if (activeState.getCurrentCycle() != ActiveState.Cycle.DM) return;

        Objective onTab = scoreboard.getObjective("onTab");

        if (onTab != null) {
            Score healthScore = onTab.getScore(player);

            healthScore.numberFormat(NumberFormat.styled(Style.style().color(TextColor.color(ChatUtil.getHealthColor(maxHealth, (int) player.getHealth()))).build()));

            onTab.getScore(player).setScore((int) player.getHealth());
        }
    }

    /**
     * Sets the game state.
     *
     * @param gameState The new game state.
     */
    public void setGameState(GameStates gameState) {
        if (currentState.getGameStates() == gameState) return;

        AbstractGameState newState = null;

        switch (gameState) {
            case ACTIVE -> {
                if (isState(GameStates.ACTIVE)) return;
                if (isState(GameStates.END)) return;
                newState = new ActiveState(this);
            }

            case END -> {
                if (currentState instanceof ActiveState activeState) {
                    if (activeState.getCurrentCycle() != ActiveState.Cycle.END) {
                        activeState.setCycle(ActiveState.Cycle.END);
                        return;
                    }
                }
                newState = new EndState(this, databaseModule);
            }

            case FORCE_END -> {
                if (isState(GameStates.FORCE_END)) return;
                newState = new EndState(this, databaseModule);
            }
        }

        if (newState == null) return;
        currentState.disable();

        currentState = newState;
        newState.enable();
    }

    /** Shuffles players and assigns teams. */
    public void shufflePlayers() {
        for (PlayerModel playerModel : getInGamePlayers()) {
            if (playerModel.getPlayer() == null) return;

            if (playerModel.getTempPlayerData().getCurrentClass() == null) {
                playerModel.getTempPlayerData().setCurrentClass(ClassModule.getClasses().get(ThreadLocalRandom.current().nextInt(ClassModule.getClasses().size()))); // Random Class
                playerModel.getPlayer().sendMessage(ChatUtil.format("<green>You got <gold>" + playerModel.getTempPlayerData().getCurrentClass().getName() + " <green>as random Class!"));
            }

            if (playerModel.getTempPlayerData().getGameTeam() == null) {
                if (playerModel.getTempPlayerData().isSpectatorMode()) {
                    playerModel.getTempPlayerData().setSpectatorMode(true);
                } else {
                    this.teamModule.whichTeamIsSmaller().addPlayer(playerModel);
                }
            }
        }

        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(this.getPlugin(), this::spawnPlayers);
        } else {
            this.spawnPlayers();
        }

    }

    /** Initializes health display below player names. */
    private void initHealthBelowName() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Objective underName = scoreboard.registerNewObjective("underName", Criteria.DUMMY, ChatUtil.format("<red>❤"));
        underName.setDisplaySlot(DisplaySlot.BELOW_NAME);
        underName.setRenderType(RenderType.INTEGER);
    }

    /** Initializes health display on the player list tab. */
    public void initHealthOnTab() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Objective onTab = scoreboard.registerNewObjective("onTab", Criteria.DUMMY, ChatUtil.format(""));
        onTab.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        onTab.setRenderType(RenderType.INTEGER);

        for (PlayerModel playerModel : getInGamePlayers()) {
            Score score = onTab.getScore(playerModel.getPlayer());
            int maxHealth = (int) playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            score.numberFormat(NumberFormat.styled(Style.style().color(TextColor.color(ChatUtil.getHealthColor(maxHealth, (int) playerModel.getPlayer().getHealth()))).build()));
        }
    }

    /** Spawns players in their team locations. */
    private void spawnPlayers() {
        initHealthBelowName();
        final EnderChestModule enderChestModule = getPlugin().getModuleManager().getModule(EnderChestModule.class);
        for (PlayerModel playerModel : getInGamePlayers()) {
            if (!playerModel.getTempPlayerData().isSpectatorMode()) {
                double health = 40.0;
                if (playerModel.getTempPlayerData().getCurrentClass().isPrestigeOne()) health = 44.0;
                teleportPlayerToSpawn(playerModel);
                playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
                playerModel.getPlayer().setHealth(health);
                playerModel.getPlayer().getInventory().clear();
                playerModel.getTempPlayerData().getCurrentClass().applyKit(playerModel);
                playerModel.getTempPlayerData().getCurrentClass().applySkin(playerModel);
                enderChestModule.addEnderChest(playerModel.getPlayer());
            }
        }
    }

    public void teleportPlayerToSpawn(PlayerModel playerModel) {
        playerModel.getPlayer().teleport(playerModel.getTempPlayerData().getGameTeam().getGameTeamSettings().getSpawnLocation());
    }

    public void teleportPlayersToSpawn() {
        for (PlayerModel playerModel : getInGamePlayers()) {
            teleportPlayerToSpawn(playerModel);
        }
    }

    /** Makes walls fall. */
    public void wallsFall() {
        List<EditSession> editSessions = new ArrayList<>();
        for (Cuboid cuboid : arenaModule.getCurrentArena().getArenaSettings().getMiddleCuboids()) {
            EditSession editSession = Fawe.instance().getWorldEdit().newEditSession(BukkitAdapter.adapt(cuboid.getWorld()));
            Region region = new CuboidRegion(BlockVector3.at(cuboid.getPoint1().x(), cuboid.getPoint1().y(), cuboid.getPoint1().z()), BlockVector3.at(cuboid.getPoint2().x(), cuboid.getPoint2().y(), cuboid.getPoint2().z()));
            editSession.setBlocks(region, BlockTypes.AIR);
            editSession.flushQueue();
            editSessions.add(editSession);
        }
        this.wallsEditSession = editSessions.toArray(EditSession[]::new);
    }

    /** Undoes wall fall. */
    public void undoWallsFall() {
        for (EditSession editSession : this.wallsEditSession) {
            editSession.undo(editSession);
        }
    }

    /** Checks if all withers are dead. */
    public boolean areAllWithersDead() {
        int count = 0;
        for (GameTeam gameTeam : TeamModule.getGameTeams().values()) {
            if (gameTeam.getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH) {
                ++count;
            }
        }
        return count == 4;
    }

    public GameTeam getWinningTeamByDraw() {

        GameTeam possibleWinner = null;
        for (GameTeam gameTeam : TeamModule.getGameTeams().values()) {
            if (possibleWinner == null) {
                possibleWinner = gameTeam;
                continue;
            }
            if (possibleWinner.getCurrentFinalKills() < gameTeam.getCurrentFinalKills()) possibleWinner = gameTeam;
        }

        return possibleWinner;
    }

    public boolean isState(GameStates state) {
        return currentState.getGameStates() == state;
    }

    /** Checks if the game can start. */
    public boolean canStart() {
        return true; // For debug purposes still on force true
    }

    public void giveLobbyItems(PlayerModel playerModel) {
        if (playerModel.getPlayer() == null) return;
        final Player player = playerModel.getPlayer();
        player.getInventory().clear();

        player.getInventory().setItem(0, ItemBuilder.getBuilder(FinalItems.CLASS_SELECTOR.getItem()).addEmptyPDCTag(GameModule.CLASS_SELECTOR_KEY).build());
        if (playerModel.getTempPlayerData().getCurrentClass() == null) return;

        final AbstractClass currentClass = playerModel.getTempPlayerData().getCurrentClass();

        player.getInventory().setItem(1, ItemBuilder.getBuilder(MegaWalls.getReflectionLayer().getCustomSkull(currentClass.getSkins().getUrl())).setDisplayName(currentClass.getClassOptions().getClassType().getColor() + currentClass.getName() + " Selector").addEmptyPDCTag(GameModule.SKIN_SELECTOR_KEY).build());
    }

    public void spawnWithers() {
        TeamModule.getGameTeams().values().forEach(GameTeam::spawnWither);
    }
}

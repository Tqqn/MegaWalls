package dev.tqqn.kireiwalls.modules.game;

import com.fastasyncworldedit.core.Fawe;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockTypes;
import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.AbstractGameState;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.framework.game.teams.wither.GameWither;
import dev.tqqn.kireiwalls.framework.region.Cuboid;
import dev.tqqn.kireiwalls.modules.classes.ClassModule;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.commands.BuildCommand;
import dev.tqqn.kireiwalls.modules.game.commands.DebugCommand;
import dev.tqqn.kireiwalls.modules.game.commands.SpectateCommand;
import dev.tqqn.kireiwalls.modules.game.commands.WitherDebugCommand;
import dev.tqqn.kireiwalls.modules.game.disabledfunctions.DisabledFunctionsListener;
import dev.tqqn.kireiwalls.modules.game.states.active.ActiveState;
import dev.tqqn.kireiwalls.modules.game.states.active.board.ActiveBoard;
import dev.tqqn.kireiwalls.modules.game.states.lobby.LobbyState;
import dev.tqqn.kireiwalls.modules.game.teams.TeamModule;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import lombok.Getter;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The GameModule class manages the game state and settings.
 */
public final class GameModule extends AbstractModule {
    @Getter private static AbstractGameState currentState;
    @Getter private static final Set<PlayerModel> ingamePlayers = new HashSet<>();
    @Getter private GameSettings gameSettings;
    private final DatabaseModule databaseModule;
    private TeamModule teamModule;
    @Getter private final Set<PlayerModel> spectators;
    private EditSession[] wallsEditSession;

    public GameModule(KireiWalls plugin, DatabaseModule databaseModule) {
        super(plugin, "Game");
        currentState = new LobbyState(this);
        this.spectators = new HashSet<>();
        this.databaseModule = databaseModule;
    }

    @Override
    public void onEnable() {
        this.teamModule = (TeamModule)this.getPlugin().getModuleManager().getModule(TeamModule.class);
        this.addComponent(DisabledFunctionsListener.class, "");
        this.addComponent(DebugCommand.class, "debug");
        this.addComponent(WitherDebugCommand.class, "witherdebug");
        this.addComponent(BuildCommand.class, "build");
        this.addComponent(SpectateCommand.class, "spectate");
        this.gameSettings = new GameSettings("DragonKeep", this.databaseModule.getDefaultConfig().getLobbyLocation(), this.databaseModule.getDefaultConfig().getLobbyTimer(), this.databaseModule.getDefaultConfig().getMiddleCuboid());
        currentState.enable();
    }

    @Override
    public void onDisable() {
        currentState.disable();
    }

    /** Ends the game. */
    public void endGame() {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(this.getPlugin(), () -> TeamModule.getGameTeams().values().forEach((gameTeam) -> gameTeam.getGameWither().kill()));
        } else {
            TeamModule.getGameTeams().values().forEach((gameTeam) -> {
                gameTeam.getGameWither().kill();
            });
        }

        getIngamePlayers().forEach((playerModel) -> {
            this.databaseModule.savePlayer(playerModel);
            if (playerModel.getPlayer() != null) {
                playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
                playerModel.getPlayer().setHealth(playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                playerModel.getPlayer().kick(ChatUtil.format("<red>Game ended!"));
            }

        });
        Bukkit.getServer().shutdown();
    }

    /**
     * Sets the game state.
     *
     * @param gameState The new game state.
     */
    public void setGameState(GameStates gameState) {
        if (currentState.getGameStates() != gameState) {
            if (currentState.getGameStates() != GameStates.ACTIVE || gameState != GameStates.WAITING) {
                switch (gameState) {
                    case ACTIVE:
                        currentState.disable();
                        currentState = new ActiveState(this);
                        currentState.enable();

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            ScoreboardModule scoreboardModule = (ScoreboardModule) this.getPlugin().getModuleManager().getModule(ScoreboardModule.class);
                            scoreboardModule.setScoreboard(PlayerModule.getPlayerModel(player.getUniqueId()), new ActiveBoard(PlayerModule.getPlayerModel(player.getUniqueId())));
                        }
                    default:
                }
            }
        }
    }

    /** Shuffles players and assigns teams. */
    public void shufflePlayers() {
        for (PlayerModel playerModel : getIngamePlayers()) {
            if (playerModel.getPlayer() == null) return;

            if (playerModel.getCurrentClass() == null) {
                playerModel.setCurrentClass(ClassModule.getClasses().get(ThreadLocalRandom.current().nextInt(ClassModule.getClasses().size()))); // Random Class
                playerModel.getPlayer().sendMessage(ChatUtil.format("<green>You got <gold>" + playerModel.getCurrentClass().getName() + " <green>as random Class!"));
            }

            if (playerModel.getGameTeam() == null) {
                if (playerModel.isSpectatorMode()) {
                    playerModel.setSpectatorMode(true);
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

        for (PlayerModel playerModel : getIngamePlayers()) {
            Score score = onTab.getScore(playerModel.getPlayer());
            int maxHealth = (int) playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            score.numberFormat(NumberFormat.styled(Style.style().color(TextColor.color(ChatUtil.getHealthColor(maxHealth, (int) playerModel.getPlayer().getHealth()))).build()));
        }
    }

    /** Spawns players in their team locations. */
    private void spawnPlayers() {
        initHealthBelowName();
        for (PlayerModel playerModel : getIngamePlayers()) {
            if (!playerModel.isSpectatorMode()) {
                double health = 40.0;
                if (playerModel.getCurrentClass().isPrestigeOne()) health = 44.0;
                playerModel.getPlayer().teleport(playerModel.getGameTeam().getGameTeamSettings().getSpawnLocation());
                playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
                playerModel.getPlayer().setHealth(playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                playerModel.getCurrentClass().applyKit(playerModel);
            }
        }
    }

    /** Makes walls fall. */
    public void wallsFall() {
        List<EditSession> editSessions = new ArrayList<>();
        for (Cuboid cuboid : this.databaseModule.getDefaultConfig().getWallCuboids()) {
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

    /** Checks if the game can start. */
    public boolean canStart() {
        return true; // For debug purposes still on force true
    }

}

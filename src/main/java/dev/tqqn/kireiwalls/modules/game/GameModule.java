package dev.tqqn.kireiwalls.modules.game;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.AbstractModule;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.AbstractGameState;
import dev.tqqn.kireiwalls.framework.game.GameStates;
import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.framework.game.teams.wither.GameWither;
import dev.tqqn.kireiwalls.modules.database.DatabaseModule;
import dev.tqqn.kireiwalls.modules.game.commands.BuildCommand;
import dev.tqqn.kireiwalls.modules.game.commands.DebugCommand;
import dev.tqqn.kireiwalls.modules.game.commands.SpectateCommand;
import dev.tqqn.kireiwalls.modules.game.commands.WitherDebugCommand;
import dev.tqqn.kireiwalls.modules.game.states.active.ActiveState;
import dev.tqqn.kireiwalls.modules.game.states.active.board.ActiveBoard;
import dev.tqqn.kireiwalls.modules.game.states.lobby.LobbyState;
import dev.tqqn.kireiwalls.modules.game.teams.TeamModule;
import dev.tqqn.kireiwalls.modules.player.PlayerModule;
import dev.tqqn.kireiwalls.modules.scoreboard.ScoreboardModule;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class GameModule extends AbstractModule {
    @Getter
    private static AbstractGameState currentState;
    @Getter
    private GameSettings gameSettings;
    private final DatabaseModule databaseModule;
    private TeamModule teamModule;
    @Getter
    private final Set<PlayerModel> ingamePlayers;
    @Getter
    private final Set<PlayerModel> spectators;

    public GameModule(KireiWalls plugin, DatabaseModule databaseModule) {
        super(plugin, "Game");
        currentState = new LobbyState(this);
        this.spectators = new HashSet<>();
        this.ingamePlayers = new HashSet<>();
        this.databaseModule = databaseModule;
    }

    public void onEnable() {
        this.teamModule = (TeamModule)this.getPlugin().getModuleManager().getModule(TeamModule.class);
        this.addComponent(DebugCommand.class, "debug");
        this.addComponent(WitherDebugCommand.class, "witherdebug");
        this.addComponent(BuildCommand.class, "build");
        this.addComponent(SpectateCommand.class, "spectate");
        this.gameSettings = new GameSettings("DragonKeep", this.databaseModule.getDefaultConfig().getLobbyLocation(), this.databaseModule.getDefaultConfig().getLobbyTimer());
        currentState.enable();
    }

    public void onDisable() {
        currentState.disable();
    }

    public void endGame() {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(this.getPlugin(), () -> TeamModule.getGameTeams().values().forEach((gameTeam) -> gameTeam.getGameWither().kill()));
        } else {
            TeamModule.getGameTeams().values().forEach((gameTeam) -> {
                gameTeam.getGameWither().kill();
            });
        }

        this.getIngamePlayers().forEach((playerModel) -> {
            this.databaseModule.savePlayer(playerModel);
            if (playerModel.getPlayer() != null) {
                playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
                playerModel.getPlayer().setHealth(playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                playerModel.getPlayer().kick(ChatUtil.format("<red>Game ended!"));
            }

        });
        Bukkit.getServer().shutdown();
    }

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

    public void shufflePlayers() {
        for (PlayerModel playerModel : this.getIngamePlayers()) {
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

    private void spawnPlayers() {
        for (PlayerModel playerModel : this.getIngamePlayers()) {
            if (!playerModel.isSpectatorMode()) {
                playerModel.getPlayer().teleport(playerModel.getGameTeam().getGameTeamSettings().getSpawnLocation());
                playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0);
                playerModel.getPlayer().setHealth(playerModel.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            }
        }

    }

    public boolean areAllWithersDead() {
        int count = 0;
        for (GameTeam gameTeam : TeamModule.getGameTeams().values()) {
            if (gameTeam.getGameWither().getWitherStatus() == GameWither.WitherStatus.DEATH) {
                ++count;
            }
        }
        return count == 4;
    }

    public boolean canStart() {
        return true;
    }

}

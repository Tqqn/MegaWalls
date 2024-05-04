package dev.tqqn.kireiwalls.framework.game.teams;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.framework.database.models.PlayerModel;
import dev.tqqn.kireiwalls.framework.game.teams.wither.GameWither;
import dev.tqqn.kireiwalls.framework.region.Cuboid;
import dev.tqqn.kireiwalls.framework.region.types.TeamProtectionRegion;
import dev.tqqn.kireiwalls.framework.region.types.WitherProtectionRegion;
import dev.tqqn.kireiwalls.nms.ReflectionLayer;
import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.NamedTextColor;
import org.antlr.v4.runtime.tree.xpath.XPathLexerErrorListener;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class GameTeam {

    private final String name;
    private final String prettyName;
    private final String tagName;
    private final String prefix;
    private final String color;
    private final String legacyColor;
    private final NamedTextColor namedTextColor;
    private GameWither gameWither;
    private final GameTeamSettings gameTeamSettings;
    private final Set<PlayerModel> currentPlayers;
    private final Set<PlayerModel> alivePlayers;

    public GameTeam(String name, String prettyName, String tagName, String prefix, String color, String legacyColor, NamedTextColor adventureColor, Cuboid spawnRegion, Cuboid witherRegion, Location spawnLocation, Location witherLocation) {
        this.name = name;
        this.prettyName = prettyName;
        this.tagName = tagName;
        this.prefix = "[" + prefix + "]";
        this.color = color;
        this.legacyColor = legacyColor;
        this.namedTextColor = adventureColor;

        TeamProtectionRegion teamProtectionRegion = new TeamProtectionRegion(name, spawnRegion, this);
        WitherProtectionRegion witherProtectionRegion = new WitherProtectionRegion(name, witherRegion, this);

        this.gameTeamSettings = new GameTeamSettings(teamProtectionRegion, witherProtectionRegion, spawnLocation, witherLocation);
        this.currentPlayers = new HashSet<>();
        this.alivePlayers = new HashSet<>();
    }

    public void spawnWither() {
        this.gameWither = new GameWither(this);
    }

    public void addPlayer(PlayerModel playerModel) {
        currentPlayers.add(playerModel);
        alivePlayers.add(playerModel);
        playerModel.setGameTeam(this);
        sendNameTag(playerModel);
    }

    public void removeAlive(PlayerModel playerModel) {
        alivePlayers.remove(playerModel);
    }

    public void sendNameTag(PlayerModel playerModel) {
        KireiWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), tagName, name, legacyColor + prefix, " " + playerModel.getCurrentClass().getTag(playerModel));
    }

    public void sendSpectatorTag(PlayerModel playerModel) {
        KireiWalls.getReflectionLayer().sendNameTag(playerModel.getPlayer(), tagName, name, "§7✖ " + legacyColor + prefix, "");
    }

    public void removePlayer(PlayerModel playerModel) {
        currentPlayers.remove(playerModel);
        playerModel.setGameTeam(null);
    }
}

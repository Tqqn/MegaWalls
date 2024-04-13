package dev.tqqn.kireiwalls.nms.v1_20_R3;

import dev.tqqn.kireiwalls.KireiWalls;
import dev.tqqn.kireiwalls.nms.ReflectionLayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;

public class v1_20_R3 implements ReflectionLayer {

    private Scoreboard mainScoreboard;

    public v1_20_R3(KireiWalls plugin) {
        Bukkit.getScheduler().runTask(plugin, () -> mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard());
    }

    @Override
    public void sendPacket(Player player, Object packetObject) {
        Packet packet = (Packet) packetObject;
        ((CraftPlayer)player).getHandle().connection.send(packet);
    }

    @Override
    public void sendNameTag(Player player, String teamName, String color, String prefix, String suffix) {
        CraftScoreboard craftScoreboard = (CraftScoreboard) mainScoreboard;
        PlayerTeam playerTeam = new PlayerTeam(craftScoreboard.getHandle(), teamName);

        playerTeam.setColor(ChatFormatting.getByName(color));
        playerTeam.setPlayerPrefix(CraftChatMessage.fromStringOrNull(prefix + " "));
        playerTeam.setPlayerSuffix(CraftChatMessage.fromStringOrNull(suffix));
        ClientboundSetPlayerTeamPacket add = ClientboundSetPlayerTeamPacket.createPlayerPacket(playerTeam, player.getName(), ClientboundSetPlayerTeamPacket.Action.ADD);
        Bukkit.getOnlinePlayers().forEach(players -> sendPacket(players, add));
    }

    @Override
    public void initScoreboardTeams() {

    }

    @Override
    public void sendSideBarScoreboard(String name, Player player, String displayName, Collection<String> board) {
        CraftScoreboard craftScoreboard = (CraftScoreboard) Bukkit.getScoreboardManager().getNewScoreboard();
        net.minecraft.world.scores.Scoreboard nmsScoreboard = craftScoreboard.getHandle();
        Objective objective = new Objective(nmsScoreboard, name, ObjectiveCriteria.DUMMY, CraftChatMessage.fromStringOrNull(displayName), ObjectiveCriteria.RenderType.INTEGER, false, null);
        nmsScoreboard.setDisplayObjective(DisplaySlot.SIDEBAR, objective);

        objective.setDisplayName(CraftChatMessage.fromStringOrNull(displayName));

        sendPacket(player, new ClientboundSetObjectivePacket(objective, 0));
        sendPacket(player, new ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, objective));

        if (!board.isEmpty()) {
            int index = board.size()-1;

            for (String line : board) {
                sendPacket(player, new ClientboundSetScorePacket("line" + index, objective.getName(), index, CraftChatMessage.fromStringOrNull(line), new BlankFormat()));
                index--;
            }
        }
    }

    @Override
    public void updateSidebarScoreboardLine(String name, Player player, String line, int index) {
        sendPacket(player, new ClientboundSetScorePacket("line" + index, name, index, CraftChatMessage.fromStringOrNull(line), new BlankFormat()));
    }

    @Override
    public void removePlayerFromScoreboard(String name, Player player) {
        sendPacket(player, new ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, null));
    }
}

package dev.tqqn.kireiwalls.nms.v1_20_R3;

import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.nms.ReflectionLayer;
import dev.tqqn.kireiwalls.nms.framework.ICustomWither;
import dev.tqqn.kireiwalls.nms.v1_20_R3.objects.CustomWither;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.scores.*;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * The v1_20_R3 class implements the ReflectionLayer interface for version 1.20_R3.
 */
public final class v1_20_R3 implements ReflectionLayer {

    @Override
    public void sendPacket(Player player, Object packetObject) {
        Packet packet = (Packet) packetObject;
        ((CraftPlayer)player).getHandle().connection.send(packet);
    }

    @Override
    public void sendNameTag(Player player, String teamName, String color, String prefix, String suffix) {
        Scoreboard scoreboard = new Scoreboard();
        PlayerTeam playerTeam = scoreboard.getPlayerTeam(teamName + player.getUniqueId());
        boolean created;
        if (playerTeam == null) {
            playerTeam = new PlayerTeam(scoreboard, teamName + player.getUniqueId());
            playerTeam.setColor(ChatFormatting.getByName(color)); //Name Color
            playerTeam.setPlayerPrefix(CraftChatMessage.fromStringOrNull(prefix + " "));
            playerTeam.setPlayerSuffix(CraftChatMessage.fromStringOrNull(suffix));
            playerTeam.setCollisionRule(Team.CollisionRule.NEVER);
            scoreboard.addPlayerTeam(teamName);
            created = true;
        } else {
            playerTeam.setColor(ChatFormatting.getByName(color)); //Name Color
            playerTeam.setPlayerPrefix(CraftChatMessage.fromStringOrNull(prefix + " "));
            playerTeam.setPlayerSuffix(CraftChatMessage.fromStringOrNull(suffix));
            created = false;
        }


        ClientboundSetPlayerTeamPacket add = ClientboundSetPlayerTeamPacket.createPlayerPacket(playerTeam, player.getName(), ClientboundSetPlayerTeamPacket.Action.ADD);
        ClientboundSetPlayerTeamPacket createTeam = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, true);
        ClientboundSetPlayerTeamPacket modifyTeam = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, false);

        Bukkit.getOnlinePlayers().forEach(players -> {
            if (created) {
                sendPacket(players, createTeam);
            } else {
                sendPacket(players, modifyTeam);
            }
            sendPacket(players, add);
        });
    }


    @Override
    public void sendSideBarScoreboard(String name, Player player, String displayName, Collection<String> board) {
        net.minecraft.world.scores.Scoreboard nmsScoreboard = new Scoreboard();
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

    @Override
    public ICustomWither createCustomWither(GameTeam gameTeam) {
        return new CustomWither(gameTeam);
    }

    @Override
    public void sendEnergy(Player player, int experience, float progress) {
        sendPacket(player, new ClientboundSetExperiencePacket(progress, 0, experience));
    }
}

package dev.tqqn.kireiwalls.nms.v1_20_R3;

import dev.tqqn.kireiwalls.framework.game.teams.GameTeam;
import dev.tqqn.kireiwalls.nms.ReflectionLayer;
import dev.tqqn.kireiwalls.nms.framework.ICustomWither;
import dev.tqqn.kireiwalls.nms.v1_20_R3.objects.CustomWither;
import dev.tqqn.kireiwalls.utils.ChatUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.chat.numbers.FixedFormat;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.world.scores.*;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.Collection;

public class v1_20_R3 implements ReflectionLayer {

    @Override
    public void sendPacket(Player player, Object packetObject) {
        Packet packet = (Packet) packetObject;
        ((CraftPlayer)player).getHandle().connection.send(packet);
    }

    @Override
    public void sendNameTag(Player player, String teamName, String color, String prefix, String suffix) {
        net.minecraft.world.scores.Scoreboard scoreboard = ((CraftScoreboard) player.getScoreboard()).getHandle();

        PlayerTeam playerTeam = scoreboard.getPlayerTeam(teamName);
        boolean created;
        if (playerTeam == null) {
            playerTeam = new PlayerTeam(scoreboard, teamName);
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
    public void updateHealth(Player player) {
        net.minecraft.world.scores.Scoreboard nmsScoreboard = ((CraftScoreboard) player.getScoreboard()).getHandle();

        Objective underName = new Objective(nmsScoreboard, "undername_health", ObjectiveCriteria.HEALTH, CraftChatMessage.fromStringOrNull(" "), ObjectiveCriteria.RenderType.INTEGER, false, new BlankFormat());
        nmsScoreboard.setDisplayObjective(DisplaySlot.BELOW_NAME, underName);
        underName.setRenderType(ObjectiveCriteria.RenderType.INTEGER);

        int playerMaxHealth = (int) Math.round(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        int playerHealth = (int) Math.round(player.getHealth());
        String healthColor = ChatUtil.getHealthColor(playerMaxHealth, playerHealth);

        Objective onTab = new Objective(nmsScoreboard, "ontab_health", ObjectiveCriteria.HEALTH, CraftChatMessage.fromStringOrNull(" "), ObjectiveCriteria.RenderType.INTEGER, true, new FixedFormat(CraftChatMessage.fromStringOrNull(healthColor + playerHealth)));
        nmsScoreboard.setDisplayObjective(DisplaySlot.LIST, onTab);
        onTab.setRenderType(ObjectiveCriteria.RenderType.INTEGER);

        sendPacket(player, new ClientboundSetObjectivePacket(underName, 1));
        sendPacket(player, new ClientboundSetObjectivePacket(underName, 0));
        sendPacket(player, new ClientboundSetDisplayObjectivePacket(DisplaySlot.BELOW_NAME, underName));

        sendPacket(player, new ClientboundSetObjectivePacket(onTab, 1));
        sendPacket(player, new ClientboundSetObjectivePacket(onTab, 0));
        sendPacket(player, new ClientboundSetDisplayObjectivePacket(DisplaySlot.LIST, onTab));
    }

    @Override
    public void sendSideBarScoreboard(String name, Player player, String displayName, Collection<String> board) {
        net.minecraft.world.scores.Scoreboard nmsScoreboard = new net.minecraft.world.scores.Scoreboard();
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
}

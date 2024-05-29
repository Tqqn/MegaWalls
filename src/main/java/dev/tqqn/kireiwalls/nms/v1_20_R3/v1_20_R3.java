package dev.tqqn.kireiwalls.nms.v1_20_R3;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import dev.tqqn.kireiwalls.modules.classes.framework.Skins;
import dev.tqqn.kireiwalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.kireiwalls.modules.teams.framework.GameTeam;
import dev.tqqn.kireiwalls.nms.ReflectionLayer;
import dev.tqqn.kireiwalls.nms.framework.ICustomWither;
import dev.tqqn.kireiwalls.nms.v1_20_R3.objects.CustomWither;
import io.netty.channel.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.*;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * The v1_20_R3 class implements the ReflectionLayer interface for version 1.20_R3.
 */
public final class v1_20_R3 implements ReflectionLayer {

    @Override
    public void sendPacket(Player player, Object packetObject) {
        if (player == null) return;

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

    @Override
    public void sendActionBar(PlayerModel playerModel) {
        Component message = CraftChatMessage.fromStringOrNull(" ");
        if (!playerModel.isSpectatorMode()) {
            message = CraftChatMessage.fromStringOrNull(playerModel.getCurrentClass().getActionBar(playerModel));
        }
        sendPacket(playerModel.getPlayer(), new ClientboundSetActionBarTextPacket(message));
    }

    @Override
    public void sendZombieParticle(PlayerModel playerModel) {
        final Player player = playerModel.getPlayer();
        if (player == null) return;

        ClientboundLevelParticlesPacket clientboundLevelParticlesPacket = new ClientboundLevelParticlesPacket(ParticleTypes.ANGRY_VILLAGER, false, player.getX(), player.getY()+1, player.getZ(), 0, 0, 0, 10, 1);
        for (Player players : Bukkit.getOnlinePlayers()) {
            sendPacket(players, clientboundLevelParticlesPacket);
        }
    }

    @Override
    public void changeSkin(Skins skins, PlayerModel playerModel) {
        if (playerModel.getPlayer() == null) return;
        final Player player = playerModel.getPlayer();
        final int currentSlot = player.getInventory().getHeldItemSlot();

        ServerPlayer serverPlayer = ((CraftPlayer)player).getHandle();
        setSkin(serverPlayer, skins.getTexture(), skins.getSignature());

        final Location oldLoc = player.getLocation().clone();

        GameType gameType = GameType.SURVIVAL;
        switch (player.getGameMode()) {
            case CREATIVE -> gameType = GameType.CREATIVE;
            case SPECTATOR -> gameType = GameType.SPECTATOR;
        }

        CommonPlayerSpawnInfo playerInfo = new CommonPlayerSpawnInfo(serverPlayer.level().dimensionTypeId(), serverPlayer.level().dimension(), serverPlayer.server.overworld().getSeed(), gameType, gameType, false, false, Optional.of(GlobalPos.of(serverPlayer.level().dimension(), serverPlayer.getOnPos())), 0);

        ClientboundPlayerInfoRemovePacket clientboundPlayerInfoRemovePacket = new ClientboundPlayerInfoRemovePacket(List.of(player.getUniqueId()));

        ClientboundRemoveEntitiesPacket clientboundRemoveEntitiesPacket = new ClientboundRemoveEntitiesPacket(player.getEntityId());

        ClientboundAddEntityPacket clientboundAddEntityPacket = new ClientboundAddEntityPacket(serverPlayer);

        ClientboundPlayerInfoUpdatePacket clientboundPlayerInfoUpdatePacket = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(Collections.singletonList(serverPlayer));

        ClientboundRespawnPacket clientboundRespawnPacket = new ClientboundRespawnPacket(playerInfo, (byte) 0);

        ClientboundGameEventPacket eventPacket = new ClientboundGameEventPacket(ClientboundGameEventPacket.LEVEL_CHUNKS_LOAD_START, 0);

        for (Player players : Bukkit.getOnlinePlayers()) {
            sendPacket(players, clientboundPlayerInfoRemovePacket);

            if (!players.equals(player)) sendPacket(players, clientboundRemoveEntitiesPacket);

            sendPacket(players, clientboundPlayerInfoUpdatePacket);
            if (!players.equals(player)) sendPacket(players, clientboundAddEntityPacket);
        }

        sendPacket(player, clientboundRespawnPacket);
        sendPacket(player, eventPacket);
        player.teleport(oldLoc);
        player.updateInventory();
        player.getInventory().setHeldItemSlot(currentSlot);
    }

    @Override
    public void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                super.write(channelHandlerContext, packet, channelPromise);
            }
        };

        Connection connection = (Connection) getConnection(player);
        ChannelPipeline pipeline = connection.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }

    @Override
    public void unInjectPlayer(Player player) {
        Connection connection = (Connection) getConnection(player);
        Channel channel = connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    @Override
    public Object getConnection(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        Connection connection = null;
        try {
            Field connectionField = ServerCommonPacketListenerImpl.class.getDeclaredField("c");
            connectionField.setAccessible(true);
            connection = (Connection) connectionField.get(craftPlayer.getHandle().connection);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return connection;
    }

    @Override
    public ItemStack getCustomSkull(String texture) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures playerTextures = profile.getTextures();
        URL url = null;

        try {
            url = new URL("https://textures.minecraft.net/texture/" + texture);
        } catch (MalformedURLException ignored) {

        }
        playerTextures.setSkin(url);
        profile.setTextures(playerTextures);

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwnerProfile(profile);
        itemStack.setItemMeta(skullMeta);

        return itemStack;
    }

    private void setSkin(ServerPlayer serverPlayer, String texture, String signature) {
        Property skinData = new Property("textures", texture, signature);
        GameProfile gameProfile = serverPlayer.getGameProfile();
        PropertyMap propertyMap = gameProfile.getProperties();
        propertyMap.removeAll("textures");
        propertyMap.put("textures", skinData);
    }
 }

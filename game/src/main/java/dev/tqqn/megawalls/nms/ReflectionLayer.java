package dev.tqqn.megawalls.nms;

import dev.tqqn.megawalls.common.classes.ClassSkins;
import dev.tqqn.megawalls.modules.database.framework.models.PlayerModel;
import dev.tqqn.megawalls.modules.teams.framework.GameTeam;
import dev.tqqn.megawalls.nms.framework.ICustomWither;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * The ReflectionLayer interface defines methods for interacting with Minecraft
 * server reflection to manipulate game entities and events at a low level.
 */
public interface ReflectionLayer {

    default void sendPacketToAll(Object[] packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Object packets : packet) {
                sendPacket(player, packets);
            }
        }
    }

    /**
     * Sends a packet to the specified player using server reflection.
     *
     * @param player The player to whom the packet is being sent.
     * @param packet The packet object to be sent to the player.
     */
    void sendPacket(Player player, Object packet);

    /**
     * Sends a custom name tag to the specified player.
     *
     * @param player   The player to whom the custom name tag is being sent.
     * @param teamName The name of the custom team.
     * @param color    The color of the name tag.
     * @param prefix   The prefix to be displayed before the player's name.
     * @param suffix   The suffix to be displayed after the player's name.
     */
    void sendNameTag(Player player, String teamName, String color, String prefix, String suffix);

    /**
     * Sends a sidebar scoreboard to the specified player.
     *
     * @param name        The name of the scoreboard.
     * @param player      The player to whom the scoreboard is being sent.
     * @param displayName The display name of the scoreboard.
     * @param board       The collection of lines to be displayed on the scoreboard.
     */
    void sendSideBarScoreboard(String name, Player player, String displayName, Collection<String> board);

    /**
     * Updates a line of the sidebar scoreboard for the specified player.
     *
     * @param name  The name of the scoreboard.
     * @param player The player whose scoreboard line is being updated.
     * @param line  The new line content.
     * @param index The index of the line to be updated.
     */
    void updateSidebarScoreboardLine(String name, Player player, String line, int index);

    /**
     * Removes a player from the sidebar scoreboard.
     *
     * @param name   The name of the scoreboard.
     * @param player The player to be removed from the scoreboard.
     */
    void removePlayerFromScoreboard(String name, Player player);

    /**
     * Creates a custom wither entity associated with the specified game team.
     *
     * @param gameTeam The game team for which the custom wither is being created.
     * @return An instance of the custom wither.
     */
    ICustomWither createCustomWither(GameTeam gameTeam);

    /**
     * Sends a EXP change to the player.
     * @param player The player -> target
     * @param experience the experience level that should be showed.
     * @param progress the experience progress bar (0.0-1.0)
     */
    void sendEnergy(Player player, int experience, float progress);

    void sendActionBar(PlayerModel playerModel);

    void sendZombieParticle(PlayerModel playerModel);

    void changeSkin(ClassSkins skins, PlayerModel playerModel);
    void injectPlayer(Player player);
    void unInjectPlayer(Player player);
    Object getConnection(Player player);

    ItemStack getCustomSkull(String texture);
}


package dev.tqqn.kireiwalls.nms;

import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * The ReflectionLayer interface defines methods for interacting with Minecraft's server code.
 * It provides methods for retrieving the server version string, sending packets to players.
 */
public interface ReflectionLayer {

    /**
     * Sends a packet to the specified player using server reflection.
     * This method sends a packet object to the given player using server reflection,
     * allowing for the manipulation of game entities and events at a low level.
     *
     * @param player      The player to whom the packet is being sent.
     * @param packet      The packet object to be sent to the player.
     */
    void sendPacket(Player player, Object packet);

    /**
     * Sends a custom name tag to the specified player.
     * This method creates a custom team with the specified name, color, prefix, and suffix,
     * adds the player to the team, and sends packets to all online players to display the custom name tag for the player.
     *
     * @param player   The player to whom the custom name tag is being sent.
     * @param teamName The name of the custom team.
     * @param color    The color of the name tag.
     * @param prefix   The prefix to be displayed before the player's name.
     * @param suffix   The suffix to be displayed after the player's name.
     */
    void sendNameTag(Player player, String teamName, String color, String prefix, String suffix);

    /**
     * Initializes the scoreboard teams for the teams.
     * If the teams do not exist, this method creates them and sets collision rules to NEVER
     * to prevent player collision within the teams.
     */
    void initScoreboardTeams();

    void sendSideBarScoreboard(String name, Player player, String displayName, Collection<String> board);

    void updateSidebarScoreboardLine(String name, Player player, String line, int index);

    void removePlayerFromScoreboard(String name, Player player);
}


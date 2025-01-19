package dev.tqqn.megawalls.modules.enderchest.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import dev.tqqn.megawalls.modules.enderchest.EnderChestModule;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@CommandAlias("ec|enderchest")
@CommandPermission("mw.enderchest")
@RequiredArgsConstructor
public final class EnderChestCommand extends BaseCommand {

    private final EnderChestModule enderChestModule;

    @Default
    public void open(Player player) {
        enderChestModule.openEnderChest(player);
    }
}

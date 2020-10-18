package com.hpfxd.polar.command.commands;

import com.hpfxd.polar.command.Command;
import com.hpfxd.polar.player.Player;
import com.hpfxd.polar.util.TextComponents;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class PingCommand extends Command {
    @Getter private final String[] aliases = { "ping", "latency" };
    @Getter private final String usage = "/ping [player]";

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(TextComponents.str(new ComponentBuilder("Your ping is currently ").color(ChatColor.GRAY)
                    .append(player.getPing() + "").color(ChatColor.GREEN)
                    .append("ms.").color(ChatColor.GRAY)
                    .create()));
        } else if (args.length == 1) {
            String name = args[0];

            Player target = player.getWorld().getPlayerByName(name);

            if (target == null) {
                throw new IllegalArgumentException("Player with the name \"" + name + "\" not found.");
            }

            player.sendMessage(TextComponents.str(new ComponentBuilder(target.getName()).color(ChatColor.GREEN)
                    .append("'s ping is currently ").color(ChatColor.GRAY)
                    .append(player.getPing() + "").color(ChatColor.GREEN)
                    .append("ms.").color(ChatColor.GRAY)
                    .create()));
        } else {
            throw new IllegalArgumentException();
        }
    }
}

package com.hpfxd.polar.command;

import com.hpfxd.polar.Polar;
import com.hpfxd.polar.command.commands.GameModeCommand;
import com.hpfxd.polar.command.commands.PingCommand;
import com.hpfxd.polar.event.player.PlayerChatEvent;
import com.hpfxd.polar.player.Player;
import com.hpfxd.polar.util.TextComponents;
import lombok.extern.slf4j.Slf4j;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CommandManager {
    private final List<Command> commands = new ArrayList<>();

    public void registerEvents() {
        Polar.getPolar().getEventManager().registerEventHandler(PlayerChatEvent.class, event -> {
            String msg = event.getMessage();

            if (msg.startsWith("/")) {
                event.setCancelled(true);
                Player player = event.getPlayer();

                try {
                    String[] s = msg.split(" ");
                    String alias = s[0].length() > 1 ? s[0].substring(1) : "";

                    Command command = this.getCommand(alias);

                    if (command == null) {
                        player.sendMessage(TextComponents.COMMAND_UNKNOWN);
                        return;
                    }

                    if (!command.hasPermission(player)) {
                        player.sendMessage(TextComponents.COMMAND_NO_PERMISSION);
                        return;
                    }

                    String[] args = {};
                    if (s.length > 1) {
                        // copy s to new array (except the first element, the command name)
                        args = Arrays.copyOfRange(s, 1, s.length);
                    }

                    try {
                        command.execute(player, args);
                    } catch (CommandException e) {
                        player.sendMessage(TextComponents.str(new ComponentBuilder("An error occurred whilst executing that command.\n").color(ChatColor.RED)
                                .append(e.getMessage()).color(ChatColor.GRAY)
                                .create()));
                    } catch (IllegalArgumentException e) {
                        if (e.getMessage() == null) {
                            // send message with command usage
                            player.sendMessage(TextComponents.str(new ComponentBuilder("Invalid usage.\n").color(ChatColor.RED)
                                    .append(command.getUsage())
                                    .create()));
                        } else {
                            // send message with exception reason and usage
                            player.sendMessage(TextComponents.str(new ComponentBuilder("Invalid usage.\n").color(ChatColor.RED)
                                    .append(e.getMessage() + "\n")
                                    .append(command.getUsage())
                                    .create()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    player.sendMessage(TextComponents.UNEXPECTED_ERROR);
                }
            }
        });
        log.info("Registered command listener.");
    }

    public void registerCommand(Command command) {
        this.commands.add(command);
    }

    public void registerDefaultCommands() {
        this.registerCommand(new PingCommand());
        this.registerCommand(new GameModeCommand());
        log.info("Registered default commands.");
    }

    public Command getCommand(String alias) {
        for (Command command : this.commands) {
            for (String a : command.getAliases()) {
                if (alias.equalsIgnoreCase(a)) {
                    return command;
                }
            }
        }

        return null;
    }
}

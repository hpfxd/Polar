package com.hpfxd.polar.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;

public class TextComponents {
    public static final String UNSUPPORTED_VERSION = str(new ComponentBuilder("You are using an unsupported Minecraft version.")
            .color(ChatColor.RED)
            .create());

    public static final String COMMAND_UNKNOWN = str(new ComponentBuilder("Unknown command.")
            .color(ChatColor.RED)
            .create());

    public static final String COMMAND_NO_PERMISSION = str(new ComponentBuilder("You do not have permission to execute that command.")
            .color(ChatColor.RED)
            .create());

    public static final String UNEXPECTED_ERROR = str(new ComponentBuilder("An unexpected error has occurred.")
            .color(ChatColor.RED)
            .create());

    public static String str(BaseComponent... components) {
        return ComponentSerializer.toString(components);
    }
}

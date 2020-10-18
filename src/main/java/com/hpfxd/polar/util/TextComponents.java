package com.hpfxd.polar.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;

public class TextComponents {
    public static final String UNSUPPORTED_VERSION = str(new ComponentBuilder("You are using an unsupported Minecraft version.")
            .color(ChatColor.RED)
            .create());

    public static String str(BaseComponent... components) {
        return ComponentSerializer.toString(components);
    }
}

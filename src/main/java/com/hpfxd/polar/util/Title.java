package com.hpfxd.polar.util;

import com.hpfxd.polar.network.packet.packets.play.PacketOutTitle;
import lombok.Data;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.ArrayList;
import java.util.List;

@Data
public class Title {
    private final BaseComponent[] title;
    private final BaseComponent[] subtitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public List<PacketOutTitle> getPackets() {
        List<PacketOutTitle> list = new ArrayList<>(2);

        list.add(new PacketOutTitle(PacketOutTitle.Action.SET_TIMES_AND_DISPLAY, this));
        list.add(new PacketOutTitle(PacketOutTitle.Action.SET_TITLE, this));
        if (this.subtitle != null) {
            list.add(new PacketOutTitle(PacketOutTitle.Action.SET_SUBTITLE, this));
        }

        return list;
    }
}

package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.util.Title;
import io.netty.buffer.ByteBuf;

public class PacketOutTitle extends Packet {
    private final Action action;
    private final Title title;

    public PacketOutTitle(Action action, Title title) {
        this.action = action;
        this.title = title;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.action.ordinal());
        if (this.action == Action.SET_TITLE) {
            PacketUtils.writeChat(buf, this.title.getTitle());
        } else if (this.action == Action.SET_SUBTITLE) {
            PacketUtils.writeChat(buf, this.title.getTitle());
        } else if (this.action == Action.SET_TIMES_AND_DISPLAY) {
            buf.writeInt(this.title.getFadeIn());
            buf.writeInt(this.title.getStay());
            buf.writeInt(this.title.getFadeOut());
        }
    }

    public enum Action {
        SET_TITLE,
        SET_SUBTITLE,
        SET_TIMES_AND_DISPLAY,
        HIDE,
        RESET
    }
}

package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketInChatMessage extends Packet {
    private String message;

    @Override
    public void read(ByteBuf buf) {
        this.message = PacketUtils.readString(buf);
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        channelHandler.getPlayer().chat(this.message);
    }
}

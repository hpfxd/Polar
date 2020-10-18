package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketInKeepAlive extends Packet {
    private int keepAliveId;

    @Override
    public void read(ByteBuf buf) {
        this.keepAliveId = PacketUtils.readVarInt(buf);
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        channelHandler.setLastKeepAliveReceived(System.nanoTime());
    }
}

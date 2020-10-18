package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutKeepAlive extends Packet {
    private final int keepAliveId;

    public PacketOutKeepAlive(int keepAliveId) {
        this.keepAliveId = keepAliveId;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.keepAliveId);
    }
}

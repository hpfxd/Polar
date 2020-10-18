package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutDisconnect extends Packet {
    private final String reason;

    public PacketOutDisconnect(String reason) {
        this.reason = reason;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeString(buf, this.reason);
    }
}

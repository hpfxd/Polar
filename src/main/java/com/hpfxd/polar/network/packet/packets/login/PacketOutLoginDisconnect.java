package com.hpfxd.polar.network.packet.packets.login;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutLoginDisconnect extends Packet {
    private final String reason;

    public PacketOutLoginDisconnect(String reason) {
        this.reason = reason;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeString(buf, this.reason);
    }
}

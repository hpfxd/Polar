package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutEntityHeadLook extends Packet {
    private final int entityId;
    private final byte yawAngle;

    public PacketOutEntityHeadLook(int entityId, byte yawAngle) {
        this.entityId = entityId;
        this.yawAngle = yawAngle;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.entityId);
        buf.writeByte(this.yawAngle);
    }
}

package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutEntityLook extends Packet {
    private final int entityId;
    private final byte yawAngle;
    private final byte pitchAngle;
    private final boolean onGround;

    public PacketOutEntityLook(int entityId, byte yawAngle, byte pitchAngle, boolean onGround) {
        this.entityId = entityId;
        this.yawAngle = yawAngle;
        this.pitchAngle = pitchAngle;
        this.onGround = onGround;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.entityId);
        buf.writeByte(this.yawAngle);
        buf.writeByte(this.pitchAngle);
        buf.writeBoolean(this.onGround);
    }
}

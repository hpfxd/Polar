package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutEntityLookAndRelativeMove extends Packet {
    private final int entityId;
    private final byte deltaX;
    private final byte deltaY;
    private final byte deltaZ;
    private final byte yawAngle;
    private final byte pitchAngle;
    private final boolean onGround;

    public PacketOutEntityLookAndRelativeMove(int entityId, byte deltaX, byte deltaY, byte deltaZ, byte yawAngle, byte pitchAngle, boolean onGround) {
        this.entityId = entityId;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.yawAngle = yawAngle;
        this.pitchAngle = pitchAngle;
        this.onGround = onGround;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.entityId);
        buf.writeByte(this.deltaX);
        buf.writeByte(this.deltaY);
        buf.writeByte(this.deltaZ);
        buf.writeByte(this.yawAngle);
        buf.writeByte(this.pitchAngle);
        buf.writeBoolean(this.onGround);
    }
}

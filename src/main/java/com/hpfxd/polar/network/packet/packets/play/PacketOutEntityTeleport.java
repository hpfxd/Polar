package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutEntityTeleport extends Packet {
    private final int entityId;
    private final int x;
    private final int y;
    private final int z;
    private final byte yawAngle;
    private final byte pitchAngle;
    private final boolean onGround;

    public PacketOutEntityTeleport(int entityId, int x, int y, int z, byte yawAngle, byte pitchAngle, boolean onGround) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yawAngle = yawAngle;
        this.pitchAngle = pitchAngle;
        this.onGround = onGround;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.entityId);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeByte(this.yawAngle);
        buf.writeByte(this.pitchAngle);
        buf.writeBoolean(this.onGround);
    }
}

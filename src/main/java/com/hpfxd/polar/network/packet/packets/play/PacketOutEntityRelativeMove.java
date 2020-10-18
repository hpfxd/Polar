package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutEntityRelativeMove extends Packet {
    private final int entityId;
    private final byte deltaX;
    private final byte deltaY;
    private final byte deltaZ;
    private final boolean onGround;

    public PacketOutEntityRelativeMove(int entityId, byte deltaX, byte deltaY, byte deltaZ, boolean onGround) {
        this.entityId = entityId;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.onGround = onGround;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.entityId);
        buf.writeByte(this.deltaX);
        buf.writeByte(this.deltaY);
        buf.writeByte(this.deltaZ);
        buf.writeBoolean(this.onGround);
    }
}

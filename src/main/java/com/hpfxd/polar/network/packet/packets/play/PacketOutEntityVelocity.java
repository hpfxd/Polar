package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.util.Vector;
import io.netty.buffer.ByteBuf;

public class PacketOutEntityVelocity extends Packet {
    private final int entityId;
    private final Vector vector;

    public PacketOutEntityVelocity(int entityId, Vector vector) {
        this.entityId = entityId;
        this.vector = vector;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.entityId);
        buf.writeShort((int) (this.vector.getX() * 8000));
        buf.writeShort((int) (this.vector.getY() * 8000));
        buf.writeShort((int) (this.vector.getZ() * 8000));
    }
}

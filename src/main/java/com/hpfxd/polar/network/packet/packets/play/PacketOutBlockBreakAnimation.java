package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.util.Position;
import io.netty.buffer.ByteBuf;

public class PacketOutBlockBreakAnimation extends Packet {
    private final int entityId;
    private final Position position;
    private final int stage;

    public PacketOutBlockBreakAnimation(int entityId, Position position, int stage) {
        this.entityId = entityId;
        this.position = position;
        this.stage = stage;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.entityId);
        PacketUtils.writePosition(buf, this.position);
        buf.writeByte(this.stage);
    }
}

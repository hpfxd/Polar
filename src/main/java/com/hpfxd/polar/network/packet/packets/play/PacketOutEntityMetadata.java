package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.util.metadata.EntityMetadata;
import io.netty.buffer.ByteBuf;

public class PacketOutEntityMetadata extends Packet {
    private final int entityId;
    private final EntityMetadata metadata;

    public PacketOutEntityMetadata(int entityId, EntityMetadata metadata) {
        this.entityId = entityId;
        this.metadata = metadata;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.entityId);
        this.metadata.write(buf);
    }
}

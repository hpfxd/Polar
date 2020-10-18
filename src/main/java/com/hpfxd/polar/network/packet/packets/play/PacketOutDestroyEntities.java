package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutDestroyEntities extends Packet {
    private final int[] entityIds;

    public PacketOutDestroyEntities(int[] entityIds) {
        this.entityIds = entityIds;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.entityIds.length);
        for (int entityId : this.entityIds) {
            PacketUtils.writeVarInt(buf, entityId);
        }
    }
}

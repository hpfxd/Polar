package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.util.ChunkDataMessage;
import io.netty.buffer.ByteBuf;

public class PacketOutChunkData extends Packet {
    private final ChunkDataMessage message;

    public PacketOutChunkData(ChunkDataMessage message) {
        this.message = message;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(this.message.getX());
        buf.writeInt(this.message.getZ());
        buf.writeBoolean(this.message.isContinuous());
        buf.writeShort(this.message.getPrimaryMask());
        PacketUtils.writeByteArray(buf, this.message.getData());
    }
}

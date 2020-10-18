package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.util.ChunkDataMessage;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class PacketOutChunkBulk extends Packet {
    private final List<ChunkDataMessage> entries;
    private final boolean skylight;

    public PacketOutChunkBulk(List<ChunkDataMessage> entries, boolean skylight) {
        this.entries = entries;
        this.skylight = skylight;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeBoolean(this.skylight);
        PacketUtils.writeVarInt(buf, this.entries.size());

        for (ChunkDataMessage entry : entries) {
            buf.writeInt(entry.getX());
            buf.writeInt(entry.getZ());
            buf.writeShort(entry.getPrimaryMask());
        }

        for (ChunkDataMessage entry : entries) {
            buf.writeBytes(entry.getData());
        }
    }
}

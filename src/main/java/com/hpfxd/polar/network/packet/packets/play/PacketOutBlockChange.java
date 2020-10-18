package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.util.Position;
import io.netty.buffer.ByteBuf;

public class PacketOutBlockChange extends Packet {
    private final Position position;
    private final int blockId;

    public PacketOutBlockChange(Position position, int blockId) {
        this.position = position;
        this.blockId = blockId;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writePosition(buf, this.position);
        PacketUtils.writeVarInt(buf, this.blockId);
    }
}

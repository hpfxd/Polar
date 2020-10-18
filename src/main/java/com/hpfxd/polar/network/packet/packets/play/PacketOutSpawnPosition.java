package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.util.Position;
import io.netty.buffer.ByteBuf;

public class PacketOutSpawnPosition extends Packet {
    private final Position position;

    public PacketOutSpawnPosition(Position position) {
        this.position = position;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writePosition(buf, this.position);
    }
}

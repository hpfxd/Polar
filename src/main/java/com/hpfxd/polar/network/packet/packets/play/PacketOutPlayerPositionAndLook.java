package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.util.MutableLocation;
import io.netty.buffer.ByteBuf;

public class PacketOutPlayerPositionAndLook extends Packet {
    private final MutableLocation location;

    public PacketOutPlayerPositionAndLook(MutableLocation location) {
        this.location = location;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeDouble(this.location.getX());
        buf.writeDouble(this.location.getY());
        buf.writeDouble(this.location.getZ());
        buf.writeFloat(this.location.getYaw());
        buf.writeFloat(this.location.getPitch());
        buf.writeByte(0);
    }
}

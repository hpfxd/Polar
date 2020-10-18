package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutHeldItemChange extends Packet {
    private final int slot;

    public PacketOutHeldItemChange(int slot) {
        this.slot = slot;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(this.slot);
    }
}

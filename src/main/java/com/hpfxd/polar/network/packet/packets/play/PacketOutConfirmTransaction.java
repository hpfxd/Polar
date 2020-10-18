package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutConfirmTransaction extends Packet {
    private final int windowId;
    private final int actionNumber;
    private final boolean accepted;

    public PacketOutConfirmTransaction(int windowId, int actionNumber, boolean accepted) {
        this.windowId = windowId;
        this.actionNumber = actionNumber;
        this.accepted = accepted;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(this.windowId);
        buf.writeShort(this.actionNumber);
        buf.writeBoolean(this.accepted);
    }
}

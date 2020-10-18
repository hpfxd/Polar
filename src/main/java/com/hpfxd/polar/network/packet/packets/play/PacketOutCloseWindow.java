package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.Inventory;
import io.netty.buffer.ByteBuf;

public class PacketOutCloseWindow extends Packet {
    private final Inventory inventory;

    public PacketOutCloseWindow(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(this.inventory.getWindowId());
    }
}

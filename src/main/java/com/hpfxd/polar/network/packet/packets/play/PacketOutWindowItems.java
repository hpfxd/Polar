package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.Inventory;
import com.hpfxd.polar.util.ItemStack;
import io.netty.buffer.ByteBuf;

public class PacketOutWindowItems extends Packet {
    private final Inventory inventory;

    public PacketOutWindowItems(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(this.inventory.getWindowId());
        buf.writeShort(this.inventory.getItems().length);
        for (ItemStack item : this.inventory.getItems()) {
            item.write(buf);
        }
    }
}

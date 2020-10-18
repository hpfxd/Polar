package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.Inventory;
import com.hpfxd.polar.util.ItemStack;
import io.netty.buffer.ByteBuf;

public class PacketOutSetSlot extends Packet {
    private final Inventory inventory;
    private final int slot;
    private final ItemStack stack;

    public PacketOutSetSlot(Inventory inventory, int slot, ItemStack stack) {
        this.inventory = inventory;
        this.slot = slot;
        this.stack = stack;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(this.inventory.getWindowId());
        buf.writeShort(this.slot);
        this.stack.write(buf);
    }
}

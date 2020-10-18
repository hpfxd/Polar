package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.PlayerInventory;
import com.hpfxd.polar.util.ItemStack;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PacketOutEntityEquipment extends Packet {
    private final int entityId;
    private final EntityEquipmentSlot slot;
    private final ItemStack stack;

    public PacketOutEntityEquipment(int entityId, EntityEquipmentSlot slot, ItemStack stack) {
        this.entityId = entityId;
        this.slot = slot;
        this.stack = stack;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.entityId);
        buf.writeShort(this.slot.ordinal());
        this.stack.write(buf);
    }

    @AllArgsConstructor
    @Getter
    public enum EntityEquipmentSlot {
        HELD(-1),
        BOOTS(PlayerInventory.ARMOR_START_OFFSET + 3),
        LEGGINGS(PlayerInventory.ARMOR_START_OFFSET + 2),
        CHEST_PLATE(PlayerInventory.ARMOR_START_OFFSET + 1),
        HELMET(PlayerInventory.ARMOR_START_OFFSET)
        ;

        private final int inventorySlot;
    }
}

package com.hpfxd.polar.player;

import com.hpfxd.polar.network.packet.packets.play.PacketOutEntityEquipment;
import com.hpfxd.polar.network.packet.packets.play.PacketOutHeldItemChange;
import com.hpfxd.polar.network.packet.packets.play.PacketOutSetSlot;
import com.hpfxd.polar.util.ItemStack;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PlayerInventory extends Inventory {
    public static final int ARMOR_START_OFFSET = 5;
    public static final int INVENTORY_START_OFFSET = 9;
    public static final int HOTBAR_START_OFFSET = 36;

    private final Player player;
    private int hotbarPosition; // 0-8
    @Setter private boolean canUpdate; // whether to send packets to players to update inventory

    public PlayerInventory(Player player) {
        super(0, null, null, 45);
        this.player = player;
    }

    public void setHotbarPositionAndSendToClient(int position) {
        this.hotbarPosition = position;
        this.player.getChannelHandler().sendPacket(new PacketOutHeldItemChange(this.hotbarPosition));
    }

    public ItemStack getItemInHand() {
        return this.items[HOTBAR_START_OFFSET + this.hotbarPosition];
    }

    public void setItemInHand(ItemStack stack) {
        this.setItem(HOTBAR_START_OFFSET + this.hotbarPosition, stack);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);

        this.player.getChannelHandler().sendPacket(new PacketOutSetSlot(this, slot, stack));

        if (slot == HOTBAR_START_OFFSET + this.hotbarPosition) this.updateEquipment(PacketOutEntityEquipment.EntityEquipmentSlot.HELD);
        else if (slot == ARMOR_START_OFFSET) this.updateEquipment(PacketOutEntityEquipment.EntityEquipmentSlot.HELMET);
        else if (slot == ARMOR_START_OFFSET + 1) this.updateEquipment(PacketOutEntityEquipment.EntityEquipmentSlot.CHEST_PLATE);
        else if (slot == ARMOR_START_OFFSET + 2) this.updateEquipment(PacketOutEntityEquipment.EntityEquipmentSlot.LEGGINGS);
        else if (slot == ARMOR_START_OFFSET + 3) this.updateEquipment(PacketOutEntityEquipment.EntityEquipmentSlot.BOOTS);
    }

    public void updateEquipment(PacketOutEntityEquipment.EntityEquipmentSlot equipmentSlot) {
        int slot = equipmentSlot.getInventorySlot();

        if (equipmentSlot == PacketOutEntityEquipment.EntityEquipmentSlot.HELD) {
            slot = HOTBAR_START_OFFSET + this.hotbarPosition;
        }

        this.player.sendPacketToViewers(new PacketOutEntityEquipment(this.player.getEntityId(), equipmentSlot, this.items[slot]));
    }

    public void setHotbarPosition(int hotbarPosition) {
        this.hotbarPosition = hotbarPosition;

        if (this.player.isBlocking()) {
            this.player.setBlocking(false);
            this.player.updateStatusMetadata();
        }
    }
}

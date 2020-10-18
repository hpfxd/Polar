package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.Player;
import io.netty.buffer.ByteBuf;

public class PacketInHeldItemChange extends Packet {
    private int slot;

    @Override
    public void read(ByteBuf buf) {
        this.slot = buf.readShort();
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        Player player = channelHandler.getPlayer();

        player.getPlayerInventory().setHotbarPosition(this.slot);
        player.getPlayerInventory().updateEquipment(PacketOutEntityEquipment.EntityEquipmentSlot.HELD);
    }
}

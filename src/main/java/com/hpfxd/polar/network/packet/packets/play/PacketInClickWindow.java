package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.event.player.PlayerInventoryInteractEvent;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketInClickWindow extends Packet {
    private int windowId;
    private int slot;
    private int button;
    private int actionNumber;
    private int mode;

    @Override
    public void read(ByteBuf buf) {
        this.windowId = buf.readByte();
        this.slot = buf.readShort();
        this.button = buf.readByte();
        this.actionNumber = buf.readShort();
        this.mode = buf.readByte();
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        PlayerInventoryInteractEvent event = new PlayerInventoryInteractEvent(this.windowId, this.slot, this.button, this.actionNumber, this.mode);
        event.setCancelled(true);
        event.postEvent();

        channelHandler.getPlayer().getChannelHandler().sendPacket(new PacketOutConfirmTransaction(this.windowId, this.actionNumber, !event.isCancelled()));
    }
}

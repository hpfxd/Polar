package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketInPlayer extends Packet {
    private boolean onGround;

    @Override
    public void read(ByteBuf buf) {
        this.onGround = buf.readBoolean();
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        channelHandler.getPlayer().setOnGround(this.onGround);
        channelHandler.getPlayer().onMovementUpdate(false, false);
    }
}

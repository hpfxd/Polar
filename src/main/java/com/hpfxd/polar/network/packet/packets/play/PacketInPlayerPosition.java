package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketInPlayerPosition extends Packet {
    private double x;
    private double y;
    private double z;
    private boolean onGround;

    @Override
    public void read(ByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.onGround = buf.readBoolean();
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        channelHandler.getPlayer().getLocation().setXYZ(this.x, this.y, this.z);
        channelHandler.getPlayer().setOnGround(this.onGround);
        channelHandler.getPlayer().onMovementUpdate(true, false);
    }
}

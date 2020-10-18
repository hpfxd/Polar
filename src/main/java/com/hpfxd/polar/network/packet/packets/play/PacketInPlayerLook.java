package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketInPlayerLook extends Packet {
    private float yaw;
    private float pitch;
    private boolean onGround;

    @Override
    public void read(ByteBuf buf) {
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.onGround = buf.readBoolean();
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        channelHandler.getPlayer().getLocation().setYawPitch(this.yaw, this.pitch);
        channelHandler.getPlayer().setOnGround(this.onGround);
        channelHandler.getPlayer().onMovementUpdate(false, true);
    }
}

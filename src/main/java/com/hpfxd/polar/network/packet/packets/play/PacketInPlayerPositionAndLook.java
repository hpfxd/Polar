package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketInPlayerPositionAndLook extends Packet {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;

    @Override
    public void read(ByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.onGround = buf.readBoolean();
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        channelHandler.getPlayer().getLocation().setXYZ(this.x, this.y, this.z);
        channelHandler.getPlayer().getLocation().setYawPitch(this.yaw, this.pitch);
        channelHandler.getPlayer().setOnGround(this.onGround);
        channelHandler.getPlayer().onMovementUpdate(true, true);
    }
}

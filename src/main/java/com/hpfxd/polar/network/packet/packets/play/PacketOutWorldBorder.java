package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.world.WorldBorder;
import io.netty.buffer.ByteBuf;

public class PacketOutWorldBorder extends Packet {
    private final Action action;
    private final WorldBorder worldBorder;

    public PacketOutWorldBorder(Action action, WorldBorder worldBorder) {
        this.action = action;
        this.worldBorder = worldBorder;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.action.ordinal());
        if (this.action == Action.SET_SIZE) {
            buf.writeDouble(this.worldBorder.getRadius());
        } else if (this.action == Action.SET_CENTER) {
            buf.writeDouble(this.worldBorder.getCenterX());
            buf.writeDouble(this.worldBorder.getCenterZ());
        }
    }

    public enum Action {
        SET_SIZE,
        LERP_SIZE,
        SET_CENTER,
        INITIALIZE,
        SET_WARNING_TIME,
        SET_WARNING_BLOCKS
    }
}

package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutAnimation extends Packet {
    private final int entityId;
    private final Animation animation;

    public PacketOutAnimation(int entityId, Animation animation) {
        this.entityId = entityId;
        this.animation = animation;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.entityId);
        buf.writeByte(this.animation.ordinal());
    }

    public enum Animation {
        SWING_ARM,
        TAKE_DAMAGE,
        LEAVE_BED,
        EAT_FOOD,
        CRITICAL_EFFECT,
        MAGIC_CRITICAL_EFFECT
    }
}

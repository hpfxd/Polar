package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutUpdateHealth extends Packet {
    private final float health;
    private final int food;
    private final float saturation;

    public PacketOutUpdateHealth(float health, int food, float saturation) {
        this.health = health;
        this.food = food;
        this.saturation = saturation;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeFloat(this.health);
        PacketUtils.writeVarInt(buf, this.food);
        buf.writeFloat(this.saturation);
    }
}

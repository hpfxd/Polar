package com.hpfxd.polar.network.packet.packets.login;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class PacketOutLoginSuccess extends Packet {
    private final UUID uuid;
    private final String username;

    public PacketOutLoginSuccess(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeString(buf, this.uuid.toString());
        PacketUtils.writeString(buf, this.username);
    }
}

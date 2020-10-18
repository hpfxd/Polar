package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class PacketOutChatMessage extends Packet {
    private final String json;
    private final int position;

    public PacketOutChatMessage(String json, int position) {
        this.json = json;
        this.position = position;
    }

    public PacketOutChatMessage(String json) {
        this.json = json;
        this.position = 0;
    }

    public PacketOutChatMessage(BaseComponent[] components) {
        this(ComponentSerializer.toString(components));
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeString(buf, this.json);
        buf.writeByte(this.position);
    }
}

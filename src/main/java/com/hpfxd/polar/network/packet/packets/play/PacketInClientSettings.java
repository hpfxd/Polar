package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.Player;
import io.netty.buffer.ByteBuf;

public class PacketInClientSettings extends Packet {
    private String locale;
    private byte viewDistance;
    private byte chatMode;
    private boolean chatColors;
    private byte skinParts;

    @Override
    public void read(ByteBuf buf) {
        this.locale = PacketUtils.readString(buf);
        this.viewDistance = buf.readByte();
        this.chatMode = buf.readByte();
        this.chatColors = buf.readBoolean();
        this.skinParts = buf.readByte();
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        Player player = channelHandler.getPlayer();

        player.getMetadata().setByte(10, this.skinParts);
        player.updateStatusMetadata();
    }
}

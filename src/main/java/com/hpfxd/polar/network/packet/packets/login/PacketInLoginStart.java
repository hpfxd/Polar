package com.hpfxd.polar.network.packet.packets.login;

import com.hpfxd.polar.Polar;
import com.hpfxd.polar.event.player.PlayerLoginEvent;
import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.network.packet.ProtocolState;
import com.hpfxd.polar.player.Player;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.UUID;

public class PacketInLoginStart extends Packet {
    private String name;

    @Override
    public void read(ByteBuf buf) {
        this.name = PacketUtils.readString(buf);
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        PlayerLoginEvent event = new PlayerLoginEvent(channelHandler, UUID.randomUUID(), this.name, new ArrayList<>());
        event.postEvent();

        channelHandler.setName(event.getName());
        channelHandler.sendPacket(new PacketOutLoginSuccess(event.getUuid(), event.getName()));
        channelHandler.setProtocolState(ProtocolState.PLAY);

        Player player = new Player(channelHandler, event.getUuid(), event.getName(), Polar.getPolar().getWorld(), event.getMojangProperties());
        channelHandler.setPlayer(player);
    }
}

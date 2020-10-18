package com.hpfxd.polar.network.packet.packets.handshake;

import com.hpfxd.polar.Polar;
import com.hpfxd.polar.event.player.PlayerHandshakeEvent;
import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.network.packet.ProtocolState;
import com.hpfxd.polar.util.TextComponents;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

public class PacketInHandshake extends Packet {
    @Getter private int protocolVersion;
    @Getter private String serverAddress;
    @Getter private int serverPort;
    private int nextState;

    @Override
    public void read(ByteBuf buf) {
        this.protocolVersion = PacketUtils.readVarInt(buf);
        this.serverAddress = PacketUtils.readString(buf);
        this.serverPort = buf.readUnsignedShort();
        this.nextState = PacketUtils.readVarInt(buf);
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        PlayerHandshakeEvent event = new PlayerHandshakeEvent(channelHandler, this.protocolVersion, this.serverAddress, this.serverPort, this.nextState);
        event.postEvent();

        if (event.getNextState() == 1) {
            channelHandler.setProtocolState(ProtocolState.STATUS);
        } else if (event.getNextState() == 2) {
            channelHandler.setProtocolState(ProtocolState.LOGIN);
        } else {
            throw new IllegalArgumentException("Next state should be either 1 or 2, instead got: " + this.nextState);
        }

        if (event.getProtocolVersion() != Polar.PROTOCOL_VERSION) {
            channelHandler.disconnect(TextComponents.UNSUPPORTED_VERSION);
        }
    }
}

package com.hpfxd.polar.network;

import com.hpfxd.polar.Polar;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.network.packet.PacketData;
import com.hpfxd.polar.network.packet.ProtocolDirection;
import com.hpfxd.polar.network.packet.ProtocolState;
import com.hpfxd.polar.network.packet.packets.login.PacketOutLoginDisconnect;
import com.hpfxd.polar.network.packet.packets.play.PacketOutDisconnect;
import com.hpfxd.polar.player.Player;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PolarChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private boolean connected = false;
    @Getter private Channel channel;
    @Getter @Setter private Player player;
    @Getter @Setter private ProtocolState protocolState = ProtocolState.HANDSHAKE;
    @Getter @Setter private String name;
    @Getter @Setter private boolean autoflush = true;
    @Getter @Setter private long lastKeepAliveSent;
    @Getter @Setter private long lastKeepAliveReceived;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.connected = true;
        this.channel = ctx.channel();
        log.info("Connection received.");
        Polar.getPolar().getNetworkManager().getChannelHandlers().add(this);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        this.destroyConnection();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        if (!this.connected) return;
        int packetId = PacketUtils.readVarInt(buf);
        PacketData packetData = Polar.getPolar().getNetworkManager().getPacketRegistry().getPacketDataById(this.protocolState, ProtocolDirection.IN, packetId);

        if (packetData != null) {
            Class<? extends Packet> clazz = packetData.getClazz();
            Packet packet = clazz.newInstance();

            packet.read(buf);
            packet.handle(this);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.destroyConnection();
        log.warn("Error in connection.", cause);
    }

    public void destroyConnection() {
        if (this.connected) {
            this.connected = false;

            Polar.getPolar().getNetworkManager().getChannelHandlers().remove(this);
            this.channel.close();
            if (this.player != null) this.player.destroy();
            log.info("Player disconnected.");
        }
    }

    public void sendPacket(Packet packet) {
        if (!this.connected) return;
        this.channel.write(packet);
        if (this.autoflush) this.channel.flush();
    }

    public void disconnect(String reason) {
        if (this.protocolState == ProtocolState.LOGIN) {
            this.sendPacket(new PacketOutLoginDisconnect(reason));
        } else if (this.protocolState == ProtocolState.PLAY) {
            this.sendPacket(new PacketOutDisconnect(reason));
        }

        this.destroyConnection();
    }
}

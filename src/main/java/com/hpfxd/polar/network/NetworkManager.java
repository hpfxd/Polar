package com.hpfxd.polar.network;

import com.hpfxd.polar.Polar;
import com.hpfxd.polar.network.packet.PacketRegistry;
import com.hpfxd.polar.network.pipeline.PacketEncoder;
import com.hpfxd.polar.network.pipeline.VarInt21Decoder;
import com.hpfxd.polar.network.pipeline.VarInt21Encoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NetworkManager {
    @Getter private final List<PolarChannelHandler> channelHandlers = new ArrayList<>();

    @Getter private final PacketRegistry packetRegistry;
    @Getter private final EventLoopGroup bossgroup;

    public NetworkManager() {
        this.packetRegistry = new PacketRegistry();
        this.packetRegistry.populateRegistry();
        if (Epoll.isAvailable()) {
            log.info("Using Epoll transport.");
        } else {
            log.info("Epoll not available, falling back to NIO. Reason: " + Epoll.unavailabilityCause().getMessage());
        }

        this.bossgroup = this.getEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(this.bossgroup)
                .channel(this.getChannel())
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.IP_TOS, 0x18) // https://en.wikipedia.org/wiki/Type_of_service
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("timer", new ReadTimeoutHandler(30))

                                .addLast("varintdecoder", new VarInt21Decoder())

                                .addLast("varintencoder", new VarInt21Encoder())
                                .addLast("packetencoder", new PacketEncoder())

                                .addLast("handler", new PolarChannelHandler());
                    }
                });

        try {
            Channel ch = bootstrap.bind(Polar.getPolar().getConfig().getHost(), Polar.getPolar().getConfig().getPort()).sync().channel();
            log.info("Listening for connections on " + ch.localAddress().toString() + ".");
        } catch (Exception e) {
            log.error("Failed to bind to port!", e);
            Polar.getPolar().shutdown();
        }
    }

    public void shutdown() {
        this.bossgroup.shutdownGracefully();
    }

    private EventLoopGroup getEventLoopGroup() {
        return Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    private Class<? extends ServerChannel> getChannel() {
        return Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }
}

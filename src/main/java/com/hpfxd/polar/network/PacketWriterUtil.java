package com.hpfxd.polar.network;

import com.hpfxd.polar.Polar;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.network.packet.PacketData;
import com.hpfxd.polar.network.packet.ProtocolDirection;
import com.hpfxd.polar.network.packet.ProtocolState;
import com.hpfxd.polar.player.Player;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketWriterUtil {
    private static final ExecutorService PACKET_WRITER_POOL = Executors.newFixedThreadPool(Polar.getPolar().getConfig().getPacketWriterExecutorPoolSize());

    public static ByteBuf writePacketToByteBuf(Packet packet, int packetId) {
        ByteBuf buf = Unpooled.buffer();
        PacketUtils.writeVarInt(buf, packetId);

        packet.write(buf);
        return buf;
    }

    public static int getPacketId(ProtocolState state, Packet packet) {
        PacketData data = Polar.getPolar().getNetworkManager().getPacketRegistry().getPacketDataByClass(state, ProtocolDirection.OUT, packet.getClass());
        if (data == null) {
            throw new NullPointerException("Packet data is null! " + packet.getClass().getName());
        }
        return data.getId();
    }

    public static void writeAndSend(Collection<Player> players, Packet packet) {
        if (players.isEmpty()) return;
        // todo use writer pool

        for (Player player : players) {
            final PolarChannelHandler channelHandler = player.getChannelHandler();
            channelHandler.sendPacket(packet);
        }
    }
}

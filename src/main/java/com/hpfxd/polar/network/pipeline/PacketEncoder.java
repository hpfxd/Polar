package com.hpfxd.polar.network.pipeline;

import com.hpfxd.polar.Polar;
import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.network.packet.PacketData;
import com.hpfxd.polar.network.packet.ProtocolDirection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        try {
            PolarChannelHandler channelHandler = (PolarChannelHandler) ctx.pipeline().get("handler");
            PacketData data = Polar.getPolar().getNetworkManager().getPacketRegistry().getPacketDataByClass(channelHandler.getProtocolState(), ProtocolDirection.OUT, packet.getClass());
            if (data == null) {
                throw new NullPointerException("Packet data is null! " + packet.getClass().getName());
            }
            PacketUtils.writeVarInt(out, data.getId());
            packet.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

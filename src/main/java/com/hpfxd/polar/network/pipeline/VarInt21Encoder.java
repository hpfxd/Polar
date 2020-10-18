package com.hpfxd.polar.network.pipeline;

import com.hpfxd.polar.network.PacketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class VarInt21Encoder extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        int bodyLen = msg.readableBytes();
        int headerLen = PacketUtils.getVarIntSize(bodyLen);
        out.ensureWritable(headerLen + bodyLen);

        PacketUtils.writeVarInt(out, bodyLen);
        out.writeBytes(msg);
    }
}

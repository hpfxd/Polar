package com.hpfxd.polar.network.packet;

import com.hpfxd.polar.network.PolarChannelHandler;
import io.netty.buffer.ByteBuf;

public class Packet {
    public void write(ByteBuf buf) {
        throw new RuntimeException("Write is not implemented!");
    }

    public void read(ByteBuf buf) {
        throw new RuntimeException("Read is not implemented!");
    }

    public void handle(PolarChannelHandler channelHandler) {
        throw new RuntimeException("Handle is not implemented!");
    }
}

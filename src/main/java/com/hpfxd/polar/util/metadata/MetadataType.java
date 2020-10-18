package com.hpfxd.polar.util.metadata;

import com.hpfxd.polar.network.PacketUtils;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.BiConsumer;

@AllArgsConstructor
@Getter
public enum MetadataType {
    BYTE((buf, obj) -> buf.writeByte((byte) obj)),
    SHORT((buf, obj) -> buf.writeShort((short) obj)),
    INT((buf, obj) -> buf.writeInt((int) obj)),
    FLOAT((buf, obj) -> buf.writeFloat((float) obj)),
    STRING((buf, obj) -> PacketUtils.writeString(buf, (String) obj))
    ;

    private final BiConsumer<ByteBuf, Object> writeHandler;
}

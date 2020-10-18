package com.hpfxd.polar.network.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PacketData {
    private final int id;
    private final ProtocolState state;
    private final ProtocolDirection direction;
    private final Class<? extends Packet> clazz;

    @Override
    public String toString() {
        return "PacketData{className=" + this.getClazz().getSimpleName() + ",id=" + this.id + ",state=" + this.state + ",direction=" + this.direction + "}";
    }
}

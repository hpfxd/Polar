package com.hpfxd.polar.event.player;

import com.hpfxd.polar.event.Event;
import com.hpfxd.polar.network.PolarChannelHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PlayerHandshakeEvent extends Event {
    private final PolarChannelHandler channelHandler;
    private int protocolVersion;
    private String serverAddress;
    private int serverPort;
    private int nextState;
}

package com.hpfxd.polar.event.player;

import com.hpfxd.polar.event.CancellableEvent;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.util.MojangProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PlayerLoginEvent extends CancellableEvent {
    private final PolarChannelHandler channelHandler;
    private UUID uuid;
    private String name;
    private List<MojangProperty> mojangProperties;
}

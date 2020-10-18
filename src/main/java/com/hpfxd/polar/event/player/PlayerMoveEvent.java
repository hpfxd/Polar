package com.hpfxd.polar.event.player;

import com.hpfxd.polar.event.CancellableEvent;
import com.hpfxd.polar.player.Player;
import com.hpfxd.polar.util.MutableLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PlayerMoveEvent extends CancellableEvent {
    private final Player player;
    private final MutableLocation from;
    private final MutableLocation to;
    private final double distanceSq;
}

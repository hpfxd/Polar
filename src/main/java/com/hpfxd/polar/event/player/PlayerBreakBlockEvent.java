package com.hpfxd.polar.event.player;

import com.hpfxd.polar.event.CancellableEvent;
import com.hpfxd.polar.player.Player;
import com.hpfxd.polar.util.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PlayerBreakBlockEvent extends CancellableEvent {
    private final Player player;
    private final Position position;
}

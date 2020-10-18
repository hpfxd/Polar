package com.hpfxd.polar.event.player;

import com.hpfxd.polar.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PlayerInventoryInteractEvent extends CancellableEvent {
    private final int windowId;
    private final int slot;
    private final int button;
    private final int actionNumber;
    private final int mode;
}

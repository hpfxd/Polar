package com.hpfxd.polar.event;

import lombok.Getter;
import lombok.Setter;

public class CancellableEvent extends Event {
    @Getter @Setter private boolean cancelled = false;
}

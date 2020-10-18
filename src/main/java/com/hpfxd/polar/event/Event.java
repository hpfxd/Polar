package com.hpfxd.polar.event;

import com.hpfxd.polar.Polar;

public class Event {
    public void postEvent() { // todo postEvent(World), 1 event manager per world
        Polar.getPolar().getEventManager().callEvent(this);
    }
}

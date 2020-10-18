package com.hpfxd.polar.event;

public interface EventHandler<E extends Event> {
    void handle(E event);
}

package com.hpfxd.polar.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager {
    private final Map<Class<? extends Event>, List<EventHandler<? extends Event>>> handlerMap = new ConcurrentHashMap<>();

    public <E extends Event> void registerEventHandler(Class<E> eventClass, EventHandler<E> eventHandler) {
        List<EventHandler<? extends Event>> list = this.handlerMap.computeIfAbsent(eventClass, k -> new ArrayList<>());

        list.add(eventHandler);
    }

    public <E extends Event> void removeEventHandler(Class<E> eventClass, EventHandler<E> eventHandler) {
        List<EventHandler<? extends Event>> list = this.handlerMap.get(eventClass);

        list.remove(eventHandler);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void callEvent(Event event) {
        List<EventHandler<? extends Event>> list = this.handlerMap.get(event.getClass());
        if (list != null) {
            for (EventHandler eventHandler : list) {
                eventHandler.handle(event);
            }
        }
    }
}

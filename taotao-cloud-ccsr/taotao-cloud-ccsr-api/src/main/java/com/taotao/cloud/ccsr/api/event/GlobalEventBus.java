package com.taotao.cloud.ccsr.api.event;// GlobalEventBus.java

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.taotao.cloud.ccsr.api.listener.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

public class GlobalEventBus {

    private static final EventBus INSTANCE = new AsyncEventBus(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2));

    private static final Set<Listener<?>> listeners = new HashSet<>();

    private GlobalEventBus() {
    }

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public static void post(Event event) {
        INSTANCE.post(event);
    }

    public static void register(Listener<?> listener) {
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
        INSTANCE.register(listener);
    }

    public static void unregister(Listener<?> listener) {
        if (!listeners.contains(listener)) {
            return;
        }
        listeners.remove(listener);
        INSTANCE.unregister(listener);
    }
}

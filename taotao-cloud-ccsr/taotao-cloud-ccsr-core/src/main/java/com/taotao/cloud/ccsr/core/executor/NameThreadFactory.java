package com.taotao.cloud.ccsr.core.executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NameThreadFactory implements ThreadFactory {
    
    private final AtomicInteger id = new AtomicInteger(0);
    
    private final String name;
    
    public NameThreadFactory(String name) {
        if (!name.endsWith(".")) {
            name +=".";
        }
        this.name = name;
    }
    
    @Override
    public Thread newThread(Runnable r) {
        String threadName = name + id.getAndIncrement();
        Thread thread = new Thread(r, threadName);
        thread.setDaemon(true);
        return thread;
    }
}

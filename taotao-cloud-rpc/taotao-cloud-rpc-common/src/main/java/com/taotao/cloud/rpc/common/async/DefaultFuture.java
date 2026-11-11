package com.taotao.cloud.rpc.common.async;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 默认 Future
 * <p>
 * 基于 {@code requestId -> Future} 映射的请求-响应匹配。
 */
public class DefaultFuture {

    private static final Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<>();

    private final long requestId;
    private final long timeoutMs;
    private volatile Object value;
    private volatile Throwable exception;
    private volatile boolean done = false;

    private DefaultFuture(long requestId, long timeoutMs) {
        this.requestId = requestId;
        this.timeoutMs = timeoutMs;
        FUTURES.put(requestId, this);
    }

    /**
     * 创建并注册一个 Future
     */
    public static DefaultFuture newFuture(long requestId, long timeoutMs) {
        return new DefaultFuture(requestId, timeoutMs);
    }

    /**
     * 收到响应
     */
    public static void received(long requestId, Object value) {
        DefaultFuture future = FUTURES.remove(requestId);
        if (future != null) {
            future.value = value;
            future.done = true;
        }
    }

    /**
     * 收到异常
     */
    public static void receivedException(long requestId, Throwable t) {
        DefaultFuture future = FUTURES.remove(requestId);
        if (future != null) {
            future.exception = t;
            future.done = true;
        }
    }

    /**
     * 同步获取结果
     */
    public Object get() throws Throwable {
        long start = System.currentTimeMillis();
        while (!done) {
            long elapsed = System.currentTimeMillis() - start;
            if (elapsed >= timeoutMs) {
                FUTURES.remove(requestId);
                throw new TimeoutException("Request " + requestId + " timed out after " + timeoutMs + "ms");
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        if (exception != null) throw exception;
        return value;
    }

    public long getRequestId() {
        return requestId;
    }

    public boolean isDone() {
        return done;
    }
}

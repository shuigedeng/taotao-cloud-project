package com.taotao.cloud.rpc.common.async;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * RPC 上下文（线程本地 + 异步上下文）
 * <p>
 * 携带本次调用的上下文信息：traceId、spanId、token、附件等。
 */
public class RpcContext {

    private static final Logger log = LogManager.getLogger(RpcContext.class);

    private static final ThreadLocal<RpcContext> LOCAL = new ThreadLocal<>();

    /** 全局 traceId 生成器 */
    private static final AtomicLong TRACE_ID_GEN = new AtomicLong(1);

    private final String traceId;
    private final String spanId;
    private final ConcurrentMap<String, String> attachments = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Object> internal = new ConcurrentHashMap<>();

    public RpcContext() {
        this.traceId = String.valueOf(TRACE_ID_GEN.incrementAndGet());
        this.spanId = "0";
    }

    public RpcContext(String traceId) {
        this.traceId = traceId;
        this.spanId = "0";
    }

    public static RpcContext getContext() {
        RpcContext ctx = LOCAL.get();
        if (ctx == null) {
            ctx = new RpcContext();
            LOCAL.set(ctx);
        }
        return ctx;
    }

    public static void setContext(RpcContext ctx) {
        if (ctx != null) {
            LOCAL.set(ctx);
        }
    }

    public static void clear() {
        LOCAL.remove();
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setAttachment(String key, String value) {
        attachments.put(key, value);
    }

    public String getAttachment(String key) {
        return attachments.get(key);
    }

    public ConcurrentMap<String, String> getAttachments() {
        return attachments;
    }

    /**
     * 生成新 traceId
     */
    public static String newTraceId() {
        return String.valueOf(TRACE_ID_GEN.incrementAndGet());
    }
}

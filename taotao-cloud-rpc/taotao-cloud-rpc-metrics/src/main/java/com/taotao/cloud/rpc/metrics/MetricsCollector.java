package com.taotao.cloud.rpc.metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 指标收集器
 * <p>
 * 全局单例，按 "service:method" 维度记录 QPS、RT、错误数等。
 */
public class MetricsCollector {

    private static final Logger log = LogManager.getLogger(MetricsCollector.class);

    private static final MetricsCollector INSTANCE = new MetricsCollector();

    public static MetricsCollector getInstance() {
        return INSTANCE;
    }

    /** 计数器：service:method -> {total, success, error} */
    private final Map<String, MetricsBucket> buckets = new ConcurrentHashMap<>();

    private MetricsCollector() {}

    /**
     * 记录一次调用
     */
    public void record(String service, String method, long rtMs, boolean success) {
        String key = service + ":" + method;
        MetricsBucket bucket = buckets.computeIfAbsent(key, k -> new MetricsBucket(service, method));
        bucket.total.incrementAndGet();
        if (success) {
            bucket.success.incrementAndGet();
        } else {
            bucket.error.incrementAndGet();
        }
        bucket.rt.record(rtMs);
    }

    /**
     * 获取指标的快照
     */
    public Map<String, MetricsBucket> snapshot() {
        return new ConcurrentHashMap<>(buckets);
    }

    /**
     * 获取所有指标
     */
    public Map<String, Map<String, Object>> getAll() {
        Map<String, Map<String, Object>> result = new ConcurrentHashMap<>();
        buckets.forEach((key, bucket) -> result.put(key, bucket.toMap()));
        return result;
    }

    /**
     * 清除所有指标
     */
    public void reset() {
        buckets.clear();
    }

    /**
     * 指标桶
     */
    public static class MetricsBucket {
        private final String service;
        private final String method;
        private final AtomicLong total = new AtomicLong();
        private final AtomicLong success = new AtomicLong();
        private final AtomicLong error = new AtomicLong();
        private final Histogram rt = new Histogram();

        public MetricsBucket(String service, String method) {
            this.service = service;
            this.method = method;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new ConcurrentHashMap<>();
            m.put("service", service);
            m.put("method", method);
            m.put("total", total.get());
            m.put("success", success.get());
            m.put("error", error.get());
            m.put("errorRate", total.get() == 0 ? 0 : (double) error.get() / total.get());
            m.put("rt", rt);
            m.put("p50", rt.getP50());
            m.put("p90", rt.getP90());
            m.put("p99", rt.getP99());
            m.put("avg", rt.getAvg());
            m.put("min", rt.getMin());
            m.put("max", rt.getMax());
            return m;
        }
    }
}

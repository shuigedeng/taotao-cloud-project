package com.taotao.cloud.rpc.metrics;

import com.taotao.cloud.rpc.metrics.MetricsCollector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 指标上报器
 * <p>
 * 周期性地把指标推送到 ttc-rpc-admin。
 * <p>
 * 当前为简化为日志输出。生产应使用 HTTP / Kafka / ttc-mq 推送。
 */
public class MetricsReporter {

    private static final Logger log = LogManager.getLogger(MetricsReporter.class);

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new java.util.concurrent.ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "ttc-rpc-metrics-reporter");
            t.setDaemon(true);
            return t;
        }
    });

    private volatile boolean started = false;

    public void start(long intervalSeconds) {
        if (started) return;
        started = true;
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                report();
            }
        }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
        log.info("MetricsReporter started, interval={}s", intervalSeconds);
    }

    public void stop() {
        scheduler.shutdownNow();
        started = false;
    }

    private void report() {
        try {
            Map<String, MetricsCollector.MetricsBucket> snapshot = MetricsCollector.getInstance().snapshot();
            if (log.isDebugEnabled()) {
                log.debug("Metrics snapshot ({} services/methods):", snapshot.size());
                for (Map.Entry<String, MetricsCollector.MetricsBucket> entry : snapshot.entrySet()) {
                    String key = entry.getKey();
                    Map<String, Object> m = entry.getValue().toMap();
                    log.debug("  {} -> total={}, success={}, error={}, p50={}ms, p99={}ms",
                            key, m.get("total"), m.get("success"), m.get("error"),
                            m.get("p50"), m.get("p99"));
                }
            }
        } catch (Exception e) {
            log.warn("Failed to report metrics: {}", e.getMessage());
        }
    }
}

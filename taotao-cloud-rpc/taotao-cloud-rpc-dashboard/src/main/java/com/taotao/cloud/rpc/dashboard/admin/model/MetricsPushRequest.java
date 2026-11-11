package com.taotao.cloud.rpc.dashboard.admin.model;

import java.util.Map;

/**
 * 指标推送 DTO
 */
public class MetricsPushRequest {
    private String appName;
    private String instance;
    private String side; // provider / consumer
    private long timestamp;
    private java.util.List<MetricsEntry> metrics;

    public static class MetricsEntry {
        private String service;
        private String method;
        private long qps;
        private long totalCount;
        private long errorCount;
        private double errorRate;
        private long p50;
        private long p90;
        private long p99;
        private long avg;
        private long activeCount;
        private Map<String, Object> extras;

        public String getService() { return service; }
        public void setService(String service) { this.service = service; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public long getQps() { return qps; }
        public void setQps(long qps) { this.qps = qps; }
        public long getTotalCount() { return totalCount; }
        public void setTotalCount(long totalCount) { this.totalCount = totalCount; }
        public long getErrorCount() { return errorCount; }
        public void setErrorCount(long errorCount) { this.errorCount = errorCount; }
        public double getErrorRate() { return errorRate; }
        public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
        public long getP50() { return p50; }
        public void setP50(long p50) { this.p50 = p50; }
        public long getP90() { return p90; }
        public void setP90(long p90) { this.p90 = p90; }
        public long getP99() { return p99; }
        public void setP99(long p99) { this.p99 = p99; }
        public long getAvg() { return avg; }
        public void setAvg(long avg) { this.avg = avg; }
        public long getActiveCount() { return activeCount; }
        public void setActiveCount(long activeCount) { this.activeCount = activeCount; }
        public Map<String, Object> getExtras() { return extras; }
        public void setExtras(Map<String, Object> extras) { this.extras = extras; }
    }

    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getInstance() { return instance; }
    public void setInstance(String instance) { this.instance = instance; }
    public String getSide() { return side; }
    public void setSide(String side) { this.side = side; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public java.util.List<MetricsEntry> getMetrics() { return metrics; }
    public void setMetrics(java.util.List<MetricsEntry> metrics) { this.metrics = metrics; }
}

package com.taotao.cloud.mq.consistency.ttc.client.producer.latency;

import java.io.Serializable;

/**
 * Broker 延迟/故障信息（对标 RocketMQ LatencyFaultItem）.
 * <p>
 * 每个 Broker 维护独立的故障记录，包含:
 * <ul>
 *   <li>currentLatency: 最近一次发送耗时 (ms)</li>
   <li>faultDurationMs: 该 Broker 需要被回避的时长 (由 currentLatency 等级决定)</li>
 *   <li>startTimestamp: 开始回避的时间点</li>
 *   <li>notAvailableCount / errorCount: 连续不可用/出错次数</li>
 * </ul>
 */
public class LatencyFaultInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 最近一次发送耗时 (ms). */
    private volatile long currentLatency;

    /** 需要回避的持续时长 (ms). */
    private volatile long faultDurationMs;

    /** 故障开始时间 (ms timestamp). */
    private volatile long startTimestamp;

    /** 连续不可用次数. */
    private volatile int notAvailableCount;

    /** 连续出错次数. */
    private volatile int errorCount;

    public LatencyFaultInfo() {
    }

    public LatencyFaultInfo(long currentLatency, long faultDurationMs, long startTimestamp) {
        this.currentLatency = currentLatency;
        this.faultDurationMs = faultDurationMs;
        this.startTimestamp = startTimestamp;
    }

    /**
     * 是否处于回避期 (当前时间在 startTimestamp + faultDurationMs 之内).
     * <p>约定: faultDurationMs = 0 表示"无回避，始终可用".
     */
    public boolean isAvailable() {
        return System.currentTimeMillis() - startTimestamp >= faultDurationMs;
    }

    /**
     * 距离可用还剩多少 ms (负数表示已可用).
     */
    public long getAvailableDelay() {
        return faultDurationMs - (System.currentTimeMillis() - startTimestamp);
    }

    // ===== Getters / Setters =====

    public long getCurrentLatency() {
        return currentLatency;
    }

    public void setCurrentLatency(long currentLatency) {
        this.currentLatency = currentLatency;
    }

    public long getFaultDurationMs() {
        return faultDurationMs;
    }

    public void setFaultDurationMs(long faultDurationMs) {
        this.faultDurationMs = faultDurationMs;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public int getNotAvailableCount() {
        return notAvailableCount;
    }

    public void setNotAvailableCount(int notAvailableCount) {
        this.notAvailableCount = notAvailableCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    @Override
    public String toString() {
        return "LatencyFaultInfo{" +
                "latency=" + currentLatency +
                ", faultDuration=" + faultDurationMs +
                ", available=" + isAvailable() +
                ", notAvailCnt=" + notAvailableCount +
                ", errCnt=" + errorCount +
                '}';
    }
}

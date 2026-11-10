package com.taotao.cloud.mq.client.producer.latency;

import com.taotao.cloud.mq.common.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认延迟故障容错实现.
 * <p>
 * 每个 Broker 独立维护故障信息，发送后根据延迟等级自动计算回避时长:
 * <pre>
 * latency  < 500ms  → 不回避 (正常)
 * 500ms ~ 1000ms   → 回避 10s
 * 1s     ~ 5000ms  → 回避 30s
 * 5s     ~ 10000ms → 回避 60s
 * > 10s             → 回避 120s
 * </pre>
 * 发送失败时回避 30s.
 */
public class DefaultLatencyFaultTolerance implements LatencyFaultTolerance {

    private static final Logger log = LoggerFactory.getLogger(DefaultLatencyFaultTolerance.class);

    /** brokerName → LatencyFaultInfo. */
    private final ConcurrentHashMap<String, LatencyFaultInfo> faultTable = new ConcurrentHashMap<>();

    /** 发送失败时默认回避 30s. */
    private static final long DEFAULT_ERROR_FAULT_DURATION_MS = 30_000L;

    @Override
    public boolean isAvailable(String brokerName) {
        LatencyFaultInfo info = faultTable.get(brokerName);
        if (info == null) {
            return true;
        }
        return info.isAvailable();
    }

    @Override
    public void recordLatency(String brokerName, long latencyMs) {
        LatencyFaultInfo info = faultTable.get(brokerName);
        if (info == null) {
            info = new LatencyFaultInfo();
            faultTable.put(brokerName, info);
        }

        info.setCurrentLatency(latencyMs);
        long durationMs = computeFaultDuration(latencyMs);

        if (durationMs > 0) {
            // 需要回避
            info.setFaultDurationMs(durationMs);
            info.setStartTimestamp(System.currentTimeMillis());
            info.setNotAvailableCount(info.getNotAvailableCount() + 1);
            log.warn("Broker {} latency {}ms exceeds threshold, fault for {}ms",
                    brokerName, latencyMs, durationMs);
        } else {
            // 正常: 如果之前有回避记录则清除
            if (info.getFaultDurationMs() > 0) {
                info.setFaultDurationMs(0L);
                info.setStartTimestamp(0L);
                log.info("Broker {} recovered, latency {}ms", brokerName, latencyMs);
            }
        }
    }

    @Override
    public void markFault(String brokerName, Throwable cause) {
        LatencyFaultInfo info = faultTable.get(brokerName);
        if (info == null) {
            info = new LatencyFaultInfo();
            faultTable.put(brokerName, info);
        }

        info.setFaultDurationMs(DEFAULT_ERROR_FAULT_DURATION_MS);
        info.setStartTimestamp(System.currentTimeMillis());
        info.setErrorCount(info.getErrorCount() + 1);
        log.warn("Broker {} marked fault due to {} (errCount={})",
                brokerName, cause != null ? cause.getMessage() : "unknown", info.getErrorCount());
    }

    @Override
    public Set<MessageQueue> filterAvailableQueues(Set<MessageQueue> queues) {
        if (queues == null || queues.isEmpty()) {
            return queues;
        }
        Set<MessageQueue> available = new HashSet<>();
        for (MessageQueue mq : queues) {
            if (isAvailable(mq.getBrokerName())) {
                available.add(mq);
            }
        }
        return available;
    }

    @Override
    public LatencyFaultInfo getFaultInfo(String brokerName) {
        return faultTable.get(brokerName);
    }

    /**
     * 根据延迟等级计算需要回避的时长.
     *
     * @return 回避时长 (ms), 0 表示不回避
     */
    static long computeFaultDuration(long latencyMs) {
        if (latencyMs < 500L) {
            return 0L;
        } else if (latencyMs < 1_000L) {
            return 10_000L;
        } else if (latencyMs < 5_000L) {
            return 30_000L;
        } else if (latencyMs < 10_000L) {
            return 60_000L;
        } else {
            return 120_000L;
        }
    }

    /**
     * 清除所有故障记录 (用于测试或 reset).
     */
    public void clear() {
        faultTable.clear();
    }

    /**
     * 获取当前故障表 (只读视图，用于测试).
     */
    public ConcurrentHashMap<String, LatencyFaultInfo> getFaultTable() {
        return faultTable;
    }
}

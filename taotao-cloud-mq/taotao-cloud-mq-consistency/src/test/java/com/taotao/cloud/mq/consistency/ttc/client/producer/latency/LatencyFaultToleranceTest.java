package com.taotao.cloud.mq.consistency.ttc.client.producer.latency;

import com.taotao.cloud.mq.consistency.ttc.common.MessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * LatencyFaultTolerance 单元测试.
 */
@DisplayName("LatencyFaultTolerance 测试")
public class LatencyFaultToleranceTest {

    private DefaultLatencyFaultTolerance tolerance;

    @BeforeEach
    public void setUp() {
        tolerance = new DefaultLatencyFaultTolerance();
    }

    // ===== LatencyFaultInfo 测试 =====

    @Test
    @DisplayName("LatencyFaultInfo 默认构造: isAvailable = true (faultDuration=0)")
    public void testInfoDefaultConstructor() {
        LatencyFaultInfo info = new LatencyFaultInfo();
        assertTrue(info.isAvailable(), "default: no fault, should be available");
        assertEquals(0, info.getCurrentLatency());
        assertEquals(0, info.getFaultDurationMs());
    }

    @Test
    @DisplayName("LatencyFaultInfo: faultDuration=0 时 isAvailable = true")
    public void testInfoAvailable() {
        LatencyFaultInfo info = new LatencyFaultInfo(100L, 0L, System.currentTimeMillis());
        assertTrue(info.isAvailable(), "faultDuration=0 → should be available immediately");
    }

    @Test
    @DisplayName("LatencyFaultInfo: startTimestamp 设为过去, faultDuration 之内, isAvailable = false")
    public void testInfoNotAvailable() {
        long now = System.currentTimeMillis();
        LatencyFaultInfo info = new LatencyFaultInfo(200L, 60_000L, now - 5_000L);
        assertFalse(info.isAvailable(), "should not be available within fault duration");
        assertTrue(info.getAvailableDelay() > 0, "available delay should be positive");
    }

    @Test
    @DisplayName("LatencyFaultInfo: faultDuration 已过, isAvailable = true")
    public void testInfoExpired() {
        long now = System.currentTimeMillis();
        LatencyFaultInfo info = new LatencyFaultInfo(200L, 1000L, now - 2000L);
        assertTrue(info.isAvailable(), "should be available after fault duration expired");
        assertTrue(info.getAvailableDelay() < 0, "available delay should be negative");
    }

    // ===== DefaultLatencyFaultTolerance 测试 =====

    @Test
    @DisplayName("新 Broker: isAvailable = true (无记录)")
    public void testNewBrokerAvailable() {
        assertTrue(tolerance.isAvailable("broker-0"));
    }

    @Test
    @DisplayName("computeFaultDuration: < 500ms → 0 (不回避)")
    public void testComputeDurationNormal() {
        assertEquals(0, DefaultLatencyFaultTolerance.computeFaultDuration(100));
        assertEquals(0, DefaultLatencyFaultTolerance.computeFaultDuration(499));
    }

    @Test
    @DisplayName("computeFaultDuration: 500-999ms → 10s")
    public void testComputeDurationLevel1() {
        assertEquals(10_000, DefaultLatencyFaultTolerance.computeFaultDuration(500));
        assertEquals(10_000, DefaultLatencyFaultTolerance.computeFaultDuration(999));
    }

    @Test
    @DisplayName("computeFaultDuration: 1000-4999ms → 30s")
    public void testComputeDurationLevel2() {
        assertEquals(30_000, DefaultLatencyFaultTolerance.computeFaultDuration(1000));
        assertEquals(30_000, DefaultLatencyFaultTolerance.computeFaultDuration(4999));
    }

    @Test
    @DisplayName("computeFaultDuration: 5000-9999ms → 60s")
    public void testComputeDurationLevel3() {
        assertEquals(60_000, DefaultLatencyFaultTolerance.computeFaultDuration(5000));
        assertEquals(60_000, DefaultLatencyFaultTolerance.computeFaultDuration(9999));
    }

    @Test
    @DisplayName("computeFaultDuration: >= 10000ms → 120s")
    public void testComputeDurationLevel4() {
        assertEquals(120_000, DefaultLatencyFaultTolerance.computeFaultDuration(10000));
        assertEquals(120_000, DefaultLatencyFaultTolerance.computeFaultDuration(30000));
    }

    @Test
    @DisplayName("recordLatency: 正常延迟 (< 500ms) 不回避")
    public void testRecordLatencyNormal() {
        tolerance.recordLatency("broker-0", 100L);
        assertTrue(tolerance.isAvailable("broker-0"), "should remain available");
        assertNotNull(tolerance.getFaultInfo("broker-0"));
        assertEquals(0L, tolerance.getFaultInfo("broker-0").getFaultDurationMs());
    }

    @Test
    @DisplayName("recordLatency: 高延迟 (1000ms) 触发回避 30s")
    public void testRecordLatencyHigh() {
        tolerance.recordLatency("broker-1", 2000L);
        assertFalse(tolerance.isAvailable("broker-1"), "should be unavailable after high latency");
        assertEquals(30_000L, tolerance.getFaultInfo("broker-1").getFaultDurationMs());
        assertEquals(1, tolerance.getFaultInfo("broker-1").getNotAvailableCount());
    }

    @Test
    @DisplayName("markFault: 发送失败标记 Broker 30s 不可用")
    public void testMarkFault() {
        tolerance.markFault("broker-2", new RuntimeException("connection reset"));
        assertFalse(tolerance.isAvailable("broker-2"));
        assertNotNull(tolerance.getFaultInfo("broker-2"));
        assertEquals(30_000L, tolerance.getFaultInfo("broker-2").getFaultDurationMs());
        assertEquals(1, tolerance.getFaultInfo("broker-2").getErrorCount());
    }

    @Test
    @DisplayName("filterAvailableQueues: 过滤不可用 Broker 的队列")
    public void testFilterQueues() {
        tolerance.markFault("broker-1", null); // broker-1 不可用

        Set<MessageQueue> queues = new HashSet<>();
        queues.add(new MessageQueue("topic-A", "broker-0", 0));
        queues.add(new MessageQueue("topic-A", "broker-0", 1));
        queues.add(new MessageQueue("topic-A", "broker-1", 0)); // 不可用
        queues.add(new MessageQueue("topic-A", "broker-2", 0));

        Set<MessageQueue> available = tolerance.filterAvailableQueues(queues);
        assertEquals(3, available.size(), "broker-1 queues should be filtered out");
        for (MessageQueue mq : available) {
            assertFalse("broker-1".equals(mq.getBrokerName()),
                    "broker-1 queues should not be present");
        }
    }

    @Test
    @DisplayName("filterAvailableQueues: null/empty 返回原集合")
    public void testFilterQueuesNull() {
        assertEquals(null, tolerance.filterAvailableQueues(null));
        Set<MessageQueue> empty = new HashSet<>();
        assertEquals(empty, tolerance.filterAvailableQueues(empty));
    }

    @Test
    @DisplayName("clear: 清空所有故障记录")
    public void testClear() {
        tolerance.markFault("broker-0", null);
        tolerance.recordLatency("broker-1", 5000L);
        tolerance.clear();
        assertTrue(tolerance.isAvailable("broker-0"));
        assertTrue(tolerance.isAvailable("broker-1"));
    }

    @Test
    @DisplayName("多次 markFault: errorCount 累加")
    public void testMultipleMarkFault() {
        tolerance.markFault("broker-3", null);
        tolerance.markFault("broker-3", null);
        tolerance.markFault("broker-3", null);
        assertEquals(3, tolerance.getFaultInfo("broker-3").getErrorCount());
    }

    @Test
    @DisplayName("正常延迟后恢复: 从不可用恢复到可用")
    public void testRecovery() {
        // 先标记故障
        tolerance.markFault("broker-4", null);
        assertFalse(tolerance.isAvailable("broker-4"));

        // 记录正常延迟 (应该清除回避)
        tolerance.recordLatency("broker-4", 50L);
        assertTrue(tolerance.isAvailable("broker-4"), "should recover after normal latency");
    }
}

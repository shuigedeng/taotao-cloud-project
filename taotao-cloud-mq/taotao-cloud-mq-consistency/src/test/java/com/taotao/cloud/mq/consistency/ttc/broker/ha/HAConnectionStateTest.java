package com.taotao.cloud.mq.consistency.ttc.broker.ha;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * HAConnectionState 单元测试.
 */
@DisplayName("HAConnectionState 测试")
public class HAConnectionStateTest {

    private HAConnectionState state;
    private String slaveAddr;

    @BeforeEach
    public void setUp() {
        slaveAddr = "192.168.1.10:10912-" + UUID.randomUUID().toString().substring(0, 8);
        state = new HAConnectionState(slaveAddr);
    }

    @AfterEach
    public void tearDown() {
        // no-op
    }

    @Test
    @DisplayName("初始 ack offset = -1, createTime = now")
    public void testInitial() {
        assertEquals(slaveAddr, state.getSlaveAddr());
        assertEquals(-1L, state.getLastAckOffset());
        assertTrue(state.getCreateTimeMs() > 0);
        assertTrue(state.getLastHeartbeatMs() > 0);
    }

    @Test
    @DisplayName("updateAckOffset 更新 offset 与心跳")
    public void testUpdateAck() throws Exception {
        long t0 = state.getLastHeartbeatMs();
        Thread.sleep(10);
        state.updateAckOffset(12345L);
        assertEquals(12345L, state.getLastAckOffset());
        assertTrue(state.getLastHeartbeatMs() > t0, "heartbeat should be bumped");
    }

    @Test
    @DisplayName("isExpired: 默认 30s 超时阈值, 新建连接不应过期")
    public void testIsExpired() {
        assertFalse(state.isExpired(30_000L), "fresh state should not be expired");
        assertFalse(state.isExpired(0L), "0ms threshold means always fresh");
    }

    @Test
    @DisplayName("toString 包含 slave 地址")
    public void testToString() {
        state.updateAckOffset(999L);
        String s = state.toString();
        assertNotNull(s);
        assertTrue(s.contains(slaveAddr));
        assertTrue(s.contains("999"));
    }
}

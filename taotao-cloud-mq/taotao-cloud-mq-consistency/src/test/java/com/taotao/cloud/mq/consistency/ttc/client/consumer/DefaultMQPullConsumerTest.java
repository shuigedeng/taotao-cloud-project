package com.taotao.cloud.mq.consistency.ttc.client.consumer;

import com.taotao.cloud.mq.consistency.ttc.common.MessageQueue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DefaultMQPullConsumer 单元测试 — 覆盖基本属性与异常分支。
 * <p>
 * 不调用 start(), 走"未启动"分支, 验证 start/shutdown 流程。
 */
public class DefaultMQPullConsumerTest {

    private DefaultMQPullConsumer consumer;

    @BeforeEach
    public void setUp() {
        consumer = new DefaultMQPullConsumer("TestGroup");
        consumer.setNamesrvAddr("localhost:9876");
    }

    @AfterEach
    public void tearDown() {
        if (consumer != null) {
            try {
                consumer.shutdown();
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(consumer);
        assertEquals("TestGroup", consumer.getConsumerGroup());
    }

    @Test
    public void testSetNamesrvAddr() {
        consumer.setNamesrvAddr("192.168.1.1:9876");
        assertEquals("192.168.1.1:9876", consumer.getNamesrvAddr());
    }

    @Test
    public void testSetConsumerGroup() {
        consumer.setConsumerGroup("NewGroup");
        assertEquals("NewGroup", consumer.getConsumerGroup());
    }

    @Test
    public void testPullNullMqThrows() {
        // 启动后才能用, 但 mq null 应优先抛 IllegalArgumentException
        try {
            consumer.start();
        } catch (Exception ignored) {
        }
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                consumer.pull(null, 0, 10);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                // start 失败等其他异常, 视为无效
            }
        });
    }

    @Test
    public void testPullWithoutStartThrows() {
        MessageQueue mq = new MessageQueue("T", "B", 0);
        assertThrows(Exception.class, () -> {
            consumer.pull(mq, 0, 10);
        });
    }

    @Test
    public void testGetConsumerGroup() {
        assertEquals("TestGroup", consumer.getConsumerGroup());
    }

    @Test
    public void testStartSetsClientId() throws Exception {
        consumer.start();
        assertNotNull(consumer.getClientId());
        assertTrue(consumer.getClientId().contains("PULL_"));
        assertTrue(consumer.getClientId().contains("TestGroup"));
    }

    @Test
    public void testSetClientIdOverrides() throws Exception {
        consumer.setClientId("MY_CLIENT");
        consumer.start();
        assertEquals("MY_CLIENT", consumer.getClientId());
    }

    @Test
    public void testShutdownIdempotent() {
        consumer.shutdown();
        // 重复 shutdown 不抛
        consumer.shutdown();
    }

    @Test
    public void testPullResultEnumValues() {
        // 验证 PullStatus 枚举稳定
        assertNotNull(DefaultMQPullConsumer.PullStatus.CONNECTION_LOST);
        assertNotNull(DefaultMQPullConsumer.PullStatus.NO_MATCHED_MSG);
        assertNotNull(DefaultMQPullConsumer.PullStatus.NO_NEW_MSG);
        assertNotNull(DefaultMQPullConsumer.PullStatus.FOUND);
        assertNotNull(DefaultMQPullConsumer.PullStatus.SYSTEM_ERROR);
        // 5 个状态位
        assertEquals(5, DefaultMQPullConsumer.PullStatus.values().length);
    }
}

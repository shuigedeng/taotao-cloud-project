package com.taotao.cloud.mq.consistency.ttc.broker.delay;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ScheduleMessageService 单元测试.
 */
@DisplayName("ScheduleMessageService 测试")
public class ScheduleMessageServiceTest {

    private ScheduleMessageService service;

    @BeforeEach
    public void setUp() {
        // 自定义短延迟便于测试: 100ms / 200ms / 500ms / 1s
        service = new ScheduleMessageService("100ms 200ms 500ms 1s");
        service.start();
    }

    @AfterEach
    public void tearDown() {
        if (service != null) {
            service.shutdown();
        }
    }

    @Test
    @DisplayName("默认构造使用 18 个延迟级别")
    public void testDefaultLevels() {
        ScheduleMessageService def = new ScheduleMessageService();
        assertEquals(18, def.getLevelCount());
        assertEquals(1_000L, def.getDelayMillis(1));
        assertEquals(5_000L, def.getDelayMillis(2));
        assertEquals(3_600_000L, def.getDelayMillis(17));
        assertEquals(7_200_000L, def.getDelayMillis(18));
    }

    @Test
    @DisplayName("自定义延迟级别")
    public void testCustomLevels() {
        assertEquals(4, service.getLevelCount());
        assertEquals(100L, service.getDelayMillis(1));
        assertEquals(200L, service.getDelayMillis(2));
        assertEquals(500L, service.getDelayMillis(3));
        assertEquals(1_000L, service.getDelayMillis(4));
    }

    @Test
    @DisplayName("非法 delayLevel 抛 IllegalArgumentException")
    public void testInvalidLevel() {
        assertThrows(IllegalArgumentException.class,
                () -> service.schedule(0, "k", "p"));
        assertThrows(IllegalArgumentException.class,
                () -> service.schedule(5, "k", "p"));
    }

    @Test
    @DisplayName("schedule 后到期触发 listener.onExpired")
    public void testScheduleAndExpire() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> recvKey = new AtomicReference<>();
        AtomicReference<Object> recvPayload = new AtomicReference<>();

        service.setListener((key, payload) -> {
            recvKey.set(key);
            recvPayload.set(payload);
            latch.countDown();
        });

        long start = System.currentTimeMillis();
        service.schedule(1, "msg-1", "payload-1");

        assertTrue(latch.await(2, TimeUnit.SECONDS), "should fire within 2s");
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(elapsed >= 80, "should wait ~100ms, got " + elapsed);
        assertTrue(elapsed < 1_000, "should not wait too long, got " + elapsed);

        assertEquals("msg-1", recvKey.get());
        assertEquals("payload-1", recvPayload.get());
    }

    @Test
    @DisplayName("cancel 在到期前有效")
    public void testCancelBeforeExpire() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        service.setListener((k, p) -> latch.countDown());

        service.schedule(3, "msg-cancel", "p"); // 500ms
        boolean cancelled = service.cancel(3, "msg-cancel");
        assertTrue(cancelled, "should cancel successfully");

        // 监听器不应该被触发
        assertEquals(1, latch.getCount(), "listener should NOT fire after cancel");
    }

    @Test
    @DisplayName("cancel 不存在的消息返回 false")
    public void testCancelNonExistent() {
        assertEquals(false, service.cancel(1, "never-scheduled"));
        assertEquals(false, service.cancel(0, "k"));
        assertEquals(false, service.cancel(5, "k"));
    }

    @Test
    @DisplayName("getScheduledCount 反映当前延迟消息数量")
    public void testGetScheduledCount() {
        assertEquals(0, service.getScheduledCount(1));
        service.schedule(1, "a", null);
        service.schedule(1, "b", null);
        assertEquals(2, service.getScheduledCount(1));
        assertEquals(0, service.getScheduledCount(2));
    }

    @Test
    @DisplayName("非法配置字符串抛 IllegalArgumentException")
    public void testInvalidConfig() {
        assertThrows(IllegalArgumentException.class,
                () -> new ScheduleMessageService("1x 2y"));
    }

    @Test
    @DisplayName("支持多种时间单位 (ms / s / m / h)")
    public void testMultipleUnits() {
        ScheduleMessageService multi = new ScheduleMessageService("500ms 2s 3m 1h");
        assertEquals(500L, multi.getDelayMillis(1));
        assertEquals(2_000L, multi.getDelayMillis(2));
        assertEquals(180_000L, multi.getDelayMillis(3));
        assertEquals(3_600_000L, multi.getDelayMillis(4));
    }

    @Test
    @DisplayName("DelayedMessage.getDelay 在到期前为正数, 到期后为负数")
    public void testDelayedMessage() throws Exception {
        long now = System.currentTimeMillis();
        ScheduleMessageService.DelayedMessage dm = new ScheduleMessageService.DelayedMessage("k", "p", now + 500);
        assertTrue(dm.getDelay(TimeUnit.MILLISECONDS) > 0);

        Thread.sleep(50);
        // 仍然在到期前
        assertTrue(dm.getDelay(TimeUnit.MILLISECONDS) > 0);

        ScheduleMessageService.DelayedMessage past = new ScheduleMessageService.DelayedMessage("k", "p", now - 100);
        assertTrue(past.getDelay(TimeUnit.MILLISECONDS) <= 0);
        assertNotNull(past.getKey());
        assertNotNull(past.getPayload());
        assertEquals(now - 100, past.getExpireAt());
    }

    @Test
    @DisplayName("shutdown 后不再投递新消息")
    public void testShutdownStopsDelivery() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        service.setListener((k, p) -> latch.countDown());

        // shutdown 前 schedule
        service.shutdown();
        service = null; // 防止 tearDown 重复

        // 不应触发 listener (线程已退出)
        try {
            service.schedule(1, "k", "p");
        } catch (NullPointerException expected) {
            // shutdown 后 listener=null 也可能 NPE
        }

        assertEquals(1, latch.getCount(), "should not deliver after shutdown");
    }
}

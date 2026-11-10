package com.taotao.cloud.mq.broker.longpoll;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PullRequestHoldService 单元测试.
 */
@DisplayName("PullRequestHoldService 测试")
public class PullRequestHoldServiceTest {

    private PullRequestHoldService service;

    @BeforeEach
    public void setUp() {
        // 缩短 hold timeout 便于测试 (500ms)
        service = new PullRequestHoldService(500L, 100);
        service.start();
    }

    @AfterEach
    public void tearDown() {
        if (service != null) {
            service.shutdown();
        }
    }

    @Test
    @DisplayName("suspendPull 返回 SuspendedPull 句柄可用于 awaitWakeup")
    public void testSuspendReturnsHandle() {
        PullRequestHoldService.SuspendedPull req = service.suspendPull("T1", 0, 100);
        assertNotNull(req, "should accept suspend");
        assertEquals("T1", req.getTopic());
        assertEquals(0, req.getQueueId());
        assertEquals(100, req.getOffset());
        assertEquals(1, service.totalHoldCount());
    }

    @Test
    @DisplayName("新消息到达唤醒挂起的请求")
    public void testNotifyMessageArrived() throws Exception {
        final PullRequestHoldService.SuspendedPull[] reqHolder = new PullRequestHoldService.SuspendedPull[1];
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean wokenByMsg = new AtomicBoolean(false);

        Thread waiter = new Thread(() -> {
            try {
                reqHolder[0] = service.suspendPull("T1", 0, 100);
                assertNotNull(reqHolder[0]);
                boolean byMsg = reqHolder[0].awaitWakeup();
                wokenByMsg.set(byMsg);
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        waiter.start();

        // 等到 waiter 进入 suspend 后再唤醒
        Thread.sleep(100);
        service.notifyMessageArrived("T1", 0);

        assertTrue(latch.await(2, TimeUnit.SECONDS), "should be woken up within 2s");
        assertTrue(wokenByMsg.get(), "should be woken by message (not timeout)");
        waiter.join(2_000);
    }

    @Test
    @DisplayName("max hold 限制生效 — 超限返回 null")
    public void testMaxHoldLimit() {
        PullRequestHoldService small = new PullRequestHoldService(60_000L, 3);
        try {
            assertNotNull(small.suspendPull("T", 0, 0));
            assertNotNull(small.suspendPull("T", 1, 0));
            assertNotNull(small.suspendPull("T", 2, 0));
            assertNull(small.suspendPull("T", 3, 0), "4th suspend should be rejected");
            assertEquals(3, small.totalHoldCount());
        } finally {
            small.shutdown();
        }
    }

    @Test
    @DisplayName("cancelSuspend 清空挂起")
    public void testCancelSuspend() {
        service.suspendPull("T1", 0, 0);
        service.suspendPull("T1", 0, 1);
        service.suspendPull("T1", 1, 0);
        assertEquals(3, service.totalHoldCount());

        service.cancelSuspend("T1", 0);
        assertEquals(1, service.totalHoldCount());
        assertFalse(service.hasSuspended("T1", 0));
        assertTrue(service.hasSuspended("T1", 1));
    }

    @Test
    @DisplayName("shutdown 唤醒所有挂起的请求 (返回 false 表示非消息唤醒)")
    public void testShutdownWakeup() throws Exception {
        final PullRequestHoldService.SuspendedPull[] reqHolder = new PullRequestHoldService.SuspendedPull[1];
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean wokenByMsg = new AtomicBoolean(true); // 期望变成 false (shutdown 唤醒)

        Thread waiter = new Thread(() -> {
            try {
                reqHolder[0] = service.suspendPull("T1", 0, 0);
                boolean byMsg = reqHolder[0].awaitWakeup();
                wokenByMsg.set(byMsg);
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        waiter.start();
        Thread.sleep(100);

        service.shutdown();
        service = null; // 防止 tearDown 重复 shutdown

        assertTrue(latch.await(2, TimeUnit.SECONDS), "should be woken by shutdown");
        assertFalse(wokenByMsg.get(), "shutdown should wakeup by NOT message");
    }

    @Test
    @DisplayName("timeout 后自动唤醒 (返回 false)")
    public void testTimeoutWakeup() throws Exception {
        PullRequestHoldService.SuspendedPull req = service.suspendPull("T1", 0, 0);
        assertNotNull(req);

        long start = System.currentTimeMillis();
        boolean wokenByMsg = req.awaitWakeup(); // 500ms hold timeout
        long elapsed = System.currentTimeMillis() - start;

        assertFalse(wokenByMsg, "should be woken by timeout, not message");
        assertTrue(elapsed >= 400, "should wait ~500ms, got " + elapsed);
        assertTrue(elapsed < 2_000, "should not wait too long, got " + elapsed);
    }

    @Test
    @DisplayName("notifyMessageArrived 多 queueId 唤醒")
    public void testNotifyMessageArrivedMultiple() {
        service.suspendPull("T", 0, 0);
        service.suspendPull("T", 1, 0);
        service.suspendPull("T", 2, 0);
        assertEquals(3, service.totalHoldCount());

        service.notifyMessageArrived("T", java.util.Arrays.asList(0, 1, 2));
        assertEquals(0, service.totalHoldCount());
    }

    @Test
    @DisplayName("notifyMessageArrived 不存在的 queueId 不抛异常")
    public void testNotifyNoExist() {
        service.notifyMessageArrived("NOT_EXIST", 0); // 不应抛
        assertEquals(0, service.totalHoldCount());
    }

    @Test
    @DisplayName("hasSuspended 反映挂起状态")
    public void testHasSuspended() {
        assertFalse(service.hasSuspended("T1", 0));
        service.suspendPull("T1", 0, 0);
        assertTrue(service.hasSuspended("T1", 0));
    }
}

package com.taotao.cloud.mq.plugin.store.log;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ReferenceResource 单元测试 — 验证引用计数 + shutdown 强制回收逻辑。
 */
public class ReferenceResourceTest {

    /**
     * 简单的可关闭资源,用于驱动 cleanup 逻辑。
     */
    static class FakeResource extends ReferenceResource {
        final AtomicInteger cleanupCount = new AtomicInteger(0);

        @Override
        public boolean cleanup(long currentRef) {
            cleanupCount.incrementAndGet();
            this.cleanupOver = true;
            return true;
        }
    }

    @Test
    public void testInitialRefCountIsOne() {
        FakeResource r = new FakeResource();
        assertEquals(1L, r.getRefCount());
        assertTrue(r.isAvailable());
        assertFalse(r.isCleanupOver());
    }

    @Test
    public void testHoldIncrementsRefCount() {
        FakeResource r = new FakeResource();
        assertTrue(r.hold());
        assertEquals(2L, r.getRefCount());
        assertTrue(r.hold());
        assertEquals(3L, r.getRefCount());
    }

    @Test
    public void testReleaseDecrementsAndCleans() {
        FakeResource r = new FakeResource();
        r.hold();
        r.hold();
        r.release();
        assertEquals(2L, r.getRefCount());
        assertFalse(r.isCleanupOver());

        r.release();
        assertEquals(1L, r.getRefCount());
        assertFalse(r.isCleanupOver());

        r.release();
        assertEquals(0L, r.getRefCount());
        assertTrue(r.isCleanupOver());
        assertEquals(1, r.cleanupCount.get());
    }

    @Test
    public void testReleaseOverCleanupReentrant() {
        FakeResource r = new FakeResource();
        r.release();
        r.release();
        r.release();
        // 已知行为: release 在 refCount<=0 后会反复调用 cleanup, 直到 refCount 持续下降。
        // 这是 ReferenceResource 的设计 (cleanup 应当幂等) 但不是 noop 模式。
        // 验证 cleanup 被调用 ≥ 1 次, 多次调用也是 OK 的, 由 cleanup 自己保证幂等。
        org.junit.jupiter.api.Assertions.assertTrue(r.cleanupCount.get() >= 1);
    }

    @Test
    public void testHoldAfterUnavailableFails() {
        FakeResource r = new FakeResource();
        r.shutdown(1000L);
        assertFalse(r.isAvailable());
        assertFalse(r.hold());
    }

    @Test
    public void testShutdownForcedWhenIntervalExceeded() throws Exception {
        FakeResource r = new FakeResource();
        // 制造"还有人在用"的场景: hold 一次, refCount=2
        r.hold();
        r.shutdown(10L);
        assertFalse(r.isAvailable());
        // shutdown 第一次: 走 release 分支, refCount 由 2 降到 1, 1>0 不走 cleanup
        assertEquals(1L, r.getRefCount());
        assertEquals(0, r.cleanupCount.get());

        // 等待 intervalForcibly 超时后, 再次 shutdown 才走强制回收分支
        Thread.sleep(50L);
        r.shutdown(0L);
        // 第二次 shutdown: refCount=1>0, 走强制回收: refCount.set(-1000-1)=-1001, 然后 release
        // release 让 refCount 变成 -1002, value<=0, 进入 cleanup
        assertEquals(1, r.cleanupCount.get());
        assertTrue(r.isCleanupOver());
    }

    @Test
    public void testShutdownRepeatedSafe() {
        FakeResource r = new FakeResource();
        r.shutdown(1000L);
        r.shutdown(1000L);
        r.shutdown(1000L);
        assertEquals(1, r.cleanupCount.get());
    }

    @Test
    public void testLastModifiedWithoutFileReturnsZero() {
        FakeResource r = new FakeResource();
        assertEquals(0L, r.getLastModifiedTimestamp());
    }

    @Test
    public void testLastModifiedWithFile() throws Exception {
        FakeResource r = new FakeResource();
        File f = File.createTempFile("refres", ".tmp");
        f.deleteOnExit();
        r.setFile(f);
        long ts = r.getLastModifiedTimestamp();
        // 文件刚被 create, 应当有非零 mtime
        org.junit.jupiter.api.Assertions.assertTrue(ts > 0L);
    }

    @Test
    public void testConcurrentHoldReleaseStable() throws Exception {
        FakeResource r = new FakeResource();
        int threads = 8;
        int loops = 1000;
        CountDownLatch done = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < loops; j++) {
                        if (r.hold()) {
                            r.release();
                        }
                    }
                } finally {
                    done.countDown();
                }
            }).start();
        }
        assertTrue(done.await(10, TimeUnit.SECONDS));
        // 初始 refCount=1, threads*loops 应当 +1 +1 +1 -1 -1 -1 = 0 净变化
        // (hold 成功 +1, release -1)
        assertEquals(1L, r.getRefCount());
    }
}

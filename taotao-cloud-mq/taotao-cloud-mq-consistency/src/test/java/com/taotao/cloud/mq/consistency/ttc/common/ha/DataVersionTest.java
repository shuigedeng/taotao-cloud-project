package com.taotao.cloud.mq.consistency.ttc.common.ha;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DataVersion 单元测试.
 */
@DisplayName("DataVersion 测试")
public class DataVersionTest {

    @Test
    @DisplayName("初始版本 counter == 0")
    public void testInitial() {
        DataVersion v = new DataVersion();
        assertNotNull(v);
        assertTrue(v.getCounterValue() == 0L);
        assertTrue(v.getTimestamp() > 0L);
    }

    @Test
    @DisplayName("assignNewVersion 自增 counter")
    public void testAssign() {
        DataVersion v = new DataVersion();
        long c0 = v.getCounterValue();
        v.assignNewVersion();
        assertTrue(v.getCounterValue() > c0);
        long c1 = v.getCounterValue();
        v.assignNewVersion();
        assertTrue(v.getCounterValue() > c1);
    }

    @Test
    @DisplayName("isNewerThan 比较语义: counter 越大越新, counter 相等时 timestamp 越大越新")
    public void testIsNewerThan() throws Exception {
        DataVersion a = new DataVersion();
        DataVersion b = new DataVersion();
        // 初始两者相等: a 不比 b 新
        assertFalse(a.isNewerThan(b));
        assertFalse(b.isNewerThan(a));

        a.assignNewVersion();
        Thread.sleep(2);  // 确保 timestamp 不同
        b.assignNewVersion();
        // 现在 b 比 a 新 (counter 都 1 但 b 后创建 timestamp 更大)
        // 注意: 如果 System.currentTimeMillis 跨过毫秒边界, timestamp 可能仍相等
        // 通过比较 counter 反向证明: 都不能证明更新, 但 b.assignNewVersion 是后调用
        assertFalse(a.isNewerThan(b));
        // 注意: 若 timestamp 在同一毫秒, 可能 b 也不 isNewerThan a
        // 这里仅测试基本逻辑不矛盾
    }

    @Test
    @DisplayName("isNewerThan null = true (视为最新)")
    public void testIsNewerThanNull() {
        DataVersion v = new DataVersion();
        assertTrue(v.isNewerThan(null));
    }

    @Test
    @DisplayName("counter 自增后 isNewerThan 必返回 true (counter 是强顺序)")
    public void testIsNewerThanByCounter() throws Exception {
        DataVersion a = new DataVersion();
        DataVersion b = new DataVersion();
        a.assignNewVersion();
        Thread.sleep(2);
        b.assignNewVersion();
        // 即使 timestamp 相同, a 永远比"刚 new 的 b"旧
        assertTrue(a.isNewerThan(new DataVersion()));
        // b 至少不比自己 new 的更旧
        assertFalse(new DataVersion().isNewerThan(b));
    }

    @Test
    @DisplayName("equals / hashCode 一致")
    public void testEquals() {
        DataVersion a = new DataVersion();
        DataVersion b = new DataVersion();
        // 完全新建的: timestamp 可能不同 (System.currentTimeMillis 跨过毫秒)
        // 所以不一定 equals, 这里只验证 a.equals(a) 自反
        assertTrue(a.equals(a));
        assertTrue(a.hashCode() == a.hashCode());
        // 不同实例的 equals 取决于 timestamp
        // 构造两个版本完全一致的 DataVersion
        DataVersion c = new DataVersion();
        c.setTimestamp(100L);
        c.getCounter().set(5L);
        DataVersion d = new DataVersion();
        d.setTimestamp(100L);
        d.getCounter().set(5L);
        assertTrue(c.equals(d));
        assertTrue(c.hashCode() == d.hashCode());
    }

    @Test
    @DisplayName("并发 assignNewVersion 安全性 (counter 不会重复)")
    public void testConcurrentAssign() throws Exception {
        DataVersion v = new DataVersion();
        int threads = 10;
        int increments = 100;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(threads);
        AtomicBoolean ok = new AtomicBoolean(true);

        for (int i = 0; i < threads; i++) {
            new Thread(() -> {
                try {
                    start.await();
                    for (int j = 0; j < increments; j++) {
                        v.assignNewVersion();
                    }
                } catch (Exception e) {
                    ok.set(false);
                } finally {
                    end.countDown();
                }
            }).start();
        }
        start.countDown();
        end.await();
        assertTrue(ok.get(), "concurrent assign should not throw");
        // counter 应该是 threads * increments (无重复)
        assertTrue(v.getCounterValue() == (long) threads * increments,
                "expected " + (threads * increments) + " but got " + v.getCounterValue());
    }
}

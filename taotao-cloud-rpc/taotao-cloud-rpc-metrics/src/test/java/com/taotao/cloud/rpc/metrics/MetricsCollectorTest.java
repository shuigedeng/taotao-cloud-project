package com.taotao.cloud.rpc.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MetricsCollector 首批测试。
 * <p>
 * 收集器是 JVM 单例，用例之间靠 {@code reset()} + 各用不同 service 名来隔离计数。
 * 所有期望值都取自源码：key = {@code service + ":" + method}，{@code toMap()} 恰好 13 个键。
 */
class MetricsCollectorTest {

    private static final Set<String> EXPECTED_KEYS = new HashSet<>(Arrays.asList(
            "service", "method", "total", "success", "error", "errorRate", "rt",
            "p50", "p90", "p99", "avg", "min", "max"));

    private final MetricsCollector collector = MetricsCollector.getInstance();

    @BeforeEach
    void cleanSlate() {
        collector.reset();
    }

    @Test
    @DisplayName("getInstance 是全局单例")
    void instanceIsGlobalSingleton() {
        assertSame(MetricsCollector.getInstance(), MetricsCollector.getInstance());
        assertSame(collector, MetricsCollector.getInstance());
    }

    @Test
    @DisplayName("key 的拼法是 service + ':' + method")
    void keyIsServiceColonMethod() {
        collector.record("com.zifang.demo.Alpha", "ping", 3, true);
        Map<String, MetricsCollector.MetricsBucket> snap = collector.snapshot();
        // 猎物：单键确实建了桶
        assertTrue(snap.containsKey("com.zifang.demo.Alpha:ping"), snap.keySet().toString());
        assertEquals(1, snap.size());
    }

    @Test
    @DisplayName("toMap 恰好 13 个键，一个不多一个不少")
    void toMapHasExactlyThirteenKeys() {
        collector.record("com.zifang.demo.Beta", "sum", 7, true);
        Map<String, Object> m = collector.getAll().get("com.zifang.demo.Beta:sum");
        assertNotNull(m);
        assertEquals(EXPECTED_KEYS, m.keySet(), m.keySet().toString());
        assertEquals(13, m.size());
        assertEquals("com.zifang.demo.Beta", m.get("service"));
        assertEquals("sum", m.get("method"));
        assertEquals(1L, m.get("total"));
        assertEquals(1L, m.get("success"));
        assertEquals(0L, m.get("error"));
        assertEquals(0.0, (Double) m.get("errorRate"), 1e-9);
    }

    @Test
    @DisplayName("errorRate 就是 error/total，不是 error/success")
    void errorRateIsErrorOverTotal() {
        for (int i = 0; i < 3; i++) {
            collector.record("com.zifang.demo.Gamma", "call", 5, true);
        }
        collector.record("com.zifang.demo.Gamma", "call", 5, false);
        Map<String, Object> m = collector.getAll().get("com.zifang.demo.Gamma:call");
        assertEquals(4L, m.get("total"));
        assertEquals(3L, m.get("success"));
        assertEquals(1L, m.get("error"));
        assertEquals(0.25, (Double) m.get("errorRate"), 1e-9);
    }

    @Test
    @DisplayName("bug_ 新建的空桶已经报出非零分位数（58），total 却是 0")
    void bug_aFreshBucketAlreadyReportsNonZeroPercentiles() {
        MetricsCollector.MetricsBucket fresh = new MetricsCollector.MetricsBucket("s", "m");
        Map<String, Object> m = fresh.toMap();
        // 猎物：计数确实是 0
        assertEquals(0L, m.get("total"));
        assertEquals(0.0, (Double) m.get("errorRate"), 1e-9, "total=0 走的是 ? 0 : ... 那一支");
        // 实测行为：分位数继承 Histogram 的空表缺陷
        assertEquals(58L, m.get("p50"));
        assertEquals(58L, m.get("p90"));
        assertEquals(58L, m.get("p99"));
        assertEquals(0.0, (Double) m.get("avg"), 1e-9);
    }

    @Test
    @DisplayName("bug_ service 名里带冒号时两个不同服务并成同一个桶")
    void bug_keyIsAmbiguousWhenServiceContainsColon() {
        // 猎物：正常名字各归各键
        collector.record("aa", "bb", 1, true);
        collector.record("bb", "aa", 1, true);
        assertEquals(2, collector.getAll().size());

        collector.reset();
        collector.record("a:b", "c", 1, true);
        collector.record("a", "b:c", 2, false);
        Map<String, Map<String, Object>> all = collector.getAll();
        assertEquals(1, all.size(), "两个不同的 (service, method) 拼出了同一个 key");
        Map<String, Object> merged = all.get("a:b:c");
        assertNotNull(merged);
        assertEquals(2L, merged.get("total"));
        // 第二个调用方的身份被吞了：桶里留的是第一个桶的 service/method
        assertEquals("a:b", merged.get("service"));
        assertEquals("c", merged.get("method"));
    }

    @Test
    @DisplayName("bug_ snapshot() 是浅拷贝：桶对象还在被写，快照会自己长大")
    void bug_snapshotIsShallow() throws Exception {
        collector.record("com.zifang.demo.Delta", "run", 1, true);
        Map<String, MetricsCollector.MetricsBucket> snap = collector.snapshot();
        MetricsCollector.MetricsBucket bucket = snap.get("com.zifang.demo.Delta:run");
        assertEquals(1L, bucket.toMap().get("total"));

        collector.record("com.zifang.demo.Delta", "run", 1, true);
        collector.record("com.zifang.demo.Delta", "run", 1, true);
        assertEquals(3L, bucket.toMap().get("total"), "拿着旧快照的人会看到数字偷偷涨");

        // 猎物：getAll() 给的是当场算好的 Map 拷贝，不跟着涨
        Map<String, Map<String, Object>> frozen = collector.getAll();
        collector.record("com.zifang.demo.Delta", "run", 1, true);
        assertEquals(3L, frozen.get("com.zifang.demo.Delta:run").get("total"));
        assertEquals(4L, collector.getAll().get("com.zifang.demo.Delta:run").get("total"));

        // 但 getAll() 里 "rt" 键挂的还是那个活的 Histogram 对象（桶没有公开 getter，只能反射）
        Field rtField = MetricsCollector.MetricsBucket.class.getDeclaredField("rt");
        rtField.setAccessible(true);
        assertSame(rtField.get(bucket), frozen.get("com.zifang.demo.Delta:run").get("rt"));
    }

    @Test
    @DisplayName("snapshot() 返回的 Map 本身是新表，改它不伤收集器")
    void snapshotMapItselfIsACopy() {
        collector.record("com.zifang.demo.Epsilon", "one", 1, true);
        Map<String, MetricsCollector.MetricsBucket> snap = collector.snapshot();
        assertNotSame(snap, collector.snapshot());
        snap.remove("com.zifang.demo.Epsilon:one");
        assertEquals(0, snap.size());
        assertTrue(collector.snapshot().containsKey("com.zifang.demo.Epsilon:one"));
    }

    @Test
    @DisplayName("reset 清空全部桶")
    void resetClearsEveryBucket() {
        collector.record("com.zifang.demo.Zeta", "one", 1, true);
        collector.record("com.zifang.demo.Eta", "one", 1, true);
        assertEquals(2, collector.snapshot().size());
        collector.reset();
        assertEquals(0, collector.snapshot().size());
        assertEquals(0, collector.getAll().size());
        // 猎物：reset 之后还能重新建桶
        collector.record("com.zifang.demo.Zeta", "one", 1, true);
        assertEquals(1, collector.snapshot().size());
    }

    @Test
    @DisplayName("bug_ 只能整体 reset，没有任何按 key 摘除或容量上限")
    void bug_noPerKeyRemovalAndNoEvictionBound() throws Exception {
        // 猎物：全清的手段是有的
        assertNotNull(MetricsCollector.class.getDeclaredMethod("reset"));
        assertNotNull(MetricsCollector.class.getDeclaredMethod("record",
                String.class, String.class, long.class, boolean.class));

        boolean hasRemove = false;
        for (java.lang.reflect.Method m : MetricsCollector.class.getDeclaredMethods()) {
            String n = m.getName().toLowerCase();
            if (n.contains("remove") || n.contains("evict") || n.contains("drop")) {
                hasRemove = true;
            }
        }
        assertFalse(hasRemove, "没有按服务/方法摘除桶的入口：一个调用方名字就能永久占位");

        Field buckets = MetricsCollector.class.getDeclaredField("buckets");
        // 声明类型是接口 Map，具体实现才是 ConcurrentHashMap —— 两者都要钉住
        assertEquals(Map.class, buckets.getType());
        buckets.setAccessible(true);
        Object live = buckets.get(collector);
        assertEquals(ConcurrentHashMap.class, live.getClass(), "没有任何容量上限的普通并发表");
        assertThrows(NoSuchFieldException.class, () -> MetricsCollector.class.getDeclaredField("maxKeys"));
    }

    @Test
    @DisplayName("并发 record 到同一个 key：total 与 success+error 精确相符")
    void concurrentRecordKeepsCountersExact() throws Exception {
        final int threads = 8;
        final int per = 200;
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(threads);
        for (int t = 0; t < threads; t++) {
            final boolean odd = (t % 2 == 1);
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        start.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    for (int i = 0; i < per; i++) {
                        collector.record("com.zifang.demo.Theta", "hot", 2, !odd);
                    }
                    done.countDown();
                }
            }, "collector-race-probe");
            th.setDaemon(true);
            th.start();
        }
        start.countDown();
        assertTrue(done.await(10, TimeUnit.SECONDS), "并发探针 10 秒没跑完");

        Map<String, Object> m = collector.getAll().get("com.zifang.demo.Theta:hot");
        assertNotNull(m);
        assertEquals((long) threads * per, m.get("total"));
        long success = (Long) m.get("success");
        long error = (Long) m.get("error");
        assertEquals((long) threads * per, success + error);
        assertEquals(4L * per, error, "4 个奇数线程各 200 次失败");
        assertEquals(0.5, (Double) m.get("errorRate"), 1e-9);
    }
}

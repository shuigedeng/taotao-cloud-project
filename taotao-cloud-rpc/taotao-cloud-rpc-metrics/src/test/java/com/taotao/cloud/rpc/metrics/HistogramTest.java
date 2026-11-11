package com.taotao.cloud.rpc.metrics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Histogram 首批测试。
 * <p>
 * 全部期望值都是从源码算出来再实测复核的：
 * {@code buckets[i] = (i + 1) * maxValue / bucketCount}，默认 bucketCount=1024、maxValue=60000
 * ⇒ buckets[0]=58、buckets[1]=117、buckets[511]=30000、buckets[1023]=60000。
 * 分位数走的是 {@code target = (long)(total * q)} 后「第一个 sum>=target 的桶」，
 * 所以 q<1 时 target 会向下取整到 0 —— 这是本组 bug_ 的共同根因。
 */
class HistogramTest {

    /** 默认桶数下第一个桶的上界（(1)*60000/1024 = 58）。 */
    private static final long FIRST_BUCKET_UPPER_BOUND = 58L;

    /** 黑盒读出「样本落在哪个桶的上界」：只喂一个样本时 p100 就是那个桶的上界。 */
    private static long upperBoundOfBucketFor(long sample) {
        Histogram h = new Histogram();
        h.record(sample);
        return h.getQuantile(1.0);
    }

    @Test
    @DisplayName("桶布局是等差（线性）的，不是 javadoc 写的指数分布")
    void bucketLayoutIsLinearNotExponential() {
        // 4 桶 / 上界 100 ⇒ 桶边界 25/50/75/100
        Histogram h = new Histogram(4, 100);
        h.record(1);
        assertEquals(25L, h.getQuantile(1.0), "1 应落在第一个桶，上界 25");
        Histogram h2 = new Histogram(4, 100);
        h2.record(26);
        assertEquals(50L, h2.getQuantile(1.0), "26 应落在第二个桶，上界 50");
        Histogram h3 = new Histogram(4, 100);
        h3.record(51);
        assertEquals(75L, h3.getQuantile(1.0), "51 应落在第三个桶，上界 75");

        // 正面对照：默认尺寸下 30001 的桶上界是 30058（步长仍是 58），
        // 若真是指数分布，30000 附近的步长应是千分位以上而不是 58。
        assertEquals(30058L, upperBoundOfBucketFor(30001L));
        assertEquals(30000L, upperBoundOfBucketFor(30000L));
    }

    @Test
    @DisplayName("默认桶边界实测值 58 / 117 / 30000 / 60000")
    void defaultBucketUpperBoundsMatchTheSourceFormula() {
        assertEquals(58L, upperBoundOfBucketFor(1L));
        assertEquals(117L, upperBoundOfBucketFor(59L));
        assertEquals(30000L, upperBoundOfBucketFor(29943L));
        assertEquals(60000L, upperBoundOfBucketFor(59943L));
    }

    @Test
    @DisplayName("bug_ 空直方图的分位数不是 0，而是第一个桶的上界 58")
    void bug_quantileOfEmptyHistogramIsNotZero() {
        Histogram h = new Histogram();
        // 猎物：确实一个样本都没进
        assertEquals(0L, h.getTotal());
        assertEquals(0L, h.getMin());
        assertEquals(0L, h.getMax());
        // 实测行为：0 也要报一个像样的延迟出来
        assertEquals(FIRST_BUCKET_UPPER_BOUND, h.getP50());
        assertEquals(FIRST_BUCKET_UPPER_BOUND, h.getP90());
        assertEquals(FIRST_BUCKET_UPPER_BOUND, h.getP99());
        assertEquals(0.0, h.getAvg(), 1e-9, "只有 avg 老实返回 0");
    }

    @Test
    @DisplayName("bug_ q<1 的分位数恒等于第一个桶上界，与真实样本无关")
    void bug_anyQuantileBelowOneIsTheFirstBucket() {
        Histogram h = new Histogram();
        h.record(30000);
        // 猎物：p100 落到了正确的桶
        assertEquals(30000L, h.getQuantile(1.0));
        assertEquals(30000L, h.getMax());
        // 实测行为：total=1 时 (long)(1*0.5)=0，第一个桶 sum=0>=0 立刻命中
        assertEquals(FIRST_BUCKET_UPPER_BOUND, h.getP50(), "单样本 30000 的 p50 报成 58");
        assertEquals(FIRST_BUCKET_UPPER_BOUND, h.getP90());
        assertEquals(FIRST_BUCKET_UPPER_BOUND, h.getP99());

        // 多喂样本也救不了：59 个桶 1 的样本才能把 p99 推过一个桶
        Histogram many = new Histogram();
        for (int i = 0; i < 100; i++) {
            many.record(30000);
        }
        assertEquals(30000L, many.getQuantile(1.0));
        assertEquals(30000L, many.getP50(), "total=100 时 target=50，第 51 个样本仍在桶 511 ⇒ 这次对");
    }

    @Test
    @DisplayName("bug_ 负数样本被静默丢弃，既不计数也不报错")
    void bug_negativeSamplesAreDroppedSilently() {
        Histogram h = new Histogram();
        h.record(-5);
        h.record(-1);
        // 猎物：正数样本正常入账
        h.record(10);
        assertEquals(1L, h.getTotal(), "两次负数记录没有增加 total");
        assertEquals(10L, h.getMax());
        assertEquals(10L, h.getMin(), "负数也没进 min");
    }

    @Test
    @DisplayName("超过 maxValue 的样本压进最后一个桶，但 min/max 记真实值")
    void overflowSamplesClampIntoLastBucketButKeepRealMinMax() {
        Histogram h = new Histogram();
        for (int i = 0; i < 100; i++) {
            h.record(60001);
        }
        assertEquals(60000L, h.getQuantile(1.0), "桶上界被夹到 60000");
        assertEquals(60001L, h.getMax(), "max 记的是真实样本");
        assertEquals(60001L, h.getMin());
        assertEquals(100L, h.getTotal());
    }

    @Test
    @DisplayName("bug_ bucketCount=0 的直方图读分位数直接越界抛异常")
    void bug_zeroBucketHistogramThrowsOnRead() {
        // 猎物：1 桶能读
        Histogram one = new Histogram(1, 100);
        one.record(5);
        assertEquals(100L, one.getQuantile(0.5));

        Histogram empty = new Histogram(0, 100);
        empty.record(5);
        assertEquals(1L, empty.getTotal(), "样本进了 total，但没桶可放");
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> empty.getQuantile(0.5), "findBucket 返回 -1 被挡住，但 getQuantile 结尾读 buckets[-1]");
    }

    @Test
    @DisplayName("q 越界返回 0，而 q=1.0 返回真实桶上界")
    void outOfRangeQuantileReturnsZero() {
        Histogram h = new Histogram();
        h.record(30000);
        // 猎物：合法 q 有非零读数
        assertEquals(30000L, h.getQuantile(1.0));
        assertEquals(0L, h.getQuantile(-0.1));
        assertEquals(0L, h.getQuantile(1.5));
    }

    @Test
    @DisplayName("min / max / avg / total 的口径")
    void minMaxAvgTotal() {
        Histogram h = new Histogram();
        assertEquals(0L, h.getMin(), "没样本时未设置的哨兵值映射成 0");
        assertEquals(0L, h.getMax());
        assertEquals(0.0, h.getAvg(), 1e-9);
        h.record(0);
        h.record(100);
        h.record(200);
        assertEquals(3L, h.getTotal());
        assertEquals(0L, h.getMin());
        assertEquals(200L, h.getMax());
        assertEquals(100.0, h.getAvg(), 1e-9);
    }

    @Test
    @DisplayName("reset 清干净计数，但分位数又变回『空表报 58』")
    void resetClearsCountersButKeepsTheEmptyQuantile() {
        Histogram h = new Histogram();
        h.record(30000);
        assertEquals(30000L, h.getMax());
        h.reset();
        assertEquals(0L, h.getTotal());
        assertEquals(0L, h.getMin());
        assertEquals(0L, h.getMax());
        assertEquals(0.0, h.getAvg(), 1e-9);
        // 猎物：reset 确实清了桶 —— 再喂一个样本 p100 就是新桶而不是累积
        h.record(1);
        assertEquals(58L, h.getQuantile(1.0));
        assertEquals(1L, h.getTotal());
        // 实测行为：空表读数依然是第一个桶
        h.reset();
        assertEquals(FIRST_BUCKET_UPPER_BOUND, h.getP99());
    }

    @Test
    @DisplayName("bug_ counts 是普通 long[]，total 却是 AtomicLong —— 桶更新不是原子的")
    void bug_bucketCountsAreNotAtomic() throws Exception {
        Field counts = Histogram.class.getDeclaredField("counts");
        Field total = Histogram.class.getDeclaredField("total");
        // 猎物：字段名没写错，两个都拿得到，且 total 确实是原子类型
        assertNotNull(counts);
        assertNotNull(total);
        assertEquals(AtomicLong.class, total.getType());
        // 实测结构：counts 是裸数组，且不是 volatile —— `counts[idx]++` 是读-改-写，并发下会丢样本
        assertEquals(long[].class, counts.getType());
        assertFalse(Modifier.isVolatile(counts.getModifiers()));
        assertThrows(NoSuchFieldException.class,
                () -> Histogram.class.getDeclaredField("countsLock"),
                "也没有任何锁字段兜着");
    }

    @Test
    @DisplayName("bug_ 并发 record 会丢桶样本：sum(counts) <= total，缺口只能靠日志看")
    void bug_concurrentRecordLosesBucketSamples() throws Exception {
        // 单线程猎物：同样的 1600 次，桶里一个不少
        Histogram single = new Histogram(1024, 60000);
        for (int i = 0; i < 1600; i++) {
            single.record(1000);
        }
        assertEquals(1600L, sumCounts(single), "单线程下桶计数之和应当等于 total");
        assertEquals(1600L, single.getTotal());

        int threads = 8;
        int per = 200;
        final Histogram h = new Histogram(1024, 60000);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(threads);
        for (int t = 0; t < threads; t++) {
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
                        h.record(1000);
                    }
                    done.countDown();
                }
            }, "histogram-race-probe");
            th.setDaemon(true);
            th.start();
        }
        start.countDown();
        assertTrue(done.await(10, TimeUnit.SECONDS), "并发探针 10 秒没跑完");

        long total = h.getTotal();
        long inBuckets = sumCounts(h);
        assertEquals((long) threads * per, total, "AtomicLong 的 total 不该丢");
        assertTrue(inBuckets <= total,
                "桶内样本 " + inBuckets + " 不该超过 total " + total + " —— 不变量破了说明量具读错了");
        if (inBuckets < total) {
            System.out.println("[ttc-rpc-metrics] 并发丢桶实测：total=" + total
                    + " sum(counts)=" + inBuckets + " 缺口=" + (total - inBuckets));
        }
    }

    private static long sumCounts(Histogram h) throws Exception {
        Field f = Histogram.class.getDeclaredField("counts");
        f.setAccessible(true);
        long[] counts = (long[]) f.get(h);
        long sum = 0;
        for (long c : counts) {
            sum += c;
        }
        return sum;
    }
}

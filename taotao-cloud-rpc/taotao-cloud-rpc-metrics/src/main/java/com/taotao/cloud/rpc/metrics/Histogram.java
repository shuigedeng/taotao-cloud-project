package com.taotao.cloud.rpc.metrics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 直方图（简单实现，统计 P50/P90/P99 等分位数）
 * <p>
 * 内部用桶（bucket）方式存储样本，最大桶数为 1024。
 */
public class Histogram {

    private final long[] buckets;
    private final long[] counts;
    private final long maxValue;
    private final AtomicLong total = new AtomicLong(0);
    private final AtomicLong sum = new AtomicLong(0);
    private final AtomicLong min = new AtomicLong(Long.MAX_VALUE);
    private final AtomicLong max = new AtomicLong(Long.MIN_VALUE);

    public Histogram() {
        this(1024, 60_000); // 默认 1024 桶，最大值 60s
    }

    public Histogram(int bucketCount, long maxValue) {
        this.buckets = new long[bucketCount];
        this.counts = new long[bucketCount];
        this.maxValue = maxValue;
        for (int i = 0; i < bucketCount; i++) {
            // 指数分布：bucket[i] = (i+1) * maxValue / bucketCount / 2
            this.buckets[i] = (i + 1) * maxValue / bucketCount;
        }
    }

    /**
     * 记录一个样本
     */
    public void record(long value) {
        if (value < 0) return;
        int idx = findBucket(value);
        if (idx >= 0 && idx < buckets.length) {
            counts[idx]++;
        }
        total.incrementAndGet();
        sum.addAndGet(value);
        // 更新 min/max
        long curMin = min.get();
        while (value < curMin) {
            if (min.compareAndSet(curMin, value)) break;
            curMin = min.get();
        }
        long curMax = max.get();
        while (value > curMax) {
            if (max.compareAndSet(curMax, value)) break;
            curMax = max.get();
        }
    }

    private int findBucket(long value) {
        for (int i = 0; i < buckets.length; i++) {
            if (value <= buckets[i]) return i;
        }
        return buckets.length - 1;
    }

    /**
     * 获取分位数
     *
     * @param q 0~1 之间
     */
    public long getQuantile(double q) {
        if (q < 0 || q > 1) return 0;
        long target = (long) (total.get() * q);
        long sum = 0;
        for (int i = 0; i < counts.length; i++) {
            sum += counts[i];
            if (sum >= target) {
                return buckets[i];
            }
        }
        return buckets[buckets.length - 1];
    }

    public long getP50() {
        return getQuantile(0.50);
    }

    public long getP90() {
        return getQuantile(0.90);
    }

    public long getP99() {
        return getQuantile(0.99);
    }

    public long getMin() {
        long v = min.get();
        return v == Long.MAX_VALUE ? 0 : v;
    }

    public long getMax() {
        long v = max.get();
        return v == Long.MIN_VALUE ? 0 : v;
    }

    public long getTotal() {
        return total.get();
    }

    public double getAvg() {
        long t = total.get();
        return t == 0 ? 0 : (double) sum.get() / t;
    }

    public void reset() {
        for (int i = 0; i < counts.length; i++) {
            counts[i] = 0;
        }
        total.set(0);
        sum.set(0);
        min.set(Long.MAX_VALUE);
        max.set(Long.MIN_VALUE);
    }
}

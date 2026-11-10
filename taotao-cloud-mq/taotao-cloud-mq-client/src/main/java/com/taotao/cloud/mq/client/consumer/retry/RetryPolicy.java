package com.taotao.cloud.mq.client.consumer.retry;

/**
 * 消费重试策略（对标 RocketMQ 重试策略）.
 * <p>
 * 支持两种重试间隔模式：
 * <ul>
 *   <li>{@link #STEPPED} — 阶梯间隔（无序消息默认）：10s → 30s → 1min → ... → 2h</li>
 *   <li>{@link #FIXED} — 固定间隔（顺序消息默认）：可配置固定时间</li>
 * </ul>
 */
public enum RetryPolicy {

    /**
     * 阶梯间隔（无序消息默认）.
     * <p>
     * 重试间隔随次数递增，避免频繁重试对系统造成压力。
     * 间隔时间与 RocketMQ 保持一致：
     * 第1次: 10s, 第2次: 30s, 第3次: 1min, 第4次: 2min, ...
     * 第15次: 1h, 第16次: 2h, 之后均为 2h
     */
    STEPPED,

    /**
     * 固定间隔（顺序消息默认）.
     * <p>
     * 每次重试间隔固定，由 {@code fixedRetryIntervalMillis} 配置。
     */
    FIXED;

    /**
     * 阶梯间隔时间数组（毫秒），与 RocketMQ 保持一致。
     * 索引 0 对应第 1 次重试，索引 15 对应第 16 次重试。
     */
    private static final long[] STEPPED_INTERVALS = {
            10_000L,      // 10s   - 第1次
            30_000L,      // 30s   - 第2次
            60_000L,      // 1min  - 第3次
            120_000L,     // 2min  - 第4次
            180_000L,     // 3min  - 第5次
            240_000L,     // 4min  - 第6次
            300_000L,     // 5min  - 第7次
            360_000L,     // 6min  - 第8次
            420_000L,     // 7min  - 第9次
            480_000L,     // 8min  - 第10次
            540_000L,     // 9min  - 第11次
            600_000L,     // 10min - 第12次
            1_200_000L,   // 20min - 第13次
            1_800_000L,   // 30min - 第14次
            3_600_000L,   // 1h    - 第15次
            7_200_000L    // 2h    - 第16次及以后
    };

    /**
     * 获取阶梯间隔时间（毫秒）。
     *
     * @param retryCount 当前重试次数（从1开始）
     * @return 重试间隔（毫秒）
     */
    public static long getSteppedInterval(int retryCount) {
        if (retryCount <= 0) {
            return STEPPED_INTERVALS[0];
        }
        int index = Math.min(retryCount - 1, STEPPED_INTERVALS.length - 1);
        return STEPPED_INTERVALS[index];
    }

    /**
     * 获取固定间隔时间（毫秒）。
     *
     * @param fixedIntervalMillis 固定间隔时间
     * @return 固定间隔时间
     */
    public static long getFixedInterval(long fixedIntervalMillis) {
        return Math.max(fixedIntervalMillis, 1000L); // 最小1秒
    }
}

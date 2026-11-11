package com.taotao.cloud.rpc.cluster.loadbalance;

import com.taotao.cloud.rpc.cluster.loadbalance.AbstractLoadBalance;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 最少活跃调用数负载均衡
 * 活跃数指当前正在处理的请求数，活跃数越小说明该服务提供者效率越高
 */
public class LeastActiveLoadBalance extends AbstractLoadBalance {

    public static final String NAME = "leastactive";

    // 活跃数计数器
    private final java.util.concurrent.ConcurrentMap<String, AtomicInteger> activeCounts =
            new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int length = invokers.size();

        // 记录最少活跃数
        int leastActive = -1;
        // 最少活跃数的 invoker 数量
        int leastCount = 0;
        // 最少活跃数的 invoker 索引
        int[] leastIndexes = new int[length];
        // 最少活跃数的 invoker 权重
        int[] leastWeights = new int[length];
        // 总权重
        int totalWeight = 0;
        // 第一个权重，用于判断是否所有权重相同
        int firstWeight = 0;
        // 是否所有权重相同
        boolean sameWeight = true;

        // 遍历所有 invokers
        for (int i = 0; i < length; i++) {
            Invoker<T> invoker = invokers.get(i);
            // 获取活跃数
            int active = getActiveCount(invoker, invocation);
            // 获取权重
            int weight = getWeight(invoker, invocation);

            // 发现更小的活跃数，重新开始统计
            if (leastActive == -1 || active < leastActive) {
                leastActive = active;
                leastCount = 1;
                leastIndexes[0] = i;
                leastWeights[0] = weight;
                totalWeight = weight;
                firstWeight = weight;
                sameWeight = true;
            } else if (active == leastActive) {
                // 活跃数相同，记录索引
                leastIndexes[leastCount] = i;
                leastWeights[leastCount] = weight;
                totalWeight += weight;

                // 检查权重是否相同
                if (sameWeight && weight != firstWeight) {
                    sameWeight = false;
                }

                leastCount++;
            }
        }

        // 如果只有一个最少活跃的 invoker，直接返回
        if (leastCount == 1) {
            return invokers.get(leastIndexes[0]);
        }

        // 如果权重不同，按权重随机选择
        if (!sameWeight && totalWeight > 0) {
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            for (int i = 0; i < leastCount; i++) {
                offset -= leastWeights[i];
                if (offset < 0) {
                    return invokers.get(leastIndexes[i]);
                }
            }
        }

        // 权重相同或没有权重，随机选择
        return invokers.get(leastIndexes[ThreadLocalRandom.current().nextInt(leastCount)]);
    }

    /**
     * 获取活跃数
     */
    private int getActiveCount(Invoker<?> invoker, Invocation invocation) {
        String key = invoker.getUrl().getAddress();
        AtomicInteger count = activeCounts.get(key);
        return count != null ? count.get() : 0;
    }

    /**
     * 增加活跃数
     */
    public void incrementActive(Invoker<?> invoker) {
        String key = invoker.getUrl().getAddress();
        activeCounts.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
    }

    /**
     * 减少活跃数
     */
    public void decrementActive(Invoker<?> invoker) {
        String key = invoker.getUrl().getAddress();
        AtomicInteger count = activeCounts.get(key);
        if (count != null) {
            count.decrementAndGet();
        }
    }
}

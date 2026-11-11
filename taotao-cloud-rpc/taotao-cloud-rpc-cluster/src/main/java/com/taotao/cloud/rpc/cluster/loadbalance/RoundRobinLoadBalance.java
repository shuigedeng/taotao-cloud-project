package com.taotao.cloud.rpc.cluster.loadbalance;

import com.taotao.cloud.rpc.cluster.loadbalance.AbstractLoadBalance;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡
 * 按权重轮询，考虑权重比例
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {

    public static final String NAME = "roundrobin";

    // 轮询计数器，每个方法一个
    private final ConcurrentMap<String, AtomicInteger> sequences = new ConcurrentHashMap<>();

    // 带权重的轮询状态
    private final ConcurrentMap<String, WeightedRoundRobin> weightedStates = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        String key = invocation.getServiceInterface() + "." + invocation.getMethodName();

        int totalWeight = getTotalWeight(invokers, invocation);
        boolean allSameWeight = true;

        // 检查是否所有权重相同
        for (Invoker<T> invoker : invokers) {
            int weight = getWeight(invoker, invocation);
            if (weight != totalWeight / invokers.size()) {
                allSameWeight = false;
                break;
            }
        }

        if (totalWeight > 0 && !allSameWeight) {
            // 使用带权重的轮询
            return selectByWeightedRoundRobin(invokers, invocation, key);
        } else {
            // 简单轮询
            return selectBySimpleRoundRobin(invokers, key);
        }
    }

    private <T> Invoker<T> selectBySimpleRoundRobin(List<Invoker<T>> invokers, String key) {
        AtomicInteger sequence = sequences.computeIfAbsent(key, k -> new AtomicInteger(0));
        int current = sequence.getAndIncrement();
        if (current < 0) {
            // 处理溢出
            sequence.set(0);
            current = 0;
        }
        return invokers.get(current % invokers.size());
    }

    private <T> Invoker<T> selectByWeightedRoundRobin(List<Invoker<T>> invokers,
                                                      Invocation invocation, String key) {
        // 清理过期的状态
        weightedStates.entrySet().removeIf(entry -> {
            WeightedRoundRobin state = entry.getValue();
            return System.currentTimeMillis() - state.lastUpdateTime > 10000; // 10秒过期
        });

        // 找到当前权重最高的
        int maxCurrent = 0;
        Invoker<T> selectedInvoker = null;
        WeightedRoundRobin selectedState = null;

        for (Invoker<T> invoker : invokers) {
            String invokerKey = key + "@" + invoker.getUrl().getAddress();
            int weight = getWeight(invoker, invocation);

            WeightedRoundRobin state = weightedStates.computeIfAbsent(invokerKey,
                    k -> new WeightedRoundRobin(weight));

            // 更新权重（可能动态变化）
            if (weight != state.weight) {
                state.weight = weight;
            }

            // 增加当前权重
            state.current += weight;
            state.lastUpdateTime = System.currentTimeMillis();

            // 选择当前权重最大的
            if (state.current > maxCurrent) {
                maxCurrent = state.current;
                selectedInvoker = invoker;
                selectedState = state;
            }
        }

        // 减少选中 invoker 的当前权重
        if (selectedState != null) {
            selectedState.current -= getTotalWeight(invokers, invocation);
        }

        return selectedInvoker != null ? selectedInvoker : invokers.get(0);
    }

    /**
     * 带权重的轮询状态
     */
    private static class WeightedRoundRobin {
        int weight;
        int current;
        long lastUpdateTime;

        WeightedRoundRobin(int weight) {
            this.weight = weight;
            this.current = 0;
            this.lastUpdateTime = System.currentTimeMillis();
        }
    }
}

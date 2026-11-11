package com.taotao.cloud.rpc.cluster.loadbalance;

import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.cluster.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机负载均衡
 * 按权重随机，权重大的被选中的概率更高
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    public static final String NAME = "random";

    private final Random random = new Random();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, com.taotao.cloud.rpc.common.URL url, Invocation invocation) {
        int length = invokers.size();
        int totalWeight = getTotalWeight(invokers, invocation);

        if (totalWeight > 0) {
            // 按权重随机
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);

            for (int i = 0; i < length; i++) {
                Invoker<T> invoker = invokers.get(i);
                int weight = getWeight(invoker, invocation);
                offset -= weight;
                if (offset < 0) {
                    return invoker;
                }
            }
        }

        // 所有权重相同，直接随机
        return invokers.get(ThreadLocalRandom.current().nextInt(length));
    }
}

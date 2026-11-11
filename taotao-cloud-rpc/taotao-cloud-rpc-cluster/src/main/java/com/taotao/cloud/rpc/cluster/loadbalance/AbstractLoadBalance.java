package com.taotao.cloud.rpc.cluster.loadbalance;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;

import java.util.List;

/**
 * 负载均衡抽象基类
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        if (invokers == null || invokers.isEmpty()) {
            return null;
        }
        if (invokers.size() == 1) {
            return invokers.get(0);
        }
        return doSelect(invokers, url, invocation);
    }

    /**
     * 子类实现具体的选择逻辑
     */
    protected abstract <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation);

    /**
     * 获取权重
     */
    protected int getWeight(Invoker<?> invoker, Invocation invocation) {
        URL url = invoker.getUrl();
        // 从 URL 获取权重
        int weight = Integer.parseInt(url.getParameter("weight", "100"));
        if (weight > 0) {
            long timestamp = Long.parseLong(url.getParameter("timestamp", "0"));
            if (timestamp > 0) {
                // 考虑启动预热时间
                long uptime = System.currentTimeMillis() - timestamp;
                if (uptime < 0) {
                    return 1;
                }
                int warmup = Integer.parseInt(url.getParameter("warmup", String.valueOf(10 * 60 * 1000)));
                if (uptime > 0 && uptime < warmup) {
                    weight = (int) ((float) uptime / warmup * weight);
                }
            }
        }
        return Math.max(weight, 0);
    }

    /**
     * 计算总权重
     */
    protected <T> int getTotalWeight(List<Invoker<T>> invokers, Invocation invocation) {
        int totalWeight = 0;
        for (Invoker<T> invoker : invokers) {
            totalWeight += getWeight(invoker, invocation);
        }
        return totalWeight;
    }
}

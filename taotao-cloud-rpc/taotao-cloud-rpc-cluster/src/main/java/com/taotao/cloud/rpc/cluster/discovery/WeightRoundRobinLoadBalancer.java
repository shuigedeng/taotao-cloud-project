package com.taotao.cloud.rpc.cluster.discovery;

import com.taotao.cloud.rpc.cluster.discovery.InstanceLoadBalancer;
import com.taotao.cloud.rpc.registry.ServiceInstance;

import java.util.List;

/**
 * 平滑加权轮询 (SWRR, nacos 默认算法).
 *
 * <p>每个实例维护 currentWeight += effectiveWeight，再选出 currentWeight 最大的，
 * 然后该实例的 currentWeight -= totalEffectiveWeight. 这样避免了简单轮询的 bursty 问题.
 *
 * <p>effectiveWeight = provider.weight（配置）。
 */
public class WeightRoundRobinLoadBalancer implements InstanceLoadBalancer {

    public static class State {
        double current = 0;
        ServiceInstance instance;
        State(ServiceInstance i) { this.instance = i; }
    }

    private final java.util.Map<String, State> states = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public synchronized ServiceInstance select(List<ServiceInstance> instances) {
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        // 复用 state，没建的建上 (线程安全靠 synchronized)
        for (ServiceInstance i : instances) {
            states.computeIfAbsent(i.getInstanceId(), k -> new State(i));
        }
        // 计算 totalWeight 与各 instance 的 max(current + weight)
        double total = 0;
        State pick = null;
        for (ServiceInstance i : instances) {
            State s = states.get(i.getInstanceId());
            double effective = Math.max(0.0001, i.getWeight());
            s.current += effective;
            total += effective;
            if (pick == null || s.current > pick.current) {
                pick = s;
            }
        }
        if (pick == null) {
            return instances.get(0);
        }
        pick.current -= total;
        return pick.instance;
    }

    @Override public String getName() { return "weighted-round-robin"; }
}

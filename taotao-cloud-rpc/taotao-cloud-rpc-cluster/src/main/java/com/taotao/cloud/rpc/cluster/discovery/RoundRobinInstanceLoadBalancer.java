package com.taotao.cloud.rpc.cluster.discovery;

import com.taotao.cloud.rpc.cluster.discovery.InstanceLoadBalancer;
import com.taotao.cloud.rpc.registry.ServiceInstance;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 简单轮询 (RR) — 适用于不区分权重的场景.
 */
public class RoundRobinInstanceLoadBalancer implements InstanceLoadBalancer {
    private final AtomicLong idx = new AtomicLong();
    @Override
    public ServiceInstance select(List<ServiceInstance> instances) {
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        long i = Math.floorMod(idx.getAndIncrement(), instances.size());
        return instances.get((int) i);
    }
    @Override public String getName() { return "round-robin"; }
}

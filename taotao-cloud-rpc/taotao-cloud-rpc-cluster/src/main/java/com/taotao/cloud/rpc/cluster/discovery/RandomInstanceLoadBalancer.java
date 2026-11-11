package com.taotao.cloud.rpc.cluster.discovery;

import com.taotao.cloud.rpc.registry.ServiceInstance;

import java.util.List;

/**
 * 随机选一个.
 */
public class RandomInstanceLoadBalancer implements InstanceLoadBalancer {
    @Override
    public ServiceInstance select(List<ServiceInstance> instances) {
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        return instances.get((int) (Math.random() * instances.size()));
    }
    @Override public String getName() { return "random"; }
}

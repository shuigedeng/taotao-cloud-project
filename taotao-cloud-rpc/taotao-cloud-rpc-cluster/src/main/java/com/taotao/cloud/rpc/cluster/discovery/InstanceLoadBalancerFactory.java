package com.taotao.cloud.rpc.cluster.discovery;

import com.taotao.cloud.rpc.cluster.discovery.InstanceLoadBalancer;
import com.taotao.cloud.rpc.cluster.discovery.RandomInstanceLoadBalancer;
import com.taotao.cloud.rpc.cluster.discovery.RoundRobinInstanceLoadBalancer;
import com.taotao.cloud.rpc.cluster.discovery.WeightRoundRobinLoadBalancer;

/**
 * 负载均衡器工厂 — 按名字创建，缺省 weighted-round-robin (nacos 默认).
 */
public final class InstanceLoadBalancerFactory {
    private InstanceLoadBalancerFactory() {}
    public static InstanceLoadBalancer create(String name) {
        if (name == null) {
            return new WeightRoundRobinLoadBalancer();
        }
        switch (name.toLowerCase()) {
            case "random":     return new RandomInstanceLoadBalancer();
            case "round-robin":
            case "rr":         return new RoundRobinInstanceLoadBalancer();
            case "weighted-round-robin":
            case "weighted_rr":
            case "wrr":
            default:           return new WeightRoundRobinLoadBalancer();
        }
    }
}

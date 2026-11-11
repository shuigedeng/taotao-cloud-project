package com.taotao.cloud.rpc.cluster.discovery;

import com.taotao.cloud.rpc.cluster.discovery.RandomInstanceLoadBalancer;
import com.taotao.cloud.rpc.cluster.discovery.RoundRobinInstanceLoadBalancer;
import com.taotao.cloud.rpc.cluster.discovery.WeightRoundRobinLoadBalancer;
import com.taotao.cloud.rpc.registry.ServiceInstance;

import java.util.List;

/**
 * 实例选择器 (轻量级 — 直接选 ServiceInstance，不再走 Invoker 抽象).
 *
 * <p>三种内置实现:
 * <ul>
 *   <li>{@link RandomInstanceLoadBalancer}   — 随机</li>
 *   <li>{@link RoundRobinInstanceLoadBalancer} — 轮询 (单线程 / 单消费者)</li>
 *   <li>{@link WeightRoundRobinLoadBalancer} — 加权轮询 (nacos 默认用的权重)</li>
 * </ul>
 *
 * <p>与原有 Dubbo-style {@link com.taotao.cloud.rpc.loadbalance.LoadBalance} 区别:
 * 原 LoadBalance 接受 Invoker 列表 (用于服务治理包装)，本接口直接选 ServiceInstance
 * 用于 ttc-rpc Client 连接池直接路由到目标 endpoint.
 */
public interface InstanceLoadBalancer {

    ServiceInstance select(List<ServiceInstance> instances);

    String getName();
}

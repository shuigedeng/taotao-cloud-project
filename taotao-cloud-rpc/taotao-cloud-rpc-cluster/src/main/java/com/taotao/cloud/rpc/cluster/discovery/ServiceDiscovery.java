package com.taotao.cloud.rpc.cluster.discovery;

import com.taotao.cloud.rpc.cluster.discovery.InstanceLoadBalancer;
import com.taotao.cloud.rpc.cluster.discovery.InstanceLoadBalancerFactory;
import com.taotao.cloud.rpc.registry.RpcRegistry;
import com.taotao.cloud.rpc.registry.ServiceInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 服务发现 (FEATURE) — 封装 RpcRegistry 订阅 + 本地缓存 + 集成 LoadBalancer.
 *
 * <p>使用流程:
 * <pre>
 *     ServiceDiscovery discovery = new ServiceDiscovery(registry, "weighted-round-robin");
 *     discovery.subscribe("HelloService");
 *     ServiceInstance target = discovery.select("HelloService");
 *     // target.getIp() / getPort() → RpcClient.connect & invoke
 * </pre>
 *
 * <p>每次 {@link RpcRegistry.RegistryEvent} push 触发本地快照更新 (ConcurrentHashMap).
 *
 * <p>线程安全: 缓存写和读都在 ConcurrentHashMap 上; LoadBalancer 内部自己维护自己的 state.
 */
public class ServiceDiscovery {

    private static final Logger log = LogManager.getLogger(ServiceDiscovery.class);

    private final RpcRegistry registry;
    private final InstanceLoadBalancer loadBalancer;
    /** serviceName -> [instanceId -> ServiceInstance] */
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ServiceInstance>> cache = new ConcurrentHashMap<>();
    /** serviceName -> [RpcRegistry.Subscription] */
    private final ConcurrentHashMap<String, RpcRegistry.Subscription> subscriptions = new ConcurrentHashMap<>();
    /** 心跳/变更触发次数 (调试用) */
    private final AtomicLong changeCount = new AtomicLong();

    public ServiceDiscovery(RpcRegistry registry) {
        this(registry, "weighted-round-robin");
    }

    public ServiceDiscovery(RpcRegistry registry, String balancerName) {
        this.registry = registry;
        this.loadBalancer = InstanceLoadBalancerFactory.create(balancerName);
    }

    /**
     * 订阅服务变更 (首次调用会立刻拉一次).
     */
    public synchronized void subscribe(String serviceName) {
        if (subscriptions.containsKey(serviceName)) {
            return;
        }
        ConcurrentHashMap<String, ServiceInstance> bucket = cache.computeIfAbsent(
                serviceName, k -> new ConcurrentHashMap<>());

        RpcRegistry.Subscription sub = registry.subscribe(serviceName, event -> {
            changeCount.incrementAndGet();
            switch (event.getType()) {
                case REGISTER:
                case DEREGISTER:
                    if (event.getInstance() != null) {
                        ServiceInstance ins = event.getInstance();
                        if (event.getType() == RpcRegistry.Type.REGISTER) {
                            bucket.put(ins.getInstanceId(), ins);
                            log.info("discovery[{}] add {}", serviceName, ins);
                        } else {
                            bucket.remove(ins.getInstanceId());
                            log.info("discovery[{}] remove {}", serviceName, ins);
                        }
                    }
                    break;
                case FULL_SNAPSHOT:
                    bucket.clear();
                    if (event.getInstances() != null) {
                        for (ServiceInstance ins : event.getInstances()) {
                            bucket.put(ins.getInstanceId(), ins);
                        }
                        log.info("discovery[{}] snapshot replaced with {} instances",
                                serviceName, bucket.size());
                    }
                    break;
            }
        });
        subscriptions.put(serviceName, sub);
    }

    /**
     * 取消订阅 (通常测试 teardown).
     */
    public void unsubscribe(String serviceName) {
        RpcRegistry.Subscription sub = subscriptions.remove(serviceName);
        if (sub != null) {
            registry.unsubscribe(serviceName, sub);
        }
        cache.remove(serviceName);
    }

    /**
     * 当前可用实例列表 (按 LoadBalancer 选用).
     */
    public List<ServiceInstance> instances(String serviceName) {
        ConcurrentHashMap<String, ServiceInstance> bucket = cache.get(serviceName);
        if (bucket == null || bucket.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(bucket.values());
    }

    /**
     * 用 LoadBalancer 选一个 (主入口).
     */
    public ServiceInstance select(String serviceName) {
        return loadBalancer.select(instances(serviceName));
    }

    public InstanceLoadBalancer getLoadBalancer() { return loadBalancer; }
    public long getChangeCount() { return changeCount.get(); }
    public void close() {
        for (String s : new ArrayList<>(subscriptions.keySet())) unsubscribe(s);
    }
}

package com.taotao.cloud.rpc.registry;

import com.taotao.cloud.rpc.registry.InMemoryRpcRegistry;
import com.taotao.cloud.rpc.registry.ServiceInstance;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 服务注册中心抽象 (FEATURE — ttc-rpc + ttc-config 联合).
 *
 * <p>ttc-rpc 通过此接口对接任意的"注册中心"实现，对应 nacos Naming 的能力集：
 * <ul>
 *   <li>register: Server 启动时上报自己的 (serviceName, ip, port, weight, ephemeral)</li>
 *   <li>deregister: Server 关闭时主动注销（ephemeral=true 时可省略，依赖心跳剔除）</li>
 *   <li>heartbeat: 周期性 renew；超时未续约 → 注册中心自动剔除 (nacos 默认 15s × 5 = 75s 心跳窗口)</li>
 *   <li>discover: 同步拉取服务实例列表 (用于客户端启动初始化)</li>
 *   <li>subscribe: 监听变更 (Naming push 推送)，回调本地缓存</li>
 * </ul>
 *
 * <p>不健康实例由 registry 实现内部标 healthy=false，下游 ServiceDiscovery 过滤。
 *
 * <p>实现:
 * <ul>
 *   <li>{@link InMemoryRpcRegistry}  — 测试用，单进程内存表</li>
 *   <li>{@code ZkNamingRpcRegistry}   — 生产用，适配 ttc-config ZNamingService (nacos 等价)</li>
 * </ul>
 */
public interface RpcRegistry {

    /**
     * 注册实例. 必须确保 instanceId 唯一.
     *
     * @return 注册成功返回 true；instanceId 重复时返回 false（注册中心允许覆盖）
     */
    boolean register(ServiceInstance instance);

    /**
     * 注销实例（主动反注册）.
     */
    void deregister(String serviceName, String instanceId);

    /**
     * 心跳续约.
     *
     * @return 注册中心返回 "next heartbeat interval" (秒)；失败或实例不存在返回 -1
     */
    long heartbeat(String serviceName, String instanceId);

    /**
     * 拉取所有实例（含不健康实例由 caller 过滤）.
     */
    List<ServiceInstance> discover(String serviceName);

    /**
     * 订阅服务变更 (注册 / 注销 / 健康切换).
     *
     * <p>回调会在 registry 工作线程上触发，注意回调里不要做重操作；
     * 通常应在 listener 内只更新本地缓存.
     *
     * @return 订阅句柄，用于 unsubscribe
     */
    Subscription subscribe(String serviceName, Consumer<RegistryEvent> listener);

    /**
     * 取消订阅.
     */
    void unsubscribe(String serviceName, Subscription subscription);

    /**
     * 关闭 (释放资源；通常是测试 teardown 时调用).
     */
    void close();

    /** 注册中心事件类型. */
    enum Type {
        /** 新增/更新实例 */
        REGISTER,
        /** 注销 */
        DEREGISTER,
        /** 实例全量替换 (用于 snapshot 同步) */
        FULL_SNAPSHOT
    }

    /** 事件. */
    class RegistryEvent {
        private final Type type;
        private final String serviceName;
        private final ServiceInstance instance;     // 可能为 null (snapshot)
        private final List<ServiceInstance> instances;  // FULL_SNAPSHOT 时非 null

        public RegistryEvent(Type type, String serviceName, ServiceInstance instance) {
            this.type = type;
            this.serviceName = serviceName;
            this.instance = instance;
            this.instances = null;
        }

        public RegistryEvent(Type type, String serviceName, List<ServiceInstance> instances) {
            this.type = type;
            this.serviceName = serviceName;
            this.instance = null;
            this.instances = instances;
        }

        public Type getType() { return type; }
        public String getServiceName() { return serviceName; }
        public ServiceInstance getInstance() { return instance; }
        public List<ServiceInstance> getInstances() { return instances; }
        public Set<ServiceInstance> getInstanceSnapshot() {
            return instances == null ? null : new java.util.LinkedHashSet<>(instances);
        }
    }

    /** 订阅句柄 (用于 unsubscribe 取消) */
    interface Subscription {
        String serviceName();
        Consumer<RegistryEvent> listener();
    }
}

package com.taotao.cloud.rpc.registry;

import java.util.Objects;

/**
 * ServiceInstance (FEATURE) — ttc-rpc 服务注册中心的实例模型.
 *
 * <p>对应 nacos/naming 服务实例（ip, port, weight, healthy, enabled, metadata），
 * 设计为 POJO（不依赖外部 Naming 协议），便于不同实现（InMemory / ZkNaming / Eureka）
 * 之间相互转换.
 */
public class ServiceInstance {

    /** 服务名（业务唯一，全局唯一，例如 HelloService v1） */
    private String serviceName;

    /** 实例唯一 ID（一次启动生成一次；通常 ip:port#seq 或 UUID） */
    private String instanceId;

    /** 实例 IP（自动获取本机 IP 也可手动指定） */
    private String ip;

    /** 实例端口 */
    private int port;

    /** 权重（1.0 默认；用于 WeightedRoundRobin） */
    private double weight = 1.0;

    /** 是否启用（false 时客户端不应调用） */
    private boolean enabled = true;

    /** 是否临时实例（true 时 Server 异常停掉就自动剔除，无需显式 deregister） */
    private boolean ephemeral = true;

    /** 集群名 / 分组（默认 DEFAULT） */
    private String cluster = "DEFAULT";

    /** 自定义 metadata（version, weight_group, region 等） */
    private java.util.Map<String, String> metadata;

    /** 服务暴露的接口（CN 风格："com.foo.BarService"，便于客户端按名查） */
    private String[] exposedInterfaces;

    public ServiceInstance() {}

    public ServiceInstance(String serviceName, String ip, int port) {
        this.serviceName = serviceName;
        this.ip = ip;
        this.port = port;
        this.instanceId = ip + ":" + port;
    }

    public String endpoint() {
        return ip + ":" + port;
    }

    /** 计算 instanceId (ip:port 简单形式). */
    public static String endpointOf(String ip, int port) {
        return ip + ":" + port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceInstance)) {
            return false;
        }
        ServiceInstance that = (ServiceInstance) o;
        return port == that.port
                && Objects.equals(serviceName, that.serviceName)
                && Objects.equals(ip, that.ip)
                && Objects.equals(instanceId, that.instanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, ip, port, instanceId);
    }

    @Override
    public String toString() {
        return "ServiceInstance{" + serviceName + "@" + endpoint() + " id=" + instanceId
                + (weight != 1.0 ? " weight=" + weight : "")
                + (enabled ? "" : " disabled")
                + (cluster != null ? " cluster=" + cluster : "") + "}";
    }

    // getters/setters
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isEphemeral() { return ephemeral; }
    public void setEphemeral(boolean ephemeral) { this.ephemeral = ephemeral; }
    public String getCluster() { return cluster; }
    public void setCluster(String cluster) { this.cluster = cluster; }
    public java.util.Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(java.util.Map<String, String> metadata) { this.metadata = metadata; }
    public String[] getExposedInterfaces() { return exposedInterfaces; }
    public void setExposedInterfaces(String[] exposedInterfaces) { this.exposedInterfaces = exposedInterfaces; }
}

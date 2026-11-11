package com.taotao.cloud.rpc.dashboard.admin.model;

import java.util.Map;

/**
 * 服务 DTO
 */
public class ServiceVO {
    private String serviceKey;
    private String serviceName;
    private String version;
    private String group;
    private int providerCount;
    private int consumerCount;
    private Map<String, Object> metrics;

    public String getServiceKey() { return serviceKey; }
    public void setServiceKey(String serviceKey) { this.serviceKey = serviceKey; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }
    public int getProviderCount() { return providerCount; }
    public void setProviderCount(int providerCount) { this.providerCount = providerCount; }
    public int getConsumerCount() { return consumerCount; }
    public void setConsumerCount(int consumerCount) { this.consumerCount = consumerCount; }
    public Map<String, Object> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
}

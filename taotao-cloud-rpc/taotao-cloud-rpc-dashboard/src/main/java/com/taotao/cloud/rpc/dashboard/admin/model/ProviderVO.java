package com.taotao.cloud.rpc.dashboard.admin.model;

import java.util.Map;

/**
 * Provider 实例 DTO
 */
public class ProviderVO {
    private String id;
    private String service;
    private String address;
    private String host;
    private int port;
    private int weight;
    private String version;
    private String group;
    private boolean healthy;
    private Map<String, Object> metrics;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }
    public boolean isHealthy() { return healthy; }
    public void setHealthy(boolean healthy) { this.healthy = healthy; }
    public Map<String, Object> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
}

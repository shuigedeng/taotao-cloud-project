package com.taotao.cloud.lock.kylin.configuration.zookeeper;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * kylin-Lock-zookeeper 配置
 *
 * @author wangjinkui
 */
@ConfigurationProperties(prefix = "kylin.lock.zookeeper")
public class ZookeeperLockProperties {
    /**
     * zk的server地址，多个server之间使用英文逗号分隔开
     */
    private String zkServers;

    /**
     * 会话超时时间，默认60000
     */
    private int sessionTimeout = 60000;

    /**
     * 连接超时时间，默认15000
     */
    private int connectionTimeout = 15000;

    /**
     * 初始sleep时间，毫秒
     * 初始的sleep时间，用于计算之后的每次重试的sleep时间，
     * 计算公式：当前sleep时间=baseSleepTimeMs*Math.max(1, random.nextInt(1<<(retryCount+1)))
     */
    private int baseSleepTimeMs = 5000;

    /**
     * 最大失败重试次数
     */
    private int maxRetries = 3;

    /**
     *命名空间
     */
    private String namespace = "curator/kylin/lock";

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public String toString() {
        return "ZookeeperLockProperties{" +
                "zkServers='" + zkServers + '\'' +
                ", sessionTimeout=" + sessionTimeout +
                ", connectionTimeout=" + connectionTimeout +
                ", baseSleepTimeMs=" + baseSleepTimeMs +
                ", maxRetries=" + maxRetries +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}

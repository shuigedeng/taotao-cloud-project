package com.taotao.cloud.mq.consistency.ttc.broker;

/**
 * Broker 配置（对标 RocketMQ BrokerConfig）.
 * <p>
 * 字段命名与 RocketMQ 保持一致, 缺省值由系统环境变量 / 属性注入.
 * <p>
 * 该类被同包和子包 (例如 processor 包) 共享, 故设为 public.
 */
public class BrokerConfig {

    public static final long MASTER_ID = 0L;

    private String namesrvAddr = System.getenv("NAMESRV_ADDR");
    private String brokerName = System.getProperty("brokerName", "DEFAULT_BROKER");
    private String brokerClusterName = System.getProperty("brokerClusterName", "DEFAULT_CLUSTER");

    /**
     * 向 NameServer 注册时**对外公布**的 Broker 地址主机名/IP（对标 RocketMQ 的 brokerIP1）。
     * <p>
     * 这不是"本进程 bind 在哪"，而是"客户端该连哪"：注册上报后 NameServer 路由表里存的就是
     * 它。此前上报值写死 {@code localhost:端口}，本机客户端看不出问题，但任何远程客户端按路由表
     * 去连的都是**它自己机器上的** 10911 —— 单机跑得通、集群里必然连不上。
     * <p>
     * 默认值仍取 localhost，以保持本地开发的既有行为；容器/k8s 部署必须显式覆盖。
     */
    private String brokerIp1 = System.getProperty("brokerIp1", "localhost");

    /**
     * 绑定的监听端口。与 {@link #brokerIp1} 合成上报地址，二者独立：监听在容器内网
     * 10911、公布给集群外客户端的可能是宿主机 IP + NodePort，因此公布地址不能只靠 IP 推断端口。
     */
    private int listenPort;

    /**
     * Broker ID: 0 = Master (默认), 1+ = Slave.
     * <p>
     * Slave 启动时会从 {@link #brokerMasterAddr} 拉取元数据 + 同步 CommitLog.
     */
    private long brokerId = MASTER_ID;

    /**
     * Master Broker 地址 (仅 Slave 使用, 格式 ip:port).
     * <p>
     * SlaveSynchronize 会向这个地址发 GET_ALL_* / QUERY_DATA_VERSION 请求.
     */
    private volatile String brokerMasterAddr;

    private int sendMessageThreadPoolNums = 16;
    private int pullMessageThreadPoolNums = 16;
    private int adminBrokerThreadPoolNums = 4;

    /** Slave 周期性同步 Master 元数据间隔 (毫秒). */
    private long syncAllIntervalMillis = 30_000L;

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerClusterName() {
        return brokerClusterName;
    }

    public void setBrokerClusterName(String brokerClusterName) {
        this.brokerClusterName = brokerClusterName;
    }

    public String getBrokerIp1() {
        return brokerIp1;
    }

    public void setBrokerIp1(String brokerIp1) {
        this.brokerIp1 = brokerIp1;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    /** 向 NameServer 上报的客户端可达地址（{@code host:port}）。 */
    public String getAdvertisedAddr() {
        return brokerIp1 + ":" + listenPort;
    }

    /**
     * 是否是 Master Broker (brokerId == 0).
     */
    public boolean isMaster() {
        return brokerId == MASTER_ID;
    }

    public long getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(long brokerId) {
        this.brokerId = brokerId;
    }

    public String getBrokerMasterAddr() {
        return brokerMasterAddr;
    }

    public void setBrokerMasterAddr(String brokerMasterAddr) {
        this.brokerMasterAddr = brokerMasterAddr;
    }

    public long getSyncAllIntervalMillis() {
        return syncAllIntervalMillis;
    }

    public void setSyncAllIntervalMillis(long syncAllIntervalMillis) {
        this.syncAllIntervalMillis = syncAllIntervalMillis;
    }

    public int getSendMessageThreadPoolNums() {
        return sendMessageThreadPoolNums;
    }

    public void setSendMessageThreadPoolNums(int sendMessageThreadPoolNums) {
        this.sendMessageThreadPoolNums = sendMessageThreadPoolNums;
    }

    public int getPullMessageThreadPoolNums() {
        return pullMessageThreadPoolNums;
    }

    public void setPullMessageThreadPoolNums(int pullMessageThreadPoolNums) {
        this.pullMessageThreadPoolNums = pullMessageThreadPoolNums;
    }

    public int getAdminBrokerThreadPoolNums() {
        return adminBrokerThreadPoolNums;
    }

    public void setAdminBrokerThreadPoolNums(int adminBrokerThreadPoolNums) {
        this.adminBrokerThreadPoolNums = adminBrokerThreadPoolNums;
    }
}

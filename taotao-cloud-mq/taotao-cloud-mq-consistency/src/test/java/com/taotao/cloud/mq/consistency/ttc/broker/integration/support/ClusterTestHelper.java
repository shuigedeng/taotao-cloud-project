package com.taotao.cloud.mq.consistency.ttc.broker.integration.support;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerConfig;
import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec;
import com.taotao.cloud.mq.consistency.ttc.nameserver.NameServerController;
import com.taotao.cloud.mq.consistency.ttc.nameserver.NamesrvConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 测试辅助工具 — 管理 NameServer + Broker 集群的生命周期, 提供 Topic 创建 / 消息发送 / 路由拉取等快捷方法。
 * <p>
 * 所有测试使用此类获取短生命周期的测试集群, 避免端口冲突。
 */
public class ClusterTestHelper {

    private static final Logger log = LoggerFactory.getLogger(ClusterTestHelper.class);

    private final String testId;
    private final String storeRoot;
    private final int namesrvPort;
    private final int brokerPort;

    private NameServerController nameServer;
    private BrokerController broker;

    public ClusterTestHelper() {
        this.testId = UUID.randomUUID().toString().substring(0, 8);
        this.storeRoot = System.getProperty("user.home") + File.separator
                + "zmq-test-" + testId;
        this.namesrvPort = 19000 + (testId.hashCode() & 0x7FFF) % 500;
        this.brokerPort = this.namesrvPort + 1000;
    }

    /**
     * 启动 NameServer + Broker, 等心跳注册完成。
     *
     * @param brokerName       broker 名称
     * @param brokerClusterName 集群名称
     */
    public void startCluster(String brokerName, String brokerClusterName) throws Exception {
        // 创建存储目录
        new File(storeRoot).mkdirs();

        // 1) 启动 NameServer
        NamesrvConfig nsCfg = new NamesrvConfig();
        nsCfg.setKvConfigPath(storeRoot + File.separator + "kvConfig.json");
        NettyServerConfig nsNetty = new NettyServerConfig();
        nsNetty.setListenPort(namesrvPort);
        nameServer = new NameServerController(nsCfg, nsNetty);
        nameServer.initialize();
        nameServer.start();

        // 2) 启动 Broker
        BrokerConfig bc = new BrokerConfig();
        bc.setNamesrvAddr("localhost:" + namesrvPort);
        bc.setBrokerName(brokerName);
        bc.setBrokerClusterName(brokerClusterName);
        MessageStoreConfig msc = new MessageStoreConfig();
        msc.setStorePathRootDir(storeRoot + File.separator + "broker");
        msc.setStorePathCommitLog(storeRoot + File.separator + "broker" + File.separator + "commitlog");
        msc.setMappedFileSizeCommitLog(1024 * 1024); // 1MB, 节省磁盘
        NettyServerConfig brokerNetty = new NettyServerConfig();
        brokerNetty.setListenPort(brokerPort);
        broker = new BrokerController(bc, msc, brokerNetty);
        broker.initialize();
        broker.start();

        // 3) 等待心跳注册
        waitForRegistration(5000);
        log.info("Cluster started: nsPort={} brokerPort={}", namesrvPort, brokerPort);
    }

    /**
     * 启动简版集群, broker 使用默认名称。
     */
    public void startCluster() throws Exception {
        startCluster("TestBroker_" + testId, "TestCluster_" + testId);
    }

    /**
     * 等待 broker 在 nameserver 注册完成。
     */
    public void waitForRegistration(long timeoutMillis) throws Exception {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < deadline) {
            if (nameServer.getRouteInfoManager().getBrokerAddrTable()
                    .containsKey("TestBroker_" + testId)
                    || !nameServer.getRouteInfoManager().getBrokerAddrTable().isEmpty()) {
                return;
            }
            Thread.sleep(50);
        }
    }

    /**
     * 关闭集群, 清理文件。
     */
    public void shutdownCluster() {
        if (broker != null) {
            try { broker.shutdown(); } catch (Exception ignore) {}
        }
        if (nameServer != null) {
            try { nameServer.shutdown(); } catch (Exception ignore) {}
        }
        // 清理临时目录
        deleteDir(new File(storeRoot));
    }

    /**
     * 仅重启 broker (不重启 nameserver), 用于测试 broker 故障恢复。
     */
    public void restartBroker() throws Exception {
        broker.shutdown();
        Thread.sleep(300);

        // 重新构建 BrokerConfig
        BrokerConfig bc = new BrokerConfig();
        bc.setNamesrvAddr("localhost:" + namesrvPort);
        bc.setBrokerName("TestBroker_" + testId);
        bc.setBrokerClusterName("TestCluster_" + testId);
        MessageStoreConfig msc = new MessageStoreConfig();
        msc.setStorePathRootDir(storeRoot + File.separator + "broker");
        msc.setStorePathCommitLog(storeRoot + File.separator + "broker" + File.separator + "commitlog");
        msc.setMappedFileSizeCommitLog(1024 * 1024);
        NettyServerConfig brokerNetty = new NettyServerConfig();
        brokerNetty.setListenPort(brokerPort);
        broker = new BrokerController(bc, msc, brokerNetty);
        broker.initialize();
        broker.start();
        waitForRegistration(3000);
    }

    /**
     * 仅启动 broker (broker 之前未启动或已关闭).
     */
    public void startBroker() throws Exception {
        BrokerConfig bc = new BrokerConfig();
        bc.setNamesrvAddr("localhost:" + namesrvPort);
        bc.setBrokerName("TestBroker_" + testId);
        bc.setBrokerClusterName("TestCluster_" + testId);
        MessageStoreConfig msc = new MessageStoreConfig();
        msc.setStorePathRootDir(storeRoot + File.separator + "broker");
        msc.setStorePathCommitLog(storeRoot + File.separator + "broker" + File.separator + "commitlog");
        msc.setMappedFileSizeCommitLog(1024 * 1024);
        NettyServerConfig brokerNetty = new NettyServerConfig();
        brokerNetty.setListenPort(brokerPort);
        broker = new BrokerController(bc, msc, brokerNetty);
        broker.initialize();
        broker.start();
        waitForRegistration(3000);
    }

    /**
     * 直接向 NameServer 发 UPDATE_AND_CREATE_TOPIC 请求。
     */
    public void createTopic(String topic) throws Exception {
        createTopic(topic, 4, 4);
    }

    public void createTopic(String topic, int readQ, int writeQ) throws Exception {
        NettyClientConfig cfg = new NettyClientConfig();
        NettyRemotingClient client = new NettyRemotingClient(cfg);
        client.start();
        try {
            io.netty.channel.Channel ch = client.getOrCreateChannel("localhost:" + namesrvPort);
            RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
            req.addExtField("topic", topic);
            req.addExtField("readQueueNums", String.valueOf(readQ));
            req.addExtField("writeQueueNums", String.valueOf(writeQ));
            RemotingCommand resp = client.invokeSync(ch, req, 3000);
            if (resp.getCode() != 0) {
                throw new RuntimeException("create topic failed: code=" + resp.getCode() + " remark=" + resp.getRemark());
            }
        } finally {
            client.shutdown();
        }
    }

    /**
     * 通过 NettyRemotingClient 向 NameServer 拉取 Topic 路由。
     */
    public com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData fetchRoute(String topic) throws Exception {
        NettyClientConfig cfg = new NettyClientConfig();
        NettyRemotingClient client = new NettyRemotingClient(cfg);
        client.start();
        try {
            io.netty.channel.Channel ch = client.getOrCreateChannel("localhost:" + namesrvPort);
            RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.GET_ROUTE_BY_TOPIC);
            req.addExtField("topic", topic);
            RemotingCommand resp = client.invokeSync(ch, req, 3000);
            if (resp.getCode() != 0) {
                return null;
            }
            return JsonCodec.decode(resp.getBody(), com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData.class);
        } finally {
            client.shutdown();
        }
    }

    public String getNamesrvAddr() {
        return "localhost:" + namesrvPort;
    }

    public String getBrokerAddr() {
        return "localhost:" + brokerPort;
    }

    public int getNamesrvPort() {
        return namesrvPort;
    }

    public int getBrokerPort() {
        return brokerPort;
    }

    public NameServerController getNameServer() {
        return nameServer;
    }

    public BrokerController getBroker() {
        return broker;
    }

    private void deleteDir(File dir) {
        if (dir == null || !dir.exists()) { return; }

        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteDir(f);
                }
            }
        }
        dir.delete();
    }
}

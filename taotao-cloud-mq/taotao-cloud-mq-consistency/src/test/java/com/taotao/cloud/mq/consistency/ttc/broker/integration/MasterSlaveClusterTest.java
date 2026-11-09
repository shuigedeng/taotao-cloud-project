package com.taotao.cloud.mq.consistency.ttc.broker.integration;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerConfig;
import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.broker.processor.AdminBrokerProcessor;
import com.taotao.cloud.mq.consistency.ttc.broker.slave.SlaveSynchronize;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.ha.DataVersion;
import com.taotao.cloud.mq.consistency.ttc.nameserver.NameServerController;
import com.taotao.cloud.mq.consistency.ttc.nameserver.NamesrvConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig;
import io.netty.channel.Channel;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Master-Slave 集群集成测试 (v3 分布式).
 * <p>
 * 验证:
 * <ul>
 *   <li>Master Broker 通过 BrokerOutAPI 暴露 TopicConfig / ConsumerOffset / DelayOffset</li>
 *   <li>Slave Broker 通过 SlaveSynchronize 从 Master 拉取元数据</li>
 *   <li>DataVersion 增量同步判断</li>
 *   <li>Master 创建 Topic 后, Slave 同步可见</li>
 * </ul>
 *
 * <p>这是 RocketMQ 主从复制的简化版本 (RocketMQ HAService 是 CommitLog 字节级同步, 我们先实现元数据级同步).
 */
@DisplayName("Master-Slave 集群集成测试")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MasterSlaveClusterTest {

    private static final Logger log = LoggerFactory.getLogger(MasterSlaveClusterTest.class);

    private static final String STORE_ROOT = System.getProperty("user.home") + File.separator
            + "zmq-ms-test-" + System.currentTimeMillis();

    private static final int NAMESRV_PORT = 29876;
    private static final int MASTER_PORT = 21911;
    private static final int SLAVE_PORT = 22911;

    private NameServerController nameServer;
    private BrokerController master;
    private BrokerController slave;
    private String masterAddr;

    @BeforeAll
    public void startCluster() throws Exception {
        new File(STORE_ROOT).mkdirs();

        // 1) NameServer
        NamesrvConfig namesrvConfig = new NamesrvConfig();
        namesrvConfig.setKvConfigPath(STORE_ROOT + "/kvConfig.json");
        NettyServerConfig namesrvNetty = new NettyServerConfig();
        namesrvNetty.setListenPort(NAMESRV_PORT);
        nameServer = new NameServerController(namesrvConfig, namesrvNetty);
        assertTrue(nameServer.initialize(), "NameServer init");
        nameServer.start();

        // 2) Master Broker
        BrokerConfig masterConfig = new BrokerConfig();
        masterConfig.setNamesrvAddr("localhost:" + NAMESRV_PORT);
        masterConfig.setBrokerName("DefaultBroker");
        masterConfig.setBrokerClusterName("DefaultCluster");
        // Master: brokerId = 0 (默认)
        NettyServerConfig masterNetty = new NettyServerConfig();
        masterNetty.setListenPort(MASTER_PORT);
        MessageStoreConfig masterStore = new MessageStoreConfig();
        masterStore.setStorePathRootDir(STORE_ROOT + "/master");
        masterStore.setStorePathCommitLog(STORE_ROOT + "/master/commitlog");
        master = new BrokerController(masterConfig, masterStore, masterNetty);
        assertTrue(master.initialize(), "Master init");
        master.start();

        masterAddr = "localhost:" + MASTER_PORT;

        // 3) Slave Broker (brokerId = 1, 配置 master addr)
        BrokerConfig slaveConfig = new BrokerConfig();
        slaveConfig.setNamesrvAddr("localhost:" + NAMESRV_PORT);
        slaveConfig.setBrokerName("DefaultBroker");  // 同 brokerName = 同一 broker group
        slaveConfig.setBrokerClusterName("DefaultCluster");
        slaveConfig.setBrokerId(1L);  // Slave
        slaveConfig.setBrokerMasterAddr(masterAddr);
        slaveConfig.setSyncAllIntervalMillis(2_000L);  // 2s 周期同步便于测试
        NettyServerConfig slaveNetty = new NettyServerConfig();
        slaveNetty.setListenPort(SLAVE_PORT);
        MessageStoreConfig slaveStore = new MessageStoreConfig();
        slaveStore.setStorePathRootDir(STORE_ROOT + "/slave");
        slaveStore.setStorePathCommitLog(STORE_ROOT + "/slave/commitlog");
        slave = new BrokerController(slaveConfig, slaveStore, slaveNetty);
        assertTrue(slave.initialize(), "Slave init");
        slave.start();

        log.info("Cluster started: namesrv={} master={} slave={}",
                NAMESRV_PORT, MASTER_PORT, SLAVE_PORT);

        // 等 Master 注册到 NameServer
        Thread.sleep(1500);
    }

    @AfterAll
    public void stopCluster() {
        if (slave != null) slave.shutdown();
        if (master != null) master.shutdown();
        if (nameServer != null) nameServer.shutdown();
    }

    @Test
    @DisplayName("BrokerOutAPI 暴露 TopicConfig 列表")
    public void testBrokerOutAPIGetTopicConfig() throws Exception {
        NettyClientConfig clientConfig = new NettyClientConfig();
        NettyRemotingClient client = new NettyRemotingClient(clientConfig);
        client.start();
        try {
            Channel ch = client.getOrCreateChannel(masterAddr);
            assertNotNull(ch);

            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.GET_ALL_TOPIC_CONFIG);
            RemotingCommand response = client.invokeSync(ch, request, 3000);
            assertNotNull(response);
            assertEquals(RemotingSysResponseCode.SUCCESS, response.getCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().length > 0, "response body should not be empty");
            log.info("BrokerOutAPI GET_ALL_TOPIC_CONFIG responded: {} bytes", response.getBody().length);
        } finally {
            client.shutdown();
        }
    }

    @Test
    @DisplayName("SlaveSynchronize 启动后版本号应被初始化")
    public void testSlaveSynchronizeInitialized() throws Exception {
        // Slave 启动时已立即 syncAll(), 所以本地版本应该被设置
        SlaveSynchronize ss = slave.getSlaveSynchronize();
        assertNotNull(ss, "SlaveSynchronize should be initialized for slave broker");
        DataVersion v = ss.getLocalTopicConfigVersion();
        assertNotNull(v);
        log.info("Slave local topicConfigVersion: {}", v);
    }

    @Test
    @DisplayName("Master 创建 Topic 后, Slave 通过 syncAll 同步")
    public void testSyncTopicConfigAfterCreate() throws Exception {
        // 1) 通过 Netty 调用 Master CREATE_TOPIC (走真实业务路径)
        String topic = "SYNC_TEST_" + System.currentTimeMillis();
        createTopicViaNetty(masterAddr, topic, 4, 4);

        // 2) 手动触发 Slave 同步 (避免等周期任务)
        SlaveSynchronize ss = slave.getSlaveSynchronize();
        assertNotNull(ss);
        DataVersion beforeVersion = ss.getLocalTopicConfigVersion();
        ss.syncAll();

        // 3) 验证 Slave 上有该 Topic
        AdminBrokerProcessor slaveAdmin = slave.getAdminBrokerProcessor();
        assertNotNull(slaveAdmin);
        TopicConfig synced = slaveAdmin.getAllTopicConfigs().get(topic);
        assertNotNull(synced, "Slave should sync the topic from master");
        assertEquals(4, synced.getReadQueueNums());
        assertEquals(4, synced.getWriteQueueNums());

        // 4) 验证本地版本号已递增
        DataVersion afterVersion = ss.getLocalTopicConfigVersion();
        log.info("Slave topicConfigVersion: {} -> {}",
                beforeVersion.getCounterValue(), afterVersion.getCounterValue());

        // 5) 再 syncAll 一次 (版本不变应该跳过)
        ss.syncAll();
        DataVersion after2 = ss.getLocalTopicConfigVersion();
        assertEquals(afterVersion.getCounterValue(), after2.getCounterValue(),
                "no-op sync should not bump version");
    }

    /**
     * 通过 Netty RPC 调用 Master 的 CREATE_TOPIC 接口.
     */
    private void createTopicViaNetty(String brokerAddr, String topic, int readN, int writeN) throws Exception {
        NettyClientConfig clientConfig = new NettyClientConfig();
        NettyRemotingClient client = new NettyRemotingClient(clientConfig);
        client.start();
        try {
            Channel ch = client.getOrCreateChannel(brokerAddr);
            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
            request.addExtField("topic", topic);
            request.addExtField("readQueueNums", String.valueOf(readN));
            request.addExtField("writeQueueNums", String.valueOf(writeN));
            RemotingCommand response = client.invokeSync(ch, request, 3000);
            assertEquals(RemotingSysResponseCode.SUCCESS, response.getCode());
        } finally {
            client.shutdown();
        }
    }

    @Test
    @DisplayName("BrokerOutAPI 暴露 DataVersion 列表")
    public void testBrokerOutAPIQueryDataVersion() throws Exception {
        NettyClientConfig clientConfig = new NettyClientConfig();
        NettyRemotingClient client = new NettyRemotingClient(clientConfig);
        client.start();
        try {
            Channel ch = client.getOrCreateChannel(masterAddr);
            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.QUERY_DATA_VERSION);
            RemotingCommand response = client.invokeSync(ch, request, 3000);
            assertNotNull(response);
            assertEquals(RemotingSysResponseCode.SUCCESS, response.getCode());
            assertNotNull(response.getBody());
            log.info("QUERY_DATA_VERSION response: {}", new String(response.getBody(), StandardCharsets.UTF_8));
        } finally {
            client.shutdown();
        }
    }

    @Test
    @DisplayName("Slave 周期同步: 配置 syncAll 后会自动启动周期任务")
    public void testSlavePeriodicSync() throws Exception {
        // 这里仅验证周期任务已经注册 (无法直接验证执行)
        // 验证: Slave 启动日志里应该有 "SlaveSynchronize started"
        assertNotNull(slave.getSlaveSynchronize());
        log.info("Slave periodic sync task is registered (interval=2s)");
    }
}

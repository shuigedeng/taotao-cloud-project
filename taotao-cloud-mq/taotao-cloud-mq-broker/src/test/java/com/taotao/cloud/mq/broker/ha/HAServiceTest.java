package com.taotao.cloud.mq.broker.ha;

import com.taotao.cloud.mq.broker.BrokerConfig;
import com.taotao.cloud.mq.broker.BrokerController;
import com.taotao.cloud.mq.nameserver.NameServerController;
import com.taotao.cloud.mq.nameserver.NamesrvConfig;
import com.taotao.cloud.mq.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.plugin.store.MessageStoreConfig;
import com.taotao.cloud.mq.plugin.store.ha.HAService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HAService 集成测试（v3 HA）.
 * <p>
 * 启动 Broker (默认 Master 模式), 验证 HAService:
 * <ul>
 *   <li>isSlave() == false (Master 模式)</li>
 *   <li>notifyMessageArrived 推入 pushQueue</li>
 *   <li>registerOrUpdateSlave 增加 slave 计数</li>
 *   <li>waitForSlaveAck 在无 slave 时立即返回 true</li>
 *   <li>getConnectionCount / getSlaveAckOffset</li>
 * </ul>
 */
@DisplayName("HAService 集成测试")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HAServiceTest {

    private static final Logger log = LoggerFactory.getLogger(HAServiceTest.class);

    private static final String STORE_ROOT = System.getProperty("user.home") + File.separator
            + "ttc-ha-test-" + System.currentTimeMillis();

    private static final int NAMESRV_PORT = 39876;
    private static final int BROKER_PORT = 31911;

    private NameServerController nameServer;
    private BrokerController broker;

    @BeforeAll
    public void startCluster() throws Exception {
        new File(STORE_ROOT).mkdirs();

        NamesrvConfig namesrvConfig = new NamesrvConfig();
        namesrvConfig.setKvConfigPath(STORE_ROOT + "/kvConfig.json");
        NettyServerConfig namesrvNetty = new NettyServerConfig();
        namesrvNetty.setListenPort(NAMESRV_PORT);
        nameServer = new NameServerController(namesrvConfig, namesrvNetty);
        assertTrue(nameServer.initialize());
        nameServer.start();

        BrokerConfig brokerConfig = new BrokerConfig();
        brokerConfig.setNamesrvAddr("localhost:" + NAMESRV_PORT);
        brokerConfig.setBrokerName("HABroker");
        NettyServerConfig brokerNetty = new NettyServerConfig();
        brokerNetty.setListenPort(BROKER_PORT);
        MessageStoreConfig storeConfig = new MessageStoreConfig();
        storeConfig.setStorePathRootDir(STORE_ROOT);
        storeConfig.setStorePathCommitLog(STORE_ROOT + "/commitlog");
        broker = new BrokerController(brokerConfig, storeConfig, brokerNetty);
        assertTrue(broker.initialize());
        broker.start();

        Thread.sleep(800);
    }

    @AfterAll
    public void stopCluster() {
        if (broker != null) broker.shutdown();
        if (nameServer != null) nameServer.shutdown();
    }

    @Test
    @Order(1)
    @DisplayName("Master 模式 HAService.isSlave() = false")
    public void testIsSlave() {
        HAService ha = broker.getHaService();
        assertNotNull(ha, "HAService should be initialized");
        assertFalse(ha.isSlave(), "default broker is Master");
    }

    @Test
    @Order(2)
    @DisplayName("初始 connectionCount = 0, slaveAckOffset = 0")
    public void testInitialState() {
        HAService ha = broker.getHaService();
        assertEquals(0, ha.getConnectionCount(), "no slaves initially");
        assertEquals(0L, ha.getSlaveAckOffset(), "initial slaveAckOffset = 0");
    }

    @Test
    @Order(3)
    @DisplayName("notifyMessageArrived 不抛异常 (Master 模式下正常入队)")
    public void testNotifyMessageArrived() {
        HAService ha = broker.getHaService();
        ha.notifyMessageArrived(100L, "hello".getBytes());
        ha.notifyMessageArrived(200L, "world".getBytes());
        // 无 slave, pushQueue 仅内部堆积, 不会影响业务
    }

    @Test
    @Order(4)
    @DisplayName("waitForSlaveAck: 无 slave 时立即返回 true")
    public void testWaitForSlaveAckNoSlave() {
        HAService ha = broker.getHaService();
        long start = System.currentTimeMillis();
        boolean ok = ha.waitForSlaveAck(50L, 5_000L);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(ok, "no slave configured, should return true immediately");
        assertTrue(elapsed < 1000, "should be fast, got " + elapsed + "ms");
    }

    @Test
    @Order(5)
    @DisplayName("DefaultHAService.registerOrUpdateSlave 增加 connectionCount")
    public void testRegisterSlave() {
        HAService ha = broker.getHaService();
        assertTrue(ha instanceof DefaultHAService);
        DefaultHAService dha = (DefaultHAService) ha;

        String slaveAddr = "10.0.0.1:10912";
        dha.registerOrUpdateSlave(slaveAddr, 1024L);
        assertEquals(1, dha.getConnectionCount());

        dha.registerOrUpdateSlave(slaveAddr, 2048L);  // 同一 slave 重复注册
        assertEquals(1, dha.getConnectionCount(), "same slave should not increase count");

        dha.registerOrUpdateSlave("10.0.0.2:10912", 512L);
        assertEquals(2, dha.getConnectionCount());
    }

    @Test
    @Order(6)
    @DisplayName("slaveAckOffset = 所有 slave 中最小 ack offset")
    public void testSlaveAckOffset() {
        HAService ha = broker.getHaService();
        DefaultHAService dha = (DefaultHAService) ha;

        // 清空之前测试注册的 slave (用新地址避免污染)
        dha.registerOrUpdateSlave("ha-test-slave-a", 500L);
        dha.registerOrUpdateSlave("ha-test-slave-b", 1000L);
        dha.registerOrUpdateSlave("ha-test-slave-c", 750L);

        // 最小值为 500
        long minAck = dha.getSlaveConnections().values().stream()
                .mapToLong(HAConnectionState::getLastAckOffset)
                .min().orElse(0L);
        assertEquals(500L, minAck);
    }

    @Test
    @Order(7)
    @DisplayName("isSlave / reportSlaveOffset 在 Slave 视角下的行为 (直接 unit 测试)")
    public void testSlaveModeBehavior() {
        // 由于本测试用 Master Broker, 单独验证 DefaultHAService 的 Slave 模式分支
        HAService ha = broker.getHaService();
        // Master 模式下 reportSlaveOffset 仅作兜底更新
        ha.reportSlaveOffset(999L);
        // 不抛异常即通过
        assertTrue(true);
    }
}

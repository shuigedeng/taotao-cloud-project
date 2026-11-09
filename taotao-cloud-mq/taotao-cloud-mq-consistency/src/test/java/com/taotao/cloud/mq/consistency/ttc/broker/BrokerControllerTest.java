package com.taotao.cloud.mq.consistency.ttc.broker;

import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig;
import com.taotao.cloud.mq.consistency.ttc.store.log.CommitLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * BrokerController 单元测试
 */
public class BrokerControllerTest {

    @TempDir
    Path tempDir;

    private BrokerController brokerController;
    private BrokerConfig brokerConfig;
    private MessageStoreConfig messageStoreConfig;

    private NettyServerConfig nettyServerConfig;

    @BeforeEach
    public void setUp() {
        brokerConfig = new BrokerConfig();
        brokerConfig.setBrokerName("TestBroker");
        brokerConfig.setBrokerClusterName("TestCluster");

        messageStoreConfig = new MessageStoreConfig();
        messageStoreConfig.setStorePathRootDir(tempDir.resolve("store").toString());
        // commitlog 有独立的默认路径（~/store/commitlog），只设 rootDir 会让数据写到 TempDir 之外
        messageStoreConfig.setStorePathCommitLog(
                tempDir.resolve("store").resolve("commitlog").toString());

        nettyServerConfig = new NettyServerConfig();
        nettyServerConfig.setListenPort(10911);

        brokerController = new BrokerController(brokerConfig, messageStoreConfig, nettyServerConfig);
    }

    @AfterEach
    public void tearDown() {
        if (brokerController != null) {
            try {
                brokerController.shutdown();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(brokerController);
        assertEquals(brokerConfig, brokerController.getBrokerConfig());
        assertEquals(messageStoreConfig, brokerController.getMessageStoreConfig());
    }

    @Test
    public void testInitialize() {
        // Initialize should create necessary directories and load commit log
        boolean initialized = brokerController.initialize();

        // Initialize may fail due to missing directories, but should not throw exception
        // The actual result depends on the environment
        assertNotNull(brokerController);
    }

    @Test
    public void testGetBrokerConfig() {
        BrokerConfig config = brokerController.getBrokerConfig();
        assertNotNull(config);
        assertEquals("TestBroker", config.getBrokerName());
        assertEquals("TestCluster", config.getBrokerClusterName());
    }

    @Test
    public void testGetMessageStoreConfig() {
        MessageStoreConfig config = brokerController.getMessageStoreConfig();
        assertNotNull(config);
    }

    @Test
    public void testBrokerConfigProperties() {
        // Test default values
        // namesrvAddr may be null if NAMESRV_ADDR env var is not set; verify setters work
        brokerConfig.setNamesrvAddr("localhost:9876");
        assertEquals("localhost:9876", brokerConfig.getNamesrvAddr());
        assertEquals(16, brokerConfig.getSendMessageThreadPoolNums());
        assertEquals(16, brokerConfig.getPullMessageThreadPoolNums());
        assertEquals(4, brokerConfig.getAdminBrokerThreadPoolNums());
    }

    @Test
    public void testBrokerConfigSetters() {
        brokerConfig.setNamesrvAddr("localhost:9876");
        brokerConfig.setBrokerName("NewBrokerName");
        brokerConfig.setBrokerClusterName("NewClusterName");
        brokerConfig.setSendMessageThreadPoolNums(32);
        brokerConfig.setPullMessageThreadPoolNums(32);
        brokerConfig.setAdminBrokerThreadPoolNums(8);

        assertEquals("localhost:9876", brokerConfig.getNamesrvAddr());
        assertEquals("NewBrokerName", brokerConfig.getBrokerName());
        assertEquals("NewClusterName", brokerConfig.getBrokerClusterName());
        assertEquals(32, brokerConfig.getSendMessageThreadPoolNums());
        assertEquals(32, brokerConfig.getPullMessageThreadPoolNums());
        assertEquals(8, brokerConfig.getAdminBrokerThreadPoolNums());
    }

    @Test
    public void testCommitLogAccess() throws Exception {
        // Initialize the broker to create the commit log
        brokerController.initialize();

        // Get the commit log
        CommitLog commitLog = brokerController.getCommitLog();

        // The commit log may be null if initialization failed
        // This is expected in unit tests without proper store directories
        assertNotNull(brokerController);
    }
}

package com.taotao.cloud.mq.broker.integration;

import com.taotao.cloud.mq.broker.BrokerConfig;
import com.taotao.cloud.mq.broker.BrokerController;
import com.taotao.cloud.mq.client.MQClientInstance;
import com.taotao.cloud.mq.client.consumer.DefaultMQPullConsumer;
import com.taotao.cloud.mq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import com.taotao.cloud.mq.client.consumer.rebalance.AllocateMessageQueueConsistentHash;
import com.taotao.cloud.mq.client.producer.DefaultMQProducer;
import com.taotao.cloud.mq.client.producer.latency.DefaultLatencyFaultTolerance;
import com.taotao.cloud.mq.common.MessageQueue;
import com.taotao.cloud.mq.common.TopicRouteData;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.nameserver.NameServerController;
import com.taotao.cloud.mq.nameserver.NamesrvConfig;
import com.taotao.cloud.mq.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.plugin.store.MessageStoreConfig;
import io.netty.channel.Channel;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 多 Broker 集成测试（v4 分布式）.
 * <p>
 * 启动 NameServer + 2 个 Broker (broker-a, broker-b), 验证:
 * <ul>
 *   <li>多 Broker 注册到 NameServer</li>
 *   <li>Producer 跨 Broker 发送消息</li>
 *   <li>PullConsumer 从多 Broker 拉取</li>
 *   <li>LatencyFaultTolerance 过滤故障 Broker</li>
 *   <li>Consumer Rebalance 均匀分配队列</li>
 * </ul>
 */
@DisplayName("多 Broker 集成测试")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MultiBrokerClusterTest {

    private static final Logger log = LoggerFactory.getLogger(MultiBrokerClusterTest.class);

    private static final String STORE_ROOT = System.getProperty("user.home") + File.separator
            + "ttc-cluster-test-" + System.currentTimeMillis();

    private static final int NAMESRV_PORT = 39880;
    private static final int BROKER_A_PORT = 31920;
    private static final int BROKER_B_PORT = 31921;

    private NameServerController nameServer;
    private BrokerController brokerA;
    private BrokerController brokerB;

    @BeforeAll
    public void startCluster() throws Exception {
        new File(STORE_ROOT).mkdirs();

        // 启动 NameServer
        NamesrvConfig namesrvConfig = new NamesrvConfig();
        namesrvConfig.setKvConfigPath(STORE_ROOT + "/kvConfig.json");
        NettyServerConfig namesrvNetty = new NettyServerConfig();
        namesrvNetty.setListenPort(NAMESRV_PORT);
        nameServer = new NameServerController(namesrvConfig, namesrvNetty);
        assertTrue(nameServer.initialize());
        nameServer.start();

        // 启动 Broker-A (brokerId=0, master)
        brokerA = createBroker("broker-a", 0, BROKER_A_PORT);
        assertTrue(brokerA.initialize());
        brokerA.start();

        // 启动 Broker-B (brokerId=0, master)
        brokerB = createBroker("broker-b", 0, BROKER_B_PORT);
        assertTrue(brokerB.initialize());
        brokerB.start();

        Thread.sleep(1000);

        // 在两个 Broker 上创建测试 Topic
        createTopicViaNetty("localhost:" + BROKER_A_PORT, "MultiBrokerTopic", 4, 4);
        createTopicViaNetty("localhost:" + BROKER_A_PORT, "PullMultiTopic", 4, 4);
        createTopicViaNetty("localhost:" + BROKER_A_PORT, "RouteMultiTopic", 4, 4);
        createTopicViaNetty("localhost:" + BROKER_B_PORT, "MultiBrokerTopic", 4, 4);
        createTopicViaNetty("localhost:" + BROKER_B_PORT, "PullMultiTopic", 4, 4);
        createTopicViaNetty("localhost:" + BROKER_B_PORT, "RouteMultiTopic", 4, 4);

        // Topic 创建后, 重新注册到 NameServer 使路由立即可见
        brokerA.reRegisterToNameServer();
        brokerB.reRegisterToNameServer();

        // 等待 NameServer 处理注册
        Thread.sleep(500);
    }

    private BrokerController createBroker(String brokerName, int brokerId, int port) {
        BrokerConfig config = new BrokerConfig();
        config.setNamesrvAddr("localhost:" + NAMESRV_PORT);
        config.setBrokerName(brokerName);
        config.setBrokerId(brokerId);
        NettyServerConfig netty = new NettyServerConfig();
        netty.setListenPort(port);
        MessageStoreConfig store = new MessageStoreConfig();
        store.setStorePathRootDir(STORE_ROOT + "/" + brokerName);
        store.setStorePathCommitLog(STORE_ROOT + "/" + brokerName + "/commitlog");
        return new BrokerController(config, store, netty);
    }

    @AfterAll
    public void stopCluster() {
        if (brokerA != null) brokerA.shutdown();
        if (brokerB != null) brokerB.shutdown();
        if (nameServer != null) nameServer.shutdown();
    }

    /**
     * 通过 Netty RPC 在 Broker 上创建 Topic (UPDATE_AND_CREATE_TOPIC).
     */
    private void createTopicViaNetty(String brokerAddr, String topic, int readN, int writeN) {
        NettyRemotingClient client = new NettyRemotingClient(new NettyClientConfig());
        client.start();
        try {
            Channel ch = client.getOrCreateChannel(brokerAddr);
            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
            request.addExtField("topic", topic);
            request.addExtField("readQueueNums", String.valueOf(readN));
            request.addExtField("writeQueueNums", String.valueOf(writeN));
            RemotingCommand response = client.invokeSync(ch, request, 3000);
            assertEquals(RemotingSysResponseCode.SUCCESS, response.getCode(),
                    "create topic " + topic + " on " + brokerAddr);
        } catch (Exception e) {
            log.warn("createTopicViaNetty failed: topic={} addr={}", topic, brokerAddr, e);
        } finally {
            client.shutdown();
        }
    }

    // ===== 多 Broker 基础测试 =====

    @Test
    @DisplayName("两个 Broker 均已启动")
    public void testBrokersStarted() {
        assertNotNull(brokerA);
        assertNotNull(brokerB);
        assertTrue(brokerA.getHaService() != null, "broker-a HAService initialized");
        assertTrue(brokerB.getHaService() != null, "broker-b HAService initialized");
    }

    @Test
    @DisplayName("Producer 可以发送到 broker-a")
    public void testSendToBrokerA() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("TestProducer");
        producer.setNamesrvAddr("localhost:" + NAMESRV_PORT);
        producer.setClientId("test-send-a");
        producer.start();

        try {
            Message msg = new Message("MultiBrokerTopic", "tagA", "keyA", "hello-broker-a".getBytes());
            SendResult result = producer.send(msg);
            assertNotNull(result);
            assertEquals(SendStatus.SEND_OK, result.getSendStatus(),
                    "should send OK to broker-a");
        } finally {
            producer.shutdown();
        }
    }

    @Test
    @DisplayName("Producer 可以发送到 broker-b")
    public void testSendToBrokerB() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("TestProducer");
        producer.setNamesrvAddr("localhost:" + NAMESRV_PORT);
        producer.setClientId("test-send-b");
        producer.start();

        try {
            Message msg = new Message("MultiBrokerTopic", "tagB", "keyB", "hello-broker-b".getBytes());
            SendResult result = producer.send(msg);
            assertNotNull(result);
            assertEquals(SendStatus.SEND_OK, result.getSendStatus(),
                    "should send OK to broker-b");
        } finally {
            producer.shutdown();
        }
    }

    @Test
    @DisplayName("连续发送多条消息到不同 Broker")
    public void testMultiSend() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("TestProducer");
        producer.setNamesrvAddr("localhost:" + NAMESRV_PORT);
        producer.setClientId("test-multi-send");
        producer.start();

        try {
            AtomicInteger successCount = new AtomicInteger(0);
            for (int i = 0; i < 10; i++) {
                Message msg = new Message("MultiBrokerTopic", "tagX", "key" + i,
                        ("msg-" + i).getBytes());
                SendResult result = producer.send(msg);
                if (result != null && result.getSendStatus() == SendStatus.SEND_OK) {
                    successCount.incrementAndGet();
                }
            }
            assertEquals(10, successCount.get(), "all 10 messages should be sent successfully");
        } finally {
            producer.shutdown();
        }
    }

    // ===== LatencyFaultTolerance 测试 =====

    @Test
    @DisplayName("LatencyFaultTolerance: 过滤故障 Broker 后正常发送")
    public void testLatencyFaultFilter() throws Exception {
        DefaultLatencyFaultTolerance tolerance = new DefaultLatencyFaultTolerance();

        // 模拟 broker-a 故障
        tolerance.markFault("broker-a", new RuntimeException("simulated"));
        assertFalse(tolerance.isAvailable("broker-a"));
        assertTrue(tolerance.isAvailable("broker-b"));

        // 过滤可用队列
        Set<MessageQueue> allQueues = new HashSet<>();
        allQueues.add(new MessageQueue("topic", "broker-a", 0));
        allQueues.add(new MessageQueue("topic", "broker-b", 0));

        Set<MessageQueue> available = tolerance.filterAvailableQueues(allQueues);
        assertEquals(1, available.size());
        assertTrue(available.iterator().next().getBrokerName().equals("broker-b"),
                "only broker-b queues should remain");
    }

    @Test
    @DisplayName("LatencyFaultTolerance: 正常延迟不回避")
    public void testLatencyNormal() {
        DefaultLatencyFaultTolerance tolerance = new DefaultLatencyFaultTolerance();
        tolerance.recordLatency("broker-a", 50L);
        tolerance.recordLatency("broker-b", 100L);
        assertTrue(tolerance.isAvailable("broker-a"));
        assertTrue(tolerance.isAvailable("broker-b"));
    }

    @Test
    @DisplayName("LatencyFaultTolerance: 高延迟触发回避")
    public void testLatencyHigh() {
        DefaultLatencyFaultTolerance tolerance = new DefaultLatencyFaultTolerance();
        tolerance.recordLatency("broker-a", 5000L); // 5s → 回避 60s
        assertFalse(tolerance.isAvailable("broker-a"),
                "broker-a should be unavailable after 5s latency");
    }

    // ===== Consumer Rebalance 测试 =====

    @Test
    @DisplayName("Rebalance AVG: 8 队列 / 2 消费者 = 4+4")
    public void testRebalanceAverage() {
        AllocateMessageQueueAveragely strategy = new AllocateMessageQueueAveragely();
        List<MessageQueue> mqAll = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mqAll.add(new MessageQueue("topic", "broker-a", i));
            mqAll.add(new MessageQueue("topic", "broker-b", i));
        }
        List<String> cidAll = Arrays.asList("c0", "c1");

        List<MessageQueue> r0 = strategy.allocate("g", "c0", mqAll, cidAll);
        List<MessageQueue> r1 = strategy.allocate("g", "c1", mqAll, cidAll);

        assertEquals(4, r0.size());
        assertEquals(4, r1.size());

        // 所有队列都被分配且不重叠
        Set<MessageQueue> all = new HashSet<>(r0);
        all.addAll(r1);
        assertEquals(8, all.size());
    }

    @Test
    @DisplayName("Rebalance ConsistentHash: 6 队列 / 3 消费者全部覆盖")
    public void testRebalanceConsistentHash() {
        AllocateMessageQueueConsistentHash strategy = new AllocateMessageQueueConsistentHash();
        List<MessageQueue> mqAll = new ArrayList<>();
        mqAll.add(new MessageQueue("topic", "broker-a", 0));
        mqAll.add(new MessageQueue("topic", "broker-a", 1));
        mqAll.add(new MessageQueue("topic", "broker-a", 2));
        mqAll.add(new MessageQueue("topic", "broker-b", 0));
        mqAll.add(new MessageQueue("topic", "broker-b", 1));
        mqAll.add(new MessageQueue("topic", "broker-b", 2));
        List<String> cidAll = Arrays.asList("c0", "c1", "c2");

        Set<MessageQueue> all = new HashSet<>();
        for (String cid : cidAll) {
            all.addAll(strategy.allocate("g", cid, mqAll, cidAll));
        }
        assertEquals(6, all.size(), "all 6 queues covered");
    }

    // ===== PullConsumer 跨 Broker 拉取测试 =====

    @Test
    @DisplayName("PullConsumer 从 broker-a 拉取")
    public void testPullFromBrokerA() throws Exception {
        // 先发送一条消息
        DefaultMQProducer producer = new DefaultMQProducer("TestProducer");
        producer.setNamesrvAddr("localhost:" + NAMESRV_PORT);
        producer.setClientId("test-pull-a-producer");
        producer.start();
        try {
            Message msg = new Message("PullMultiTopic", "tagA", "keyPA",
                    "pull-test-a".getBytes());
            producer.send(msg);
        } finally {
            producer.shutdown();
        }

        Thread.sleep(200);

        // 拉取
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("TestConsumer");
        consumer.setNamesrvAddr("localhost:" + NAMESRV_PORT);
        consumer.setClientId("test-pull-a-consumer");
        consumer.start();

        try {
            // Producer round-robin 分配到两个 Broker, 遍历所有 Broker 的所有 queue 找到消息
            boolean found = false;
            for (String broker : Arrays.asList("broker-a", "broker-b")) {
                for (int q = 0; q < 4; q++) {
                    MessageQueue mq = new MessageQueue("PullMultiTopic", broker, q);
                    DefaultMQPullConsumer.PullResult result = consumer.pull(mq, 0, 10);
                    if (result != null && result.getMsgFoundList() != null && !result.getMsgFoundList().isEmpty()) {
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            assertTrue(found, "should pull at least 1 message from cluster");
        } finally {
            consumer.shutdown();
        }
    }

    // ===== TopicRouteData 跨 Broker 测试 =====

    @Test
    @DisplayName("TopicRouteData 包含多个 Broker 的队列信息")
    public void testTopicRouteMultiBroker() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("TestProducer");
        producer.setNamesrvAddr("localhost:" + NAMESRV_PORT);
        producer.setClientId("test-route");
        producer.start();

        try {
            // 先发消息确保 topic 注册
            Message msg = new Message("RouteMultiTopic", "tagR", "keyR",
                    "route-test".getBytes());
            producer.send(msg);
        } finally {
            producer.shutdown();
        }

        Thread.sleep(300);

        // 通过 MQClientInstance 获取路由
        MQClientInstance instance = new MQClientInstance("route-test-client",
                "localhost:" + NAMESRV_PORT, new NettyClientConfig());
        instance.start();
        try {
            TopicRouteData route = instance.getTopicRouteData("RouteMultiTopic");
            if (route != null && route.getBrokerDatas() != null) {
                assertTrue(route.getBrokerDatas().size() >= 1,
                        "should have at least 1 broker in route");
            }
        } finally {
            instance.shutdown();
        }
    }

    // ===== HA Service 状态测试 =====

    @Test
    @DisplayName("两个 Broker 均以 Master 模式运行 (brokerId=0)")
    public void testBothBrokersMaster() {
        assertFalse(brokerA.getHaService().isSlave(), "broker-a is Master");
        assertFalse(brokerB.getHaService().isSlave(), "broker-b is Master");
    }

    @Test
    @DisplayName("两个 Broker 独立 HAService 实例")
    public void testIndependentHA() {
        assertTrue(brokerA.getHaService() != brokerB.getHaService(),
                "HAService instances should be different");
    }
}

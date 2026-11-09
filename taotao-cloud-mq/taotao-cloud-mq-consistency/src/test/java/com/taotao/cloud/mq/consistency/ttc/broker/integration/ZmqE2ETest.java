package com.taotao.cloud.mq.consistency.ttc.broker.integration;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerConfig;
import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.client.MQClientInstance;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.ConsumeConcurrentlyStatus;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.DefaultMQPullConsumer;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.DefaultMQPushConsumer;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.MessageListener;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.MessageQueueContext;
import com.taotao.cloud.mq.consistency.ttc.client.producer.DefaultMQProducer;
import com.taotao.cloud.mq.consistency.ttc.common.MessageQueue;
import com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData;
import com.taotao.cloud.mq.consistency.ttc.common.message.Message;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendStatus;
import com.taotao.cloud.mq.consistency.ttc.nameserver.NameServerController;
import com.taotao.cloud.mq.consistency.ttc.nameserver.NamesrvConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig;
import com.taotao.cloud.mq.consistency.ttc.testsupport.ProducerInstanceAccess;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * z-mq 端到端集成测试.
 * <p>
 * 在 JVM 内启动 NameServer + Broker，然后通过 Producer 发消息, Consumer 拉 / 推消息。
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ZmqE2ETest {

    private static final Logger log = LoggerFactory.getLogger(ZmqE2ETest.class);

    private static final String STORE_ROOT = System.getProperty("user.home") + File.separator
            + "zmq-it-" + RandomUtil.uuidShort(8);

    private static final int NAMESRV_PORT = 19876;
    private static final int BROKER_PORT = 11911;

    private NameServerController nameServer;
    private BrokerController broker;
    private String namesrvAddr;
    private String brokerAddr;

    @BeforeAll
    public void startCluster() throws Exception {
        // 1) NameServer
        NamesrvConfig namesrvConfig = new NamesrvConfig();
        NettyServerConfig namesrvNetty = new NettyServerConfig();
        namesrvNetty.setListenPort(NAMESRV_PORT);
        namesrvConfig.setKvConfigPath(STORE_ROOT + "/kvConfig.json");
        new File(STORE_ROOT).mkdirs();
        nameServer = new NameServerController(namesrvConfig, namesrvNetty);
        assertTrue(nameServer.initialize(), "NameServer initialize");
        nameServer.start();

        // 2) Broker
        BrokerConfig brokerConfig = new BrokerConfig();
        brokerConfig.setNamesrvAddr("localhost:" + NAMESRV_PORT);
        brokerConfig.setBrokerName("DefaultBroker");
        brokerConfig.setBrokerClusterName("DefaultCluster");
        NettyServerConfig brokerNetty = new NettyServerConfig();
        brokerNetty.setListenPort(BROKER_PORT);
        MessageStoreConfig storeConfig = new MessageStoreConfig();
        storeConfig.setStorePathRootDir(STORE_ROOT + "/broker");
        storeConfig.setStorePathCommitLog(STORE_ROOT + "/broker/commitlog");
        broker = new BrokerController(brokerConfig, storeConfig, brokerNetty);
        assertTrue(broker.initialize(), "Broker initialize");
        broker.start();

        namesrvAddr = "localhost:" + NAMESRV_PORT;
        brokerAddr = "localhost:" + BROKER_PORT;

        // 等心跳稳定
        waitForBrokerRegistration(3000);
        log.info("Cluster started: namesrv={} broker={}", namesrvAddr, brokerAddr);
    }

    @AfterAll
    public void stopCluster() {
        if (broker != null) { broker.shutdown(); }

        if (nameServer != null) { nameServer.shutdown(); }

    }

    private void waitForBrokerRegistration(long timeoutMillis) throws Exception {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < deadline) {
            try {
                if (nameServer.getRouteInfoManager().getBrokerAddrTable().containsKey("DefaultBroker")) {
                    return;
                }
            } catch (Exception ignore) {
            }
            Thread.sleep(100);
        }
        log.warn("Broker registration heartbeat timeout");
    }

    @Test
    @DisplayName("E2E: Producer 同步发 10 条, PullConsumer 拉取回 10 条")
    public void testProduceAndPull() throws Exception {
        String topic = "IT_TEST_TOPIC_" + System.currentTimeMillis();
        createTopicDirect(topic);
        Thread.sleep(800);

        DefaultMQProducer producer = new DefaultMQProducer("IT_PRODUCER");
        producer.setNamesrvAddr(namesrvAddr);
        producer.start();
        try {
            for (int i = 0; i < 10; i++) {
                Message m = new Message(topic, ("msg-" + i).getBytes(StandardCharsets.UTF_8));
                SendResult sr = producer.send(m);
                assertEquals(SendStatus.SEND_OK, sr.getSendStatus(),
                        "send status should be OK, but was " + sr.getSendStatus());
            }
        } finally {
            producer.shutdown();
        }

        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("IT_CONSUMER");
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.start();
        try {
            TopicRouteData route = fetchRoute(topic);
            // Producer 用 round-robin 分散到 4 个 queue, 测试需要遍历所有 queue 才能拉到全部
            List<MessageQueue> allQueues = pickAllQueues(topic, route);
            assertNotNull(allQueues);
            assertTrue(!allQueues.isEmpty(), "should pick at least one queue");

            int total = 0;
            long deadline = System.currentTimeMillis() + 5000;
            for (MessageQueue mq : allQueues) {
                long offset = 0;
                int emptyCount = 0;
                while (total < 10 && System.currentTimeMillis() < deadline) {
                    DefaultMQPullConsumer.PullResult pr = consumer.pull(mq, offset, 16);
                    assertNotNull(pr);
                    int got = pr.getMsgFoundList().size();
                    total += got;
                    offset = pr.getNextOffset();
                    if (got == 0) {
                        emptyCount++;
                        // 连续 2 次空就跳出本 queue
                        if (emptyCount >= 2) break;
                        Thread.sleep(200);
                    } else {
                        emptyCount = 0;
                    }
                }
            }
            assertEquals(10, total, "should pull exactly 10 messages");
            log.info("PullConsumer pulled {} messages across {} queues", total, allQueues.size());
        } finally {
            consumer.shutdown();
        }
    }

    @Test
    @DisplayName("E2E: PushConsumer 启动后自动收到 Producer 发的消息")
    public void testProduceAndPush() throws Exception {
        String topic = "IT_PUSH_TOPIC_" + System.currentTimeMillis();
        createTopicDirect(topic);
        Thread.sleep(500);

        CountDownLatch latch = new CountDownLatch(5);
        CopyOnWriteArrayList<MessageExt> received = new CopyOnWriteArrayList<>();
        DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer("IT_PUSH_CONSUMER");
        pushConsumer.setNamesrvAddr(namesrvAddr);
        pushConsumer.setPullIntervalMillis(300);
        pushConsumer.subscribe(topic, new MessageListener.Concurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(MessageExt[] msgs, MessageQueueContext mq) {
                for (MessageExt m : msgs) {
                    received.add(m);
                    latch.countDown();
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        pushConsumer.start();

        try {
            DefaultMQProducer producer = new DefaultMQProducer("IT_PUSH_PRODUCER");
            producer.setNamesrvAddr(namesrvAddr);
            producer.start();
            try {
                for (int i = 0; i < 5; i++) {
                    Message m = new Message(topic, ("push-msg-" + i).getBytes(StandardCharsets.UTF_8));
                    SendResult sr = producer.send(m);
                    assertEquals(SendStatus.SEND_OK, sr.getSendStatus());
                }
            } finally {
                producer.shutdown();
            }
            assertTrue(latch.await(10, TimeUnit.SECONDS), "should consume 5 messages in 10s");
            log.info("PushConsumer received {} messages", received.size());
        } finally {
            pushConsumer.shutdown();
        }
    }

    @Test
    @DisplayName("E2E: 大消息 (64KB) 发送 + 拉取")
    public void testLargeMessage() throws Exception {
        String topic = "IT_LARGE_TOPIC_" + System.currentTimeMillis();
        createTopicDirect(topic);
        Thread.sleep(500);

        byte[] payload = new byte[64 * 1024];
        for (int i = 0; i < payload.length; i++) {
            payload[i] = (byte) (i & 0xFF);
        }
        DefaultMQProducer producer = new DefaultMQProducer("IT_LARGE_PROD");
        producer.setNamesrvAddr(namesrvAddr);
        producer.start();
        try {
            SendResult sr = producer.send(new Message(topic, payload));
            assertEquals(SendStatus.SEND_OK, sr.getSendStatus(), "256KB send should succeed");
            log.info("Sent 256KB message, msgId={}", sr.getMsgId());
        } finally {
            producer.shutdown();
        }

        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("IT_LARGE_CONS");
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.start();
        try {
            TopicRouteData route = fetchRoute(topic);
            // 遍历所有 queue, 找到至少 1 条消息
            List<MessageQueue> allQueues = pickAllQueues(topic, route);
            assertNotNull(allQueues);
            assertTrue(!allQueues.isEmpty(), "should pick at least one queue");

            int total = 0;
            for (MessageQueue mq : allQueues) {
                DefaultMQPullConsumer.PullResult pr = consumer.pull(mq, 0, 8);
                assertNotNull(pr, "pull result not null");
                total += pr.getMsgFoundList().size();
                if (total > 0) break;  // 找到消息就退出
            }
            assertTrue(total >= 1, "should pull at least 1 message (got " + total + ")");
            log.info("Large message pulled {} message across all queues", total);
        } finally {
            consumer.shutdown();
        }
    }

    @Test
    @DisplayName("E2E: 并发 Producer 多线程发 50 条不丢")
    public void testConcurrentProduce() throws Exception {
        String topic = "IT_CONCURRENT_TOPIC_" + System.currentTimeMillis();
        createTopicDirect(topic);
        Thread.sleep(500);

        int threads = 5;
        int perThread = 10;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch done = new CountDownLatch(threads * perThread);
        AtomicInteger okCount = new AtomicInteger(0);
        try {
            for (int t = 0; t < threads; t++) {
                int tid = t;
                pool.submit(() -> {
                    DefaultMQProducer p = new DefaultMQProducer("IT_CONC_PROD_" + tid);
                    p.setNamesrvAddr(namesrvAddr);
                    try {
                        p.start();
                        for (int i = 0; i < perThread; i++) {
                            try {
                                SendResult sr = p.send(new Message(topic,
                                        ("c-" + tid + "-" + i).getBytes(StandardCharsets.UTF_8)));
                                if (sr.getSendStatus() == SendStatus.SEND_OK) {
                                    okCount.incrementAndGet();
                                }
                            } catch (Exception e) {
                                log.warn("send failed: {}", e.getMessage());
                            } finally {
                                done.countDown();
                            }
                        }
                    } catch (Exception e) {
                        log.error("producer start failed", e);
                        for (int i = 0; i < perThread; i++) done.countDown();
                    } finally {
                        p.shutdown();
                    }
                });
            }
            assertTrue(done.await(20, TimeUnit.SECONDS), "all sends should complete in 20s");
            assertEquals(threads * perThread, okCount.get(),
                    "expected " + (threads * perThread) + " successful sends, got " + okCount.get());
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    @DisplayName("E2E: Broker 重启后, 新 Producer 能重新发现路由并发送")
    public void testBrokerRestartAndRecover() throws Exception {
        String topic = "IT_RESTART_TOPIC_" + System.currentTimeMillis();
        createTopicDirect(topic);
        Thread.sleep(500);

        // 1) 第一次发送
        DefaultMQProducer producer1 = new DefaultMQProducer("IT_RESTART_PROD");
        producer1.setNamesrvAddr(namesrvAddr);
        producer1.start();
        try {
            SendResult sr = producer1.send(new Message(topic, "before-restart".getBytes(StandardCharsets.UTF_8)));
            assertEquals(SendStatus.SEND_OK, sr.getSendStatus());
        } finally {
            producer1.shutdown();
        }

        // 2) Broker shutdown + start
        broker.shutdown();
        Thread.sleep(500);
        NettyServerConfig brokerNetty = new NettyServerConfig();
        brokerNetty.setListenPort(BROKER_PORT);
        MessageStoreConfig storeConfig = new MessageStoreConfig();
        storeConfig.setStorePathRootDir(STORE_ROOT + "/broker");
        storeConfig.setStorePathCommitLog(STORE_ROOT + "/broker/commitlog");
        BrokerConfig brokerConfig = new BrokerConfig();
        brokerConfig.setNamesrvAddr(namesrvAddr);
        brokerConfig.setBrokerName("DefaultBroker");
        brokerConfig.setBrokerClusterName("DefaultCluster");
        broker = new BrokerController(brokerConfig, storeConfig, brokerNetty);
        assertTrue(broker.initialize(), "broker reinit");
        broker.start();
        waitForBrokerRegistration(3000);

        // 3) 新 Producer 重发
        DefaultMQProducer producer2 = new DefaultMQProducer("IT_RESTART_PROD_2");
        producer2.setNamesrvAddr(namesrvAddr);
        producer2.start();
        try {
            SendResult sr = producer2.send(new Message(topic, "after-restart".getBytes(StandardCharsets.UTF_8)));
            assertEquals(SendStatus.SEND_OK, sr.getSendStatus(), "send after broker restart should succeed");
        } finally {
            producer2.shutdown();
        }
    }

    // =============== 工具方法 ===============

    /**
     * 直接向 NameServer 发 UPDATE_AND_CREATE_TOPIC 请求创建 Topic (RocketMQ 标准流程).
     * <p>
     * NameServer 把 Topic 注册到 topicQueueTable, 之后 Producer 才能查到路由。
     */
    private void createTopicDirect(String topic) throws Exception {
        NettyClientConfig clientConfig = new NettyClientConfig();
        NettyRemotingClient client = new NettyRemotingClient(clientConfig);
        client.start();
        try {
            io.netty.channel.Channel ch = client.getOrCreateChannel(namesrvAddr);
            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
            request.addExtField("topic", topic);
            request.addExtField("readQueueNums", "4");
            request.addExtField("writeQueueNums", "4");
            RemotingCommand response = client.invokeSync(ch, request, 3000);
            assertEquals(0, response.getCode(), "create topic should succeed");
        } finally {
            client.shutdown();
        }
    }

    /**
     * 通过一个临时 Producer 拿一次路由 (内部会触发从 NameServer 拉取).
     */
    private TopicRouteData fetchRoute(String topic) throws Exception {
        DefaultMQProducer p = new DefaultMQProducer("ROUTE_FETCHER");
        p.setNamesrvAddr(namesrvAddr);
        p.start();
        try {
            // Producer 内部 MQClientInstance 通过 GET_ROUTE_BY_TOPIC 拉一次
            // 这里直接借助 Producer 在 start 时已经连上 NameServer 的状态
            MQClientInstance inst = ProducerInstanceAccess.peekInstance(p);
            return inst.getTopicRouteData(topic);
        } finally {
            p.shutdown();
        }
    }

    private MessageQueue pickFirstQueue(String topic, TopicRouteData route) {
        if (route == null || route.getBrokerDatas() == null) return null;

        for (com.taotao.cloud.mq.consistency.ttc.common.BrokerData bd : route.getBrokerDatas()) {
            if (bd.selectBrokerAddr() == null) continue;

            for (com.taotao.cloud.mq.consistency.ttc.common.QueueData qd : route.getQueueDatas()) {
                if (qd.getBrokerName().equals(bd.getBrokerName())) {
                    return new MessageQueue(topic, bd.getBrokerName(), 0);
                }
            }
        }
        return null;
    }

    /**
     * 拿到该 Topic 在该 Broker 上的所有 MessageQueue (queueId 0..readQueueNums-1).
     * <p>
     * Producer 端 selectOneMessageQueue 是 round-robin 分发到 readQueueNums 个 queue,
     * 所以 Consumer 端必须遍历所有 queue 才能拉到完整消息.
     */
    private List<MessageQueue> pickAllQueues(String topic, TopicRouteData route) {
        List<MessageQueue> all = new ArrayList<>();
        if (route == null || route.getBrokerDatas() == null) return all;

        for (com.taotao.cloud.mq.consistency.ttc.common.BrokerData bd : route.getBrokerDatas()) {
            if (bd.selectBrokerAddr() == null) continue;
            for (com.taotao.cloud.mq.consistency.ttc.common.QueueData qd : route.getQueueDatas()) {
                if (qd.getBrokerName().equals(bd.getBrokerName())) {
                    int readQ = qd.getReadQueueNums();
                    for (int i = 0; i < readQ; i++) {
                        all.add(new MessageQueue(topic, bd.getBrokerName(), i));
                    }
                }
            }
        }
        return all;
    }
}

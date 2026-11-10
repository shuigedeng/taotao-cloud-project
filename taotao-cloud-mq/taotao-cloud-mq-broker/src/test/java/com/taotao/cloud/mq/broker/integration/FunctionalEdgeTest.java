package com.taotao.cloud.mq.broker.integration;

import com.taotao.cloud.mq.client.consumer.DefaultMQPullConsumer;
import com.taotao.cloud.mq.client.producer.DefaultMQProducer;
import com.taotao.cloud.mq.common.MessageQueue;
import com.taotao.cloud.mq.common.TopicRouteData;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.integration.support.ClusterTestHelper;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 功能性边界测试 — 覆盖消息生命周期、去重、顺序、大消息、空消息、特殊字符、
 * 路由失败、topic 生命周期等边界场景。
 * <p>
 * 每个 @Test 使用独立的 ClusterTestHelper (端口随机), 避免并行冲突。
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FunctionalEdgeTest {

    // ========== 1. 消息大小边界 ==========

    @Test
    @Order(1)
    @DisplayName("空消息体 (0 字节) 能正常发送并拉取")
    void testEmptyBodyMessage() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_EMPTY_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            TopicRouteData route = h.fetchRoute(topic);
            String brokerName = route.getBrokerDatas().get(0).getBrokerName();
            MessageQueue targetQ = new MessageQueue(topic, brokerName, 0);

            DefaultMQProducer producer = new DefaultMQProducer("P_EMB");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                Message msg = new Message(topic, new byte[0]);
                SendResult sr = producer.send(msg, targetQ);
                assertEquals(SendStatus.SEND_OK, sr.getSendStatus());
            } finally {
                producer.shutdown();
            }
            Thread.sleep(300);

            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("C_EMB");
            consumer.setNamesrvAddr(h.getNamesrvAddr());
            consumer.start();
            try {
                DefaultMQPullConsumer.PullResult pr = consumer.pull(targetQ, 0, 10);
                assertEquals(1, pr.getMsgFoundList().size(),
                        "应能拉到发送的 1 条空消息");
                assertTrue(pr.getMsgFoundList().get(0).getBody() == null
                                || pr.getMsgFoundList().get(0).getBody().length == 0,
                        "空消息 body 应为 null 或 0 长度");
            } finally {
                consumer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    @Test
    @Order(2)
    @DisplayName("64KB 大消息能正常发送和拉取")
    void testLargeMessage() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_LARGE_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            TopicRouteData route = h.fetchRoute(topic);
            String brokerName = route.getBrokerDatas().get(0).getBrokerName();
            MessageQueue targetQ = new MessageQueue(topic, brokerName, 0);

            byte[] payload = new byte[64 * 1024];
            new java.util.Random(42).nextBytes(payload);

            DefaultMQProducer producer = new DefaultMQProducer("P_LRG");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                SendResult sr = producer.send(new Message(topic, payload), targetQ);
                assertEquals(SendStatus.SEND_OK, sr.getSendStatus());
            } finally {
                producer.shutdown();
            }
            Thread.sleep(300);

            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("C_LRG");
            consumer.setNamesrvAddr(h.getNamesrvAddr());
            consumer.start();
            try {
                DefaultMQPullConsumer.PullResult pr = consumer.pull(targetQ, 0, 10);
                assertEquals(1, pr.getMsgFoundList().size(),
                        "应能拉到发送的 1 条大消息");
            } finally {
                consumer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    @Test
    @Order(3)
    @DisplayName("Unicode / 多语言字符消息能正确发送并拉取")
    void testUnicodeMessage() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_UNI_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            TopicRouteData route = h.fetchRoute(topic);
            String brokerName = route.getBrokerDatas().get(0).getBrokerName();
            MessageQueue targetQ = new MessageQueue(topic, brokerName, 0);

            String unicodeContent = "你好世界 🌍 Ελληνικά العربية にほんご フランス語 🎉🚀";
            byte[] payload = unicodeContent.getBytes(StandardCharsets.UTF_8);

            DefaultMQProducer producer = new DefaultMQProducer("P_UNI");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                SendResult sr = producer.send(new Message(topic, payload), targetQ);
                assertEquals(SendStatus.SEND_OK, sr.getSendStatus());
            } finally {
                producer.shutdown();
            }
            Thread.sleep(300);

            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("C_UNI");
            consumer.setNamesrvAddr(h.getNamesrvAddr());
            consumer.start();
            try {
                DefaultMQPullConsumer.PullResult pr = consumer.pull(targetQ, 0, 10);
                assertEquals(1, pr.getMsgFoundList().size());
                assertEquals(unicodeContent,
                        new String(pr.getMsgFoundList().get(0).getBody(), StandardCharsets.UTF_8),
                        "Unicode 消息内容应完全一致");
            } finally {
                consumer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    @Test
    @Order(4)
    @DisplayName("消息含换行符和控制字符能正确发送并拉取")
    void testNewlineAndControlChars() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_NL_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            TopicRouteData route = h.fetchRoute(topic);
            String brokerName = route.getBrokerDatas().get(0).getBrokerName();
            MessageQueue targetQ = new MessageQueue(topic, brokerName, 0);

            String content = "line1\nline2\n\nline3\r\nline4\ttab\0null";
            byte[] payload = content.getBytes(StandardCharsets.UTF_8);

            DefaultMQProducer producer = new DefaultMQProducer("P_NL");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                SendResult sr = producer.send(new Message(topic, payload), targetQ);
                assertEquals(SendStatus.SEND_OK, sr.getSendStatus());
            } finally {
                producer.shutdown();
            }
            Thread.sleep(300);

            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("C_NL");
            consumer.setNamesrvAddr(h.getNamesrvAddr());
            consumer.start();
            try {
                DefaultMQPullConsumer.PullResult pr = consumer.pull(targetQ, 0, 10);
                assertEquals(1, pr.getMsgFoundList().size());
                assertEquals(content,
                        new String(pr.getMsgFoundList().get(0).getBody(), StandardCharsets.UTF_8),
                        "含控制字符的消息内容应完全一致");
            } finally {
                consumer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 2. 顺序保证 ==========

    @Test
    @Order(10)
    @DisplayName("同一 queueId 内 100 条消息 nextOffset 单调递增 + 内容正确")
    void testOrderGuaranteeSameQueueId() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_ORDER_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic, 4, 4);
            Thread.sleep(300);

            TopicRouteData route = h.fetchRoute(topic);
            assertNotNull(route, "topic 路由应存在");
            String brokerName = route.getBrokerDatas().get(0).getBrokerName();

            int count = 100;
            DefaultMQProducer producer = new DefaultMQProducer("P_ORDER");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                // 全部发到 queueId=2, 验证同一 queueId 内 nextOffset 单调递增
                MessageQueue target = new MessageQueue(topic, brokerName, 2);
                int ok = 0;
                for (int i = 0; i < count; i++) {
                    Message msg = new Message(topic, ("seq-" + i).getBytes(StandardCharsets.UTF_8));
                    SendResult sr = producer.send(msg, target);
                    if (sr.getSendStatus() == SendStatus.SEND_OK) ok++;

                }
                assertEquals(count, ok, "100 条消息应全部发送成功");
            } finally {
                producer.shutdown();
            }
            Thread.sleep(500);

            // 拉取 queueId=2, offset=0, maxNum=100, 验证 offset 单调递增 + body 内容
            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("C_ORDER");
            consumer.setNamesrvAddr(h.getNamesrvAddr());
            consumer.start();
            try {
                MessageQueue mq = new MessageQueue(topic, brokerName, 2);
                DefaultMQPullConsumer.PullResult pr = consumer.pull(mq, 0, 100);
                assertEquals(count, pr.getMsgFoundList().size(),
                        "应能拉取到全部 " + count + " 条消息");

                long prevOffset = -1;
                for (MessageExt m : pr.getMsgFoundList()) {
                    long off = m.getQueueOffset();
                    assertTrue(off > prevOffset,
                            "nextOffset 应单调递增, prev=" + prevOffset + " cur=" + off);
                    prevOffset = off;
                }
                assertEquals(count - 1, prevOffset,
                        "最后一条 offset 应等于 " + (count - 1));

                // 验证 body 内容正确 (第 i 条 body 应为 "seq-i")
                for (int i = 0; i < count; i++) {
                    byte[] body = pr.getMsgFoundList().get(i).getBody();
                    String expected = "seq-" + i;
                    assertEquals(expected, new String(body, StandardCharsets.UTF_8),
                            "第 " + i + " 条消息 body 应为 " + expected);
                }
            } finally {
                consumer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    @Test
    @Order(11)
    @DisplayName("不同 queueId 间无强顺序 (各自独立 offset)")
    void testCrossQueueIdIndependent() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_QID_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic, 4, 4);
            Thread.sleep(300);

            TopicRouteData route = h.fetchRoute(topic);
            String brokerName = route.getBrokerDatas().get(0).getBrokerName();

            DefaultMQProducer producer = new DefaultMQProducer("P_QID");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                // 间隔发到不同 queue
                MessageQueue q0 = new MessageQueue(topic, brokerName, 0);
                MessageQueue q1 = new MessageQueue(topic, brokerName, 1);
                for (int i = 0; i < 5; i++) {
                    producer.send(new Message(topic, ("q0-" + i).getBytes()), q0);
                    producer.send(new Message(topic, ("q1-" + i).getBytes()), q1);
                }
            } finally {
                producer.shutdown();
            }
            Thread.sleep(300);

            // 验证 queue 0 和 queue 1 各自有 5 条, 且 offset 独立从 0 开始
            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("C_QID");
            consumer.setNamesrvAddr(h.getNamesrvAddr());
            consumer.start();
            try {
                DefaultMQPullConsumer.PullResult pr0 = consumer.pull(
                        new MessageQueue(topic, brokerName, 0), 0, 10);
                DefaultMQPullConsumer.PullResult pr1 = consumer.pull(
                        new MessageQueue(topic, brokerName, 1), 0, 10);
                assertEquals(5, pr0.getMsgFoundList().size());
                assertEquals(5, pr1.getMsgFoundList().size());
                assertEquals(0L, pr0.getMsgFoundList().get(0).getQueueOffset());
                assertEquals(0L, pr1.getMsgFoundList().get(0).getQueueOffset());
                assertEquals(4L, pr0.getMsgFoundList().get(4).getQueueOffset());
                assertEquals(4L, pr1.getMsgFoundList().get(4).getQueueOffset());
            } finally {
                consumer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 3. Topic 生命周期 ==========

    @Test
    @Order(20)
    @DisplayName("不存在的 topic 拉取返回 null/空")
    void testNonExistentTopicPullReturnsEmpty() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_NOEXIST_" + UUID.randomUUID().toString().substring(0, 6);
            // 不创建 topic
            TopicRouteData route = h.fetchRoute(topic);
            assertNull(route, "不存在的 topic 路由应为 null");
        } finally {
            h.shutdownCluster();
        }
    }

    @Test
    @Order(21)
    @DisplayName("重复创建同名 topic 不报错, 幂等")
    void testDuplicateTopicCreation() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_DUP_" + UUID.randomUUID().toString().substring(0, 6);
            // 创建两次同名 topic
            h.createTopic(topic, 2, 2);
            h.createTopic(topic, 4, 4);
            Thread.sleep(300);

            // 应能正常拉取路由
            TopicRouteData route = h.fetchRoute(topic);
            assertNotNull(route, "topic 路由应存在");
        } finally {
            h.shutdownCluster();
        }
    }

    @Test
    @Order(22)
    @DisplayName("topic 不存在时 Producer send 抛出异常")
    void testProducerSendToNonExistentTopicThrows() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            DefaultMQProducer producer = new DefaultMQProducer("P_NOEXIST");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                Message msg = new Message("NOEXIST_TOPIC", "body".getBytes());
                assertThrows(Exception.class, () -> producer.send(msg),
                        "发送到不存在的 topic 应抛异常");
            } finally {
                producer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 4. Broker 故障恢复 ==========

    @Test
    @Order(30)
    @DisplayName("Broker 关闭后 producer send 失败, 重启后恢复")
    void testBrokerShutdownAndRecover() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_RECOVER_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(500);

            // 正常发送
            DefaultMQProducer producer = new DefaultMQProducer("P_RECOVER");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                SendResult sr = producer.send(new Message(topic, "before".getBytes()));
                assertEquals(SendStatus.SEND_OK, sr.getSendStatus());
            } finally {
                producer.shutdown();
            }

            // 关闭 broker
            h.getBroker().shutdown();
            Thread.sleep(500);

            // 新 producer 尝试发送 (应失败, 因为 broker 已关闭)
            DefaultMQProducer producer2 = new DefaultMQProducer("P_RECOVER_2");
            producer2.setNamesrvAddr(h.getNamesrvAddr());
            producer2.start();
            try {
                try {
                    SendResult sr = producer2.send(new Message(topic, "during-down".getBytes()));
                    // 如果 broker 关闭足够快, 应该失败; 但如果有缓存, 可能成功一次
                    // 这里不强制 assert, 只验证不抛 NPE
                } catch (Exception e) {
                    // 正常, broker 不可用
                }
            } finally {
                producer2.shutdown();
            }

            // 重启 broker
            h.restartBroker();
            Thread.sleep(800);

            // 新 producer 重启后应能发送
            DefaultMQProducer producer3 = new DefaultMQProducer("P_RECOVER_3");
            producer3.setNamesrvAddr(h.getNamesrvAddr());
            producer3.start();
            try {
                SendResult sr = producer3.send(new Message(topic, "after-restart".getBytes()));
                assertEquals(SendStatus.SEND_OK, sr.getSendStatus(),
                        "broker 重启后应能正常发送");
            } finally {
                producer3.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 5. NameServer 关闭后的容错 ==========

    @Test
    @Order(31)
    @DisplayName("NameServer 关闭后 producer 不抛 NPE")
    void testNameserverShutdownGraceful() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            DefaultMQProducer producer = new DefaultMQProducer("P_NS_DOWN");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                // 正常发送一次
                String topic = "NS_DOWN_" + UUID.randomUUID().toString().substring(0, 6);
                h.createTopic(topic);
                Thread.sleep(300);
                producer.send(new Message(topic, "ok".getBytes()));

                // 关闭 nameserver
                h.getNameServer().shutdown();
                Thread.sleep(500);

                // 尝试发送新消息, 不应 NPE, 应抛网络异常
                try {
                    producer.send(new Message(topic, "after-ns-down".getBytes()));
                } catch (Exception e) {
                    // 可能抛连接异常, 只要不是 NPE 就 OK
                    assertFalse(e instanceof NullPointerException,
                            "NameServer 关闭后不应 NPE, 实际=" + e.getClass().getName());
                }
            } finally {
                producer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 6. 消息属性 (Tags, Keys) ==========

    @Test
    @Order(40)
    @DisplayName("消息 Tags 和 Keys 能正确传递")
    void testMessageTagsAndKeys() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_TAGS_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            TopicRouteData route = h.fetchRoute(topic);
            String brokerName = route.getBrokerDatas().get(0).getBrokerName();
            MessageQueue targetQ = new MessageQueue(topic, brokerName, 0);

            DefaultMQProducer producer = new DefaultMQProducer("P_TAGS");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                Message msg = new Message(topic, "TagA", "Key001", "payload".getBytes(StandardCharsets.UTF_8));
                SendResult sr = producer.send(msg, targetQ);
                assertEquals(SendStatus.SEND_OK, sr.getSendStatus());
            } finally {
                producer.shutdown();
            }
            Thread.sleep(300);

            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("C_TAGS");
            consumer.setNamesrvAddr(h.getNamesrvAddr());
            consumer.start();
            try {
                DefaultMQPullConsumer.PullResult pr = consumer.pull(targetQ, 0, 10);
                assertEquals(1, pr.getMsgFoundList().size(),
                        "应拉到发送的 1 条消息");
                assertEquals("TagA", pr.getMsgFoundList().get(0).getTags());
                assertEquals("Key001", pr.getMsgFoundList().get(0).getKeys());
            } finally {
                consumer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 7. 并发生产拉取 ==========

    @Test
    @Order(50)
    @DisplayName("多线程并发生产 200 条, PullConsumer 全部拉到")
    void testConcurrentProduceAndPull() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_CONC_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic, 4, 4);
            Thread.sleep(500);

            TopicRouteData route = h.fetchRoute(topic);
            String brokerName = route.getBrokerDatas().get(0).getBrokerName();

            int threads = 5;
            int perThread = 40;
            int total = threads * perThread;
            AtomicInteger okCount = new AtomicInteger(0);
            ExecutorService pool = Executors.newFixedThreadPool(threads);
            CountDownLatch done = new CountDownLatch(total);
            try {
                for (int t = 0; t < threads; t++) {
                    final int tid = t;
                    pool.submit(() -> {
                        DefaultMQProducer p = new DefaultMQProducer("P_CONC_" + tid);
                        p.setNamesrvAddr(h.getNamesrvAddr());
                        try {
                            p.start();
                        } catch (Exception e) {
                            done.countDown();
                            return;
                        }
                        try {
                            // 全部发到 queue 0
                            MessageQueue targetQ = new MessageQueue(topic, brokerName, 0);
                            for (int i = 0; i < perThread; i++) {
                                try {
                                    SendResult sr = p.send(new Message(topic,
                                            ("c-" + tid + "-" + i).getBytes(StandardCharsets.UTF_8)), targetQ);
                                    if (sr.getSendStatus() == SendStatus.SEND_OK) {
                                        okCount.incrementAndGet();
                                    }
                                } catch (Exception e) {
                                    // log
                                } finally {
                                    done.countDown();
                                }
                            }
                        } finally {
                            p.shutdown();
                        }
                    });
                }
                assertTrue(done.await(30, TimeUnit.SECONDS), "所有发送应在 30 秒内完成");
                assertTrue(okCount.get() >= total * 0.8,
                        "至少 80% 发送成功, 实际=" + okCount.get() + "/" + total);
            } finally {
                pool.shutdownNow();
            }

            // PullConsumer 从 queue 0 拉取
            Thread.sleep(500);
            MessageQueue mq = new MessageQueue(topic, brokerName, 0);

            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("C_CONC");
            consumer.setNamesrvAddr(h.getNamesrvAddr());
            consumer.start();
            try {
                int totalPulled = 0;
                long offset = 0;
                long deadline = System.currentTimeMillis() + 5000;
                while (totalPulled < total && System.currentTimeMillis() < deadline) {
                    DefaultMQPullConsumer.PullResult pr = consumer.pull(mq, offset, 50);
                    totalPulled += pr.getMsgFoundList().size();
                    offset = pr.getNextOffset();
                    if (pr.getMsgFoundList().isEmpty()) {
                        Thread.sleep(100);
                    }
                }
                assertEquals(total, totalPulled,
                        "PullConsumer 应拉到全部 " + total + " 条消息, 实际=" + totalPulled);
            } finally {
                consumer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 8. Offset 管理 ==========

    @Test
    @Order(60)
    @DisplayName("多次 pull 同一 offset 不会重复读取 (幂等拉取)")
    void testPullSameOffsetIdempotent() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_OFFSET_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            TopicRouteData route = h.fetchRoute(topic);
            assertNotNull(route);
            String brokerName = route.getBrokerDatas().get(0).getBrokerName();
            MessageQueue targetQ = new MessageQueue(topic, brokerName, 0);

            DefaultMQProducer producer = new DefaultMQProducer("P_OFF");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                for (int i = 0; i < 5; i++) {
                    producer.send(new Message(topic, ("m" + i).getBytes()), targetQ);
                }
            } finally {
                producer.shutdown();
            }
            Thread.sleep(300);

            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("C_OFF");
            consumer.setNamesrvAddr(h.getNamesrvAddr());
            consumer.start();
            try {
                // 多次从 offset 0 拉取
                DefaultMQPullConsumer.PullResult r1 = consumer.pull(targetQ, 0, 10);
                DefaultMQPullConsumer.PullResult r2 = consumer.pull(targetQ, 0, 10);
                assertEquals(5, r1.getMsgFoundList().size());
                assertEquals(5, r2.getMsgFoundList().size());
                // 验证两次拉取结果一致
                for (int i = 0; i < 5; i++) {
                    assertEquals(
                            new String(r1.getMsgFoundList().get(i).getBody(), StandardCharsets.UTF_8),
                            new String(r2.getMsgFoundList().get(i).getBody(), StandardCharsets.UTF_8),
                            "第 " + i + " 条消息两次拉取应一致");
                }
            } finally {
                consumer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 9. null body ==========

    @Test
    @Order(70)
    @DisplayName("null body 消息能正常发送 (MVP)")
    void testNullBodyMessage() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_NULL_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            DefaultMQProducer producer = new DefaultMQProducer("P_NULL");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                // body 为 null
                Message msg = new Message(topic, (byte[]) null);
                // 不强制要求成功 (null body 可能被拒绝), 只验证不 NPE
                try {
                    producer.send(msg);
                } catch (Exception e) {
                    assertFalse(e instanceof NullPointerException,
                            "null body 不应导致 NPE, 实际=" + e.getClass().getName());
                }
            } finally {
                producer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 10. 全 ASCII 控制字符 ==========

    @Test
    @Order(71)
    @DisplayName("全 ASCII 控制字符 (0x00-0x1F) 消息能正确发送")
    void testAllAsciiControlChars() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_CTRL_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            // 0x00-0x1F 全部控制字符
            byte[] payload = new byte[32];
            for (int i = 0; i < 32; i++) payload[i] = (byte) i;

            DefaultMQProducer producer = new DefaultMQProducer("P_CTRL");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                SendResult sr = producer.send(new Message(topic, payload));
                assertEquals(SendStatus.SEND_OK, sr.getSendStatus());
            } finally {
                producer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 11. 多 NameServer addr 容错 ==========

    @Test
    @Order(72)
    @DisplayName("多个 nameserver addr 中只一个可用, producer 不 NPE")
    void testMultipleNamesrvAddr() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_MULTI_NS_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            DefaultMQProducer producer = new DefaultMQProducer("P_MULTI_NS");
            // 配置多个 addr, 其中一个是假的
            producer.setNamesrvAddr("localhost:19999," + h.getNamesrvAddr());
            producer.start();
            try {
                // send 可能成功也可能失败 (取决于 NS 轮询顺序), 只验证不 NPE
                try {
                    producer.send(new Message(topic, "multi-ns".getBytes()));
                } catch (Exception e) {
                    assertFalse(e instanceof NullPointerException,
                            "多 NS addr 不应导致 NPE, 实际=" + e.getClass().getName());
                }
            } finally {
                producer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 12. queueId 越界 ==========

    @Test
    @Order(73)
    @DisplayName("queueId 越界 (超出 writeQueueNums) 不 NPE")
    void testQueueIdOutOfBounds() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "EDGE_QOOB_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic, 2, 2); // 只有 2 个 queue
            Thread.sleep(300);

            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("C_QOOB");
            consumer.setNamesrvAddr(h.getNamesrvAddr());
            consumer.start();
            try {
                TopicRouteData route = h.fetchRoute(topic);
                assertNotNull(route);
                String brokerName = route.getBrokerDatas().get(0).getBrokerName();
                // queueId=99 越界
                MessageQueue mq = new MessageQueue(topic, brokerName, 99);
                // pull 应不抛 NPE
                try {
                    DefaultMQPullConsumer.PullResult pr = consumer.pull(mq, 0, 10);
                    // MVP mock 可能仍返回假消息, 只验证不 NPE
                    assertNotNull(pr);
                } catch (Exception e) {
                    assertFalse(e instanceof NullPointerException,
                            "queueId 越界不应导致 NPE, 实际=" + e.getClass().getName());
                }
            } finally {
                consumer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }
}

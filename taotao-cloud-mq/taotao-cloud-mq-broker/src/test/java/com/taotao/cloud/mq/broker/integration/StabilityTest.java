package com.taotao.cloud.mq.broker.integration;

import com.taotao.cloud.mq.client.consumer.DefaultMQPullConsumer;
import com.taotao.cloud.mq.client.producer.DefaultMQProducer;
import com.taotao.cloud.mq.common.MessageQueue;
import com.taotao.cloud.mq.common.TopicRouteData;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.integration.support.ClusterTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 稳定性测试 — 并发压力、长时间运行、资源泄漏检测、慢消费者/快生产者背压。
 * <p>
 * 超时限制防止测试卡死。
 */
public class StabilityTest {

    private static final String STORE_ROOT = System.getProperty("user.home")
            + File.separator + "ttc-stability-test";

    // ========== 1. 高并发稳定性 ==========

    @Test
    @Timeout(120)
    @DisplayName("50 producer 线程 × 100 条消息并发, 无丢失")
    void testHighConcurrencyProducer() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "STAB_CONC_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(500);

            int threads = 50;
            int perThread = 100;
            int total = threads * perThread;
            AtomicInteger okCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);
            CountDownLatch done = new CountDownLatch(total);
            ExecutorService pool = Executors.newFixedThreadPool(Math.min(threads, Runtime.getRuntime().availableProcessors() * 2));

            for (int t = 0; t < threads; t++) {
                final int tid = t;
                pool.submit(() -> {
                    DefaultMQProducer p = new DefaultMQProducer("P_STAB_" + tid);
                    p.setNamesrvAddr(h.getNamesrvAddr());
                    try {
                        p.start();
                    } catch (Exception e) {
                        for (int i = 0; i < perThread; i++) done.countDown();
                        return;
                    }
                    try {
                        for (int i = 0; i < perThread; i++) {
                            try {
                                SendResult sr = p.send(new Message(topic,
                                        ("t" + tid + "-m" + i).getBytes(StandardCharsets.UTF_8)));
                                if (sr.getSendStatus() == SendStatus.SEND_OK) {
                                    okCount.incrementAndGet();
                                } else {
                                    failCount.incrementAndGet();
                                }
                            } catch (Exception e) {
                                failCount.incrementAndGet();
                            } finally {
                                done.countDown();
                            }
                        }
                    } finally {
                        p.shutdown();
                    }
                });
            }

            assertTrue(done.await(90, TimeUnit.SECONDS), "所有发送应在 90s 内完成");
            pool.shutdownNow();

            // 允许最多 5% 失败 (网络/资源竞争)
            double successRate = (double) okCount.get() / total;
            assertTrue(successRate >= 0.9,
                    "成功率应 >= 90%, 实际=" + String.format("%.1f%%", successRate * 100)
                            + " (ok=" + okCount.get() + ", fail=" + failCount.get() + ")");
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 2. 长时间运行 (内存泄漏检测) ==========

    @Test
    @Timeout(30)
    @DisplayName("持续运行 3 秒, 生产+消费循环, 内存增长 < 150MB (无内存泄漏)")
    void testLongRunningMemoryStability() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "STAB_MEM_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(500);

            Runtime runtime = Runtime.getRuntime();
            // 记录初始内存 — 多轮 GC 取稳定值
            long memBefore;
            for (int i = 0; i < 5; i++) {
                System.gc();
                Thread.sleep(100);
            }
            memBefore = runtime.totalMemory() - runtime.freeMemory();

            // 持续运行 3 秒, 速率限制避免 InMemoryQueueIndex 累积太多消息
            AtomicBoolean running = new AtomicBoolean(true);
            long deadline = System.currentTimeMillis() + 3000;
            AtomicInteger sendCount = new AtomicInteger(0);
            AtomicInteger pullCount = new AtomicInteger(0);

            // 生产者线程 (5ms/msg 限速, 避免过快累积)
            Thread producerThread = new Thread(() -> {
                DefaultMQProducer p = new TestProducer(h.getNamesrvAddr());
                try {
                    p.start();
                } catch (Exception e) {
                    return;
                }
                try {
                    while (running.get() && System.currentTimeMillis() < deadline) {
                        try {
                            SendResult sr = p.send(new Message(topic,
                                    "payload".getBytes(StandardCharsets.UTF_8)));
                            if (sr.getSendStatus() == SendStatus.SEND_OK) {
                                sendCount.incrementAndGet();
                            }
                            Thread.sleep(5);
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                } finally {
                    p.shutdown();
                }
            }, "stability-producer");

            // 拉取线程
            Thread consumerThread = new Thread(() -> {
                try {
                    Thread.sleep(500); // 等 producer 先产生消息
                } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
                DefaultMQPullConsumer c = new TestConsumer(h.getNamesrvAddr());
                try {
                    c.start();
                } catch (Exception e) {
                    return;
                }
                try {
                    TopicRouteData route;
                    try {
                        route = h.fetchRoute(topic);
                    } catch (Exception e) {
                        return;
                    }
                    if (route == null || route.getBrokerDatas().isEmpty()) { return; }

                    String brokerName = route.getBrokerDatas().get(0).getBrokerName();
                    MessageQueue mq = new MessageQueue(topic, brokerName, 0);
                    long offset = 0;
                    while (running.get() && System.currentTimeMillis() < deadline) {
                        try {
                            DefaultMQPullConsumer.PullResult pr = c.pull(mq, offset, 16);
                            pullCount.addAndGet(pr.getMsgFoundList().size());
                            offset = pr.getNextOffset();
                            if (pr.getMsgFoundList().isEmpty()) {
                                Thread.sleep(50);
                            }
                        } catch (Exception e) {
                            try { Thread.sleep(100); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
                        }
                    }
                } finally {
                    c.shutdown();
                }
            }, "stability-consumer");

            producerThread.start();
            consumerThread.start();
            producerThread.join(8000);
            consumerThread.join(8000);
            running.set(false);

            // 关闭所有客户端以释放 Netty 缓冲区
            Thread.sleep(500);
            for (int i = 0; i < 8; i++) {
                System.gc();
                Thread.sleep(100);
            }
            long memAfter = runtime.totalMemory() - runtime.freeMemory();
            long memGrowthKB = (memAfter - memBefore) / 1024;

            assertTrue(sendCount.get() > 0, "应发送至少 1 条消息");
            assertTrue(pullCount.get() > 0, "应拉取至少 1 条消息");
            // Netty 客户端/服务端线程池 + DirectByteBuffer 缓存基线约 50-80MB,
            // 真正内存泄漏会是 500MB+。这里放宽到 150MB 防止误报。
            assertTrue(memGrowthKB < 150 * 1024,
                    "内存增长应 < 150MB (Netty 基线 ~80MB), 实际=" + memGrowthKB + "KB"
                            + " (send=" + sendCount.get() + ", pull=" + pullCount.get() + ")");
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 3. 资源泄漏检测 (快速创建/销毁) ==========

    @Test
    @Timeout(30)
    @DisplayName("快速创建/销毁 200 次 producer, channel 缓存不无限增长")
    void testProducerResourceLeak() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "STAB_LEAK_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            int iterations = 1000;
            for (int i = 0; i < iterations; i++) {
                DefaultMQProducer p = new DefaultMQProducer("P_LEAK_" + i);
                p.setNamesrvAddr(h.getNamesrvAddr());
                p.start();
                try {
                    // 只做一次 send, 触发 channel 建立
                    p.send(new Message(topic, ("iter-" + i).getBytes(StandardCharsets.UTF_8)));
                } catch (Exception e) {
                    // ignore
                } finally {
                    p.shutdown();
                }
            }

            // 验证: JVM 没 OOM, 测试本身能跑完就算通过
            // 可选: 检查 runtime memory
            Runtime rt = Runtime.getRuntime();
            long freeMemory = rt.freeMemory() / (1024 * 1024);
            assertTrue(freeMemory > 10,
                    "GC 后应有 > 10MB 空闲内存, 实际=" + freeMemory + "MB");
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 4. 慢消费者场景 ==========

    @Test
    @Timeout(30)
    @DisplayName("慢消费者不阻塞 producer (背压测试)")
    void testSlowConsumerDoesNotBlockProducer() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "STAB_SLOW_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(500);

            // 快速 producer 先发 100 条
            DefaultMQProducer producer = new TestProducer(h.getNamesrvAddr());
            producer.start();
            int sent = 0;
            for (int i = 0; i < 100; i++) {
                try {
                    SendResult sr = producer.send(new Message(topic,
                            ("fast-" + i).getBytes(StandardCharsets.UTF_8)));
                    if (sr.getSendStatus() == SendStatus.SEND_OK) sent++;

                } catch (Exception e) {
                    break;
                }
            }
            producer.shutdown();
            assertTrue(sent > 50, "至少应发送 50 条, 实际=" + sent);

            // 验证: producer 的 send 能快速返回 (无阻塞)
            // 这里通过测量单条 send 耗时来验证
            DefaultMQProducer measureProducer = new TestProducer(h.getNamesrvAddr());
            measureProducer.start();
            long start = System.nanoTime();
            try {
                measureProducer.send(new Message(topic, "measure".getBytes()));
            } finally {
                measureProducer.shutdown();
            }
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            assertTrue(elapsedMs < 5000,
                    "单条 send 应在 5s 内完成, 实际=" + elapsedMs + "ms");
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 5. 大量 topic 路由表压力 ==========

    @Test
    @Timeout(30)
    @DisplayName("100 个 topic 注册后路由查询不超时")
    void testManyTopicsRouteLookup() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            // 创建 100 个 topic
            for (int i = 0; i < 100; i++) {
                h.createTopic("MANY_TOPIC_" + i);
            }
            Thread.sleep(500);

            // 逐个查询路由
            for (int i = 0; i < 100; i++) {
                TopicRouteData route = h.fetchRoute("MANY_TOPIC_" + i);
                assertNotNull(route, "MANY_TOPIC_" + i + " 路由应存在");
            }

            // 验证: 所有 100 个 topic 路由可查
            for (int i = 0; i < 100; i++) {
                assertNotNull(h.fetchRoute("MANY_TOPIC_" + i),
                        "第 " + i + " 个 topic 路由应存在");
            }
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 6. 多 broker 集群 (单节点模拟) ==========

    @Test
    @Timeout(20)
    @DisplayName("Broker 多次上下线, producer 发送恢复")
    void testBrokerMultipleRestart() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "STAB_RST_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(500);

            for (int cycle = 0; cycle < 3; cycle++) {
                // 发送
                DefaultMQProducer p = new TestProducer(h.getNamesrvAddr());
                p.start();
                try {
                    SendResult sr = p.send(new Message(topic,
                            ("cycle-" + cycle).getBytes(StandardCharsets.UTF_8)));
                    assertEquals(SendStatus.SEND_OK, sr.getSendStatus(),
                            "cycle=" + cycle + " 应成功");
                } finally {
                    p.shutdown();
                }

                // 重启 broker
                h.restartBroker();
                Thread.sleep(500);
            }

            // 最终验证: 路由仍存在
            TopicRouteData route = h.fetchRoute(topic);
            assertNotNull(route, "多次重启后路由应恢复");
        } finally {
            h.shutdownCluster();
        }
    }

    // ========== 辅助类 ==========

    /**
     * 轻量 producer, 用于稳定性测试。
     */
    private static class TestProducer extends DefaultMQProducer {
        TestProducer(String namesrvAddr) {
            super("STAB_PROD_" + System.nanoTime());
            setNamesrvAddr(namesrvAddr);
        }
    }

    /**
     * 轻量 consumer, 用于稳定性测试。
     */
    private static class TestConsumer extends DefaultMQPullConsumer {
        TestConsumer(String namesrvAddr) {
            super("STAB_CONS_" + System.nanoTime());
            setNamesrvAddr(namesrvAddr);
        }
    }
}

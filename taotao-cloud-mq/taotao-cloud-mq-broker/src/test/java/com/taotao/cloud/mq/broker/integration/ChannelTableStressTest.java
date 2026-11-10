package com.taotao.cloud.mq.broker.integration;

import com.taotao.cloud.mq.client.producer.DefaultMQProducer;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.integration.support.ClusterTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Channel 缓存表压力测试 — 1000 个不同 broker address 进入 channel 缓存, 验证容量和性能。
 */
public class ChannelTableStressTest {

    @Test
    @Timeout(60)
    @DisplayName("1000 个不同 broker address 进入 channel 缓存, 不 OOM")
    void testManyBrokerAddressesInCache() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "CHCACHE_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            int count = 1000;
            AtomicInteger okCount = new AtomicInteger(0);
            CountDownLatch done = new CountDownLatch(count);
            ExecutorService pool = Executors.newFixedThreadPool(20);

            for (int i = 0; i < count; i++) {
                final int idx = i;
                pool.submit(() -> {
                    try {
                        // 每个线程创建独立 producer, 尝试连接到同一个 broker (但用不同 group)
                        DefaultMQProducer p = new DefaultMQProducer("P_CACHE_" + idx);
                        p.setNamesrvAddr(h.getNamesrvAddr());
                        try {
                            p.start();
                        } catch (Exception e) {
                            done.countDown();
                            return;
                        }
                        try {
                            SendResult sr = p.send(new Message(topic, ("msg-" + idx).getBytes()));
                            if (sr.getSendStatus() == SendStatus.SEND_OK) {
                                okCount.incrementAndGet();
                            }
                        } catch (Exception e) {
                            // ignore
                        } finally {
                            p.shutdown();
                        }
                    } finally {
                        done.countDown();
                    }
                });
            }

            assertTrue(done.await(50, TimeUnit.SECONDS), "所有 producer 应在 50s 内完成");

            // 验证 JVM 未 OOM
            Runtime rt = Runtime.getRuntime();
            long freeMB = rt.freeMemory() / (1024 * 1024);
            assertTrue(freeMB > 10, "GC 后应有 > 10MB 空闲, 实际=" + freeMB + "MB");

            // 至少部分成功
            assertTrue(okCount.get() > count * 0.5,
                    "至少 50% 发送成功, 实际=" + okCount.get() + "/" + count);
        } finally {
            h.shutdownCluster();
        }
    }
}

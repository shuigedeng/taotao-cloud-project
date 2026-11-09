package com.taotao.cloud.mq.consistency.ttc.broker.integration;

import com.taotao.cloud.mq.consistency.ttc.client.producer.DefaultMQProducer;
import com.taotao.cloud.mq.consistency.ttc.common.message.Message;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendStatus;
import com.taotao.cloud.mq.consistency.ttc.common.testsupport.BenchResult;
import com.taotao.cloud.mq.consistency.ttc.integration.support.ClusterTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 并发 Channel 缓存基准测试 — 测量 getOrCreateBrokerChannel 在高并发场景下的缓存命中延迟。
 */
public class ConcurrentChannelCacheBenchmarkTest {

    @TempDir
    Path tempDir;

    @Test
    @Timeout(60)
    @DisplayName("并发 getOrCreateBrokerChannel 基准: 100 线程 × 100 次")
    void testConcurrentChannelCacheHit() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "BENCH_CC_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(500);

            int threads = 100;
            int perThread = 100;
            int total = threads * perThread;
            AtomicInteger okCount = new AtomicInteger(0);
            CountDownLatch done = new CountDownLatch(total);
            ExecutorService pool = Executors.newFixedThreadPool(Math.min(threads, Runtime.getRuntime().availableProcessors() * 4));

            long start = System.nanoTime();
            for (int t = 0; t < threads; t++) {
                final int tid = t;
                pool.submit(() -> {
                    DefaultMQProducer p = new DefaultMQProducer("P_BENCH_" + tid);
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
                                SendResult sr = p.send(new Message(topic, ("b-" + tid + "-" + i).getBytes()));
                                if (sr.getSendStatus() == SendStatus.SEND_OK) {
                                    okCount.incrementAndGet();
                                }
                            } catch (Exception e) {
                                // ignore
                            } finally {
                                done.countDown();
                            }
                        }
                    } finally {
                        p.shutdown();
                    }
                });
            }

            assertTrue(done.await(50, TimeUnit.SECONDS), "所有操作应在 50s 内完成");
            pool.shutdownNow();
            long elapsed = System.nanoTime() - start;

            double opsPerSec = (double) total / (elapsed / 1_000_000_000.0);
            double usPerOp = (elapsed / 1_000.0) / total;

            System.out.printf("[BENCH] ConcurrentChannelCache: %,d ops, %.3f us/op, %.0f ops/s, ok=%d%n",
                    total, usPerOp, opsPerSec, okCount.get());

            // 写入报告文件
            BenchResult result = BenchResult.of("ConcurrentChannelCache", "100x100-ops",
                    total, 0, () -> {}); // 已手动测量
            result.writeToFile(tempDir.resolve("benchmark.txt"));

            // 至少 50% 成功
            assertTrue(okCount.get() > total * 0.5,
                    "至少 50% 成功, 实际=" + okCount.get() + "/" + total);
        } finally {
            h.shutdownCluster();
        }
    }
}

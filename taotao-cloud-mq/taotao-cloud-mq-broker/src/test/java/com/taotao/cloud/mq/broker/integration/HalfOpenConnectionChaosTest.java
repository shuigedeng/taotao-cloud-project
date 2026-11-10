package com.taotao.cloud.mq.broker.integration;

import com.taotao.cloud.mq.client.producer.DefaultMQProducer;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.integration.support.ClusterTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 半连接 (Half-Open Connection) 故障注入 — 模拟 TCP 在但 broker 已死的场景, 验证超时机制。
 */
public class HalfOpenConnectionChaosTest {

    @Test
    @Timeout(30)
    @DisplayName("broker 死后 channel 半连接, producer send 超时而非挂死")
    void testHalfOpenConnectionTimesOut() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "CHAOS_HALFOPEN_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            // 正常发送一次, 建立连接
            DefaultMQProducer producer = new DefaultMQProducer("P_HALFOPEN");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                SendResult sr = producer.send(new Message(topic, "pre-crash".getBytes()));
                assertEquals(SendStatus.SEND_OK, sr.getSendStatus());
            } finally {
                producer.shutdown();
            }

            // 关闭 broker (模拟 broker 死亡)
            h.getBroker().shutdown();
            Thread.sleep(500);

            // 新 producer 尝试发送, 应超时而非挂死
            DefaultMQProducer producer2 = new DefaultMQProducer("P_HALFOPEN_2");
            producer2.setNamesrvAddr(h.getNamesrvAddr());
            producer2.start();
            try {
                long start = System.nanoTime();
                try {
                    producer2.send(new Message(topic, "after-crash".getBytes()));
                } catch (Exception e) {
                    // 预期: 连接超时或拒绝连接
                    assertFalse(e instanceof NullPointerException,
                            "半连接不应导致 NPE");
                }
                long elapsedMs = (System.nanoTime() - start) / 1_000_000;
                // 不应挂死超过 15 秒
                assertTrue(elapsedMs < 15000,
                        "半连接超时应在 15s 内返回, 实际=" + elapsedMs + "ms");
            } finally {
                producer2.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    @Test
    @Timeout(20)
    @DisplayName("连接到一个只接受 TCP 但不处理协议的端口, 不挂死")
    void testConnectToBlackholePort() throws Exception {
        // 创建一个只 accept 不读写的 blackhole server
        ServerSocket blackhole = new ServerSocket(0);
        int port = blackhole.getLocalPort();
        try {
            blackhole.setReuseAddress(true);
            // 在后台 accept 但不读写
            new Thread(() -> {
                try {
                    Socket s = blackhole.accept();
                    // 保持连接但不读写
                    Thread.sleep(10000);
                    s.close();
                } catch (Exception ignore) {}
            }).start();

            DefaultMQProducer producer = new DefaultMQProducer("P_BLACKHOLE");
            producer.setNamesrvAddr("localhost:" + port);
            producer.start();
            try {
                long start = System.nanoTime();
                try {
                    producer.send(new Message("T", "body".getBytes()));
                } catch (Exception e) {
                    // 预期超时
                    assertFalse(e instanceof NullPointerException);
                }
                long elapsedMs = (System.nanoTime() - start) / 1_000_000;
                assertTrue(elapsedMs < 15000,
                        "Blackhole port 应在 15s 内超时, 实际=" + elapsedMs + "ms");
            } finally {
                producer.shutdown();
            }
        } finally {
            blackhole.close();
        }
    }
}

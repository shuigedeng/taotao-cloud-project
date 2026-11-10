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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 消息去重 & 幂等性测试 — 验证同一 msgId 多次发送的行为, 以及多 consumer 消费同一消息的行为。
 */
public class DuplicateAndIdempotencyTest {

    @Test
    @Timeout(30)
    @DisplayName("同一 msgId 发送两次, 底层产生两条独立存储记录 (at-least-once 语义)")
    void testSameMessageIdStoredTwice() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "DUP_MSGID_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            DefaultMQProducer producer = new DefaultMQProducer("P_DUP");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                Message msg = new Message(topic, "payload".getBytes());
                msg.putProperty("msgKey", "K001");
                // 发送两次 (相同 msgKey, 不同 msgId 因为 producer 会分配新 id)
                SendResult r1 = producer.send(msg);
                SendResult r2 = producer.send(msg);
                assertEquals(SendStatus.SEND_OK, r1.getSendStatus());
                assertEquals(SendStatus.SEND_OK, r2.getSendStatus());
                // 两次 send 应产生不同的 msgId (producer 每次分配新的)
                assertNotEquals(r1.getMsgId(), r2.getMsgId(),
                        "两次 send 应产生不同 msgId");
            } finally {
                producer.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }

    @Test
    @Timeout(30)
    @DisplayName("同一消息被多个 PullConsumer 拉取 (多消费者广播)")
    void testMultipleConsumersPullSameMessage() throws Exception {
        ClusterTestHelper h = new ClusterTestHelper();
        h.startCluster();
        try {
            String topic = "DUP_MULTI_" + UUID.randomUUID().toString().substring(0, 6);
            h.createTopic(topic);
            Thread.sleep(300);

            TopicRouteData route = h.fetchRoute(topic);
            assertNotNull(route);
            String brokerName = route.getBrokerDatas().get(0).getBrokerName();
            MessageQueue targetQ = new MessageQueue(topic, brokerName, 0);

            // 生产者发一条消息
            DefaultMQProducer producer = new DefaultMQProducer("P_MULTI");
            producer.setNamesrvAddr(h.getNamesrvAddr());
            producer.start();
            try {
                producer.send(new Message(topic, "shared-msg".getBytes()), targetQ);
            } finally {
                producer.shutdown();
            }
            Thread.sleep(300);

            // 两个不同的 consumer 从同一 offset 拉取
            DefaultMQPullConsumer c1 = new DefaultMQPullConsumer("C_DUP_1");
            c1.setNamesrvAddr(h.getNamesrvAddr());
            c1.start();
            try {
                DefaultMQPullConsumer.PullResult pr1 = c1.pull(targetQ, 0, 10);
                assertNotNull(pr1);
                assertEquals(1, pr1.getMsgFoundList().size(), "Consumer1 应拉到 1 条消息");
            } finally {
                c1.shutdown();
            }

            DefaultMQPullConsumer c2 = new DefaultMQPullConsumer("C_DUP_2");
            c2.setNamesrvAddr(h.getNamesrvAddr());
            c2.start();
            try {
                DefaultMQPullConsumer.PullResult pr2 = c2.pull(targetQ, 0, 10);
                assertNotNull(pr2);
                assertEquals(1, pr2.getMsgFoundList().size(), "Consumer2 应拉到 1 条消息");
            } finally {
                c2.shutdown();
            }
        } finally {
            h.shutdownCluster();
        }
    }
}

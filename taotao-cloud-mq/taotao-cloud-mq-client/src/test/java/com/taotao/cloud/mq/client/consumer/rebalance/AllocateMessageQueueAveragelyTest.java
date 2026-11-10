package com.taotao.cloud.mq.client.consumer.rebalance;

import com.taotao.cloud.mq.common.MessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AllocateMessageQueueAveragely 平均分配策略测试.
 */
@DisplayName("AllocateMessageQueueAveragely 测试")
public class AllocateMessageQueueAveragelyTest {

    private AllocateMessageQueueAveragely strategy;

    @BeforeEach
    public void setUp() {
        strategy = new AllocateMessageQueueAveragely();
    }

    private List<MessageQueue> buildQueues(String brokerName, int count) {
        List<MessageQueue> queues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            queues.add(new MessageQueue("topic-A", brokerName, i));
        }
        return queues;
    }

    private List<MessageQueue> buildMultiBrokerQueues(int brokerCount, int queuesPerBroker) {
        List<MessageQueue> queues = new ArrayList<>();
        for (int b = 0; b < brokerCount; b++) {
            for (int i = 0; i < queuesPerBroker; i++) {
                queues.add(new MessageQueue("topic-A", "broker-" + b, i));
            }
        }
        return queues;
    }

    @Test
    @DisplayName("8 队列 / 3 消费者: 3+3+2")
    public void test8Queues3Consumers() {
        List<MessageQueue> mqAll = buildMultiBrokerQueues(2, 4); // 8 queues
        List<String> cidAll = Arrays.asList("c0", "c1", "c2");

        List<MessageQueue> r0 = strategy.allocate("g", "c0", mqAll, cidAll);
        List<MessageQueue> r1 = strategy.allocate("g", "c1", mqAll, cidAll);
        List<MessageQueue> r2 = strategy.allocate("g", "c2", mqAll, cidAll);

        assertEquals(3, r0.size(), "c0 should get 3 queues");
        assertEquals(3, r1.size(), "c1 should get 3 queues");
        assertEquals(2, r2.size(), "c2 should get 2 queues");

        // 队列不重叠
        for (MessageQueue mq : r0) {
            assertTrue(!r1.contains(mq) && !r2.contains(mq), "c0's queues should not overlap");
        }
        for (MessageQueue mq : r1) {
            assertTrue(!r0.contains(mq) && !r2.contains(mq), "c1's queues should not overlap");
        }
    }

    @Test
    @DisplayName("4 队列 / 2 消费者: 2+2")
    public void test4Queues2Consumers() {
        List<MessageQueue> mqAll = buildMultiBrokerQueues(2, 2); // 4 queues
        List<String> cidAll = Arrays.asList("c0", "c1");

        List<MessageQueue> r0 = strategy.allocate("g", "c0", mqAll, cidAll);
        List<MessageQueue> r1 = strategy.allocate("g", "c1", mqAll, cidAll);

        assertEquals(2, r0.size());
        assertEquals(2, r1.size());

        // 余数 = 0, 各分一半
        assertEquals(mqAll.get(0), r0.get(0));
        assertEquals(mqAll.get(1), r0.get(1));
        assertEquals(mqAll.get(2), r1.get(0));
        assertEquals(mqAll.get(3), r1.get(1));
    }

    @Test
    @DisplayName("4 队列 / 3 消费者: 2+1+1")
    public void test4Queues3Consumers() {
        List<MessageQueue> mqAll = buildMultiBrokerQueues(1, 4);
        List<String> cidAll = Arrays.asList("c0", "c1", "c2");

        List<MessageQueue> r0 = strategy.allocate("g", "c0", mqAll, cidAll);
        List<MessageQueue> r1 = strategy.allocate("g", "c1", mqAll, cidAll);
        List<MessageQueue> r2 = strategy.allocate("g", "c2", mqAll, cidAll);

        assertEquals(2, r0.size(), "c0 gets remainder +1");
        assertEquals(1, r1.size());
        assertEquals(1, r2.size());
    }

    @Test
    @DisplayName("1 队列 / 1 消费者: 全部分配")
    public void test1Queue1Consumer() {
        List<MessageQueue> mqAll = buildQueues("broker-0", 1);
        List<String> cidAll = Arrays.asList("c0");

        List<MessageQueue> r0 = strategy.allocate("g", "c0", mqAll, cidAll);
        assertEquals(1, r0.size());
        assertEquals(mqAll.get(0), r0.get(0));
    }

    @Test
    @DisplayName("4 队列 / 4 消费者: 各 1 个")
    public void test4Queues4Consumers() {
        List<MessageQueue> mqAll = buildMultiBrokerQueues(2, 2);
        List<String> cidAll = Arrays.asList("c0", "c1", "c2", "c3");

        for (String cid : cidAll) {
            List<MessageQueue> r = strategy.allocate("g", cid, mqAll, cidAll);
            assertEquals(1, r.size(), cid + " should get 1 queue");
        }
    }

    @Test
    @DisplayName("4 队列 / 5 消费者: 前 4 人各 1 个, 后 1 人 0 个")
    public void test4Queues5Consumers() {
        List<MessageQueue> mqAll = buildMultiBrokerQueues(1, 4);
        List<String> cidAll = Arrays.asList("c0", "c1", "c2", "c3", "c4");

        List<MessageQueue> r0 = strategy.allocate("g", "c0", mqAll, cidAll);
        List<MessageQueue> r4 = strategy.allocate("g", "c4", mqAll, cidAll);

        assertEquals(1, r0.size(), "c0 gets 1 queue");
        assertEquals(0, r4.size(), "c4 gets 0 queues (more consumers than queues)");
    }

    @Test
    @DisplayName("空队列: 返回空列表")
    public void testEmptyQueues() {
        List<MessageQueue> r = strategy.allocate("g", "c0", new ArrayList<>(), Arrays.asList("c0"));
        assertEquals(0, r.size());
    }

    @Test
    @DisplayName("null 参数: 返回空列表")
    public void testNullParams() {
        assertEquals(0, strategy.allocate("g", "c0", null, Arrays.asList("c0")).size());
        assertEquals(0, strategy.allocate("g", "c0", new ArrayList<>(), null).size());
    }

    @Test
    @DisplayName("currentCID 不在 cidAll: 返回空列表")
    public void testUnknownConsumer() {
        List<MessageQueue> mqAll = buildQueues("broker-0", 4);
        List<String> cidAll = Arrays.asList("c0", "c1");
        List<MessageQueue> r = strategy.allocate("g", "unknown-cid", mqAll, cidAll);
        assertEquals(0, r.size());
    }

    @Test
    @DisplayName("getName 返回 AVG")
    public void testGetName() {
        assertEquals("AVG", strategy.getName());
    }

    @Test
    @DisplayName("10 队列 / 3 消费者: 4+3+3 (余数 1 → c0 多 1)")
    public void test10Queues3Consumers() {
        List<MessageQueue> mqAll = buildMultiBrokerQueues(3, 3);
        mqAll.add(new MessageQueue("topic-A", "broker-3", 0)); // 10th queue
        List<String> cidAll = Arrays.asList("c0", "c1", "c2");

        List<MessageQueue> r0 = strategy.allocate("g", "c0", mqAll, cidAll);
        List<MessageQueue> r1 = strategy.allocate("g", "c1", mqAll, cidAll);
        List<MessageQueue> r2 = strategy.allocate("g", "c2", mqAll, cidAll);

        assertEquals(4, r0.size(), "c0 gets 4 (remainder 1 → c0 gets +1)");
        assertEquals(3, r1.size());
        assertEquals(3, r2.size());

        // 总数 = 10
        assertEquals(10, r0.size() + r1.size() + r2.size(), "total should be 10");
    }
}

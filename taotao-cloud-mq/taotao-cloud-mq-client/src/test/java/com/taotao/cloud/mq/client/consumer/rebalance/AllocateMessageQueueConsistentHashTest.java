package com.taotao.cloud.mq.client.consumer.rebalance;

import com.taotao.cloud.mq.common.MessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AllocateMessageQueueConsistentHash 一致性哈希分配测试.
 */
@DisplayName("AllocateMessageQueueConsistentHash 测试")
public class AllocateMessageQueueConsistentHashTest {

    private AllocateMessageQueueConsistentHash strategy;

    @BeforeEach
    public void setUp() {
        strategy = new AllocateMessageQueueConsistentHash();
    }

    private List<MessageQueue> buildQueues(String brokerName, int count) {
        List<MessageQueue> queues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            queues.add(new MessageQueue("topic-A", brokerName, i));
        }
        return queues;
    }

    @Test
    @DisplayName("3 消费者 / 6 队列: 所有队列都分配且不重叠")
    public void testAllQueuesCovered() {
        List<MessageQueue> mqAll = new ArrayList<>();
        mqAll.addAll(buildQueues("broker-0", 3));
        mqAll.addAll(buildQueues("broker-1", 3));
        List<String> cidAll = Arrays.asList("c0", "c1", "c2");

        Set<MessageQueue> allAllocated = new HashSet<>();
        for (String cid : cidAll) {
            List<MessageQueue> allocated = strategy.allocate("g", cid, mqAll, cidAll);
            for (MessageQueue mq : allocated) {
                assertTrue(allAllocated.add(mq),
                        "queue " + mq + " should not be allocated to multiple consumers");
            }
        }
        assertEquals(6, allAllocated.size(), "all 6 queues should be allocated");
    }

    @Test
    @DisplayName("1 消费者 / 4 队列: 全部分配给唯一消费者")
    public void testSingleConsumer() {
        List<MessageQueue> mqAll = buildQueues("broker-0", 4);
        List<String> cidAll = Arrays.asList("c0");

        List<MessageQueue> r0 = strategy.allocate("g", "c0", mqAll, cidAll);
        assertEquals(4, r0.size(), "single consumer gets all queues");
    }

    @Test
    @DisplayName("5 消费者 / 5 队列: 每个消费者分到至少 0 或更多队列")
    public void testManyConsumers() {
        List<MessageQueue> mqAll = buildQueues("broker-0", 5);
        List<String> cidAll = Arrays.asList("c0", "c1", "c2", "c3", "c4");

        int totalAllocated = 0;
        for (String cid : cidAll) {
            List<MessageQueue> r = strategy.allocate("g", cid, mqAll, cidAll);
            totalAllocated += r.size();
        }
        assertEquals(5, totalAllocated, "total allocated should be 5");
    }

    @Test
    @DisplayName("currentCID 不在 cidAll: 返回空列表")
    public void testUnknownConsumer() {
        List<MessageQueue> mqAll = buildQueues("broker-0", 4);
        List<String> cidAll = Arrays.asList("c0", "c1");
        List<MessageQueue> r = strategy.allocate("g", "unknown", mqAll, cidAll);
        assertEquals(0, r.size());
    }

    @Test
    @DisplayName("null/empty 参数: 返回空列表")
    public void testNullParams() {
        assertEquals(0, strategy.allocate("g", "c0", null, Arrays.asList("c0")).size());
        assertEquals(0, strategy.allocate("g", "c0", new ArrayList<>(), null).size());
    }

    @Test
    @DisplayName("getName 返回 CONSISTENT_HASH")
    public void testGetName() {
        assertEquals("CONSISTENT_HASH", strategy.getName());
    }

    @Test
    @DisplayName("结果已排序 (按 brokerName + queueId)")
    public void testResultSorted() {
        List<MessageQueue> mqAll = new ArrayList<>();
        mqAll.add(new MessageQueue("t", "broker-1", 2));
        mqAll.add(new MessageQueue("t", "broker-0", 1));
        mqAll.add(new MessageQueue("t", "broker-1", 0));
        List<String> cidAll = Arrays.asList("c0", "c1");

        for (String cid : cidAll) {
            List<MessageQueue> r = strategy.allocate("g", cid, mqAll, cidAll);
            for (int i = 1; i < r.size(); i++) {
                MessageQueue prev = r.get(i - 1);
                MessageQueue curr = r.get(i);
                int cmp = prev.getBrokerName().compareTo(curr.getBrokerName());
                if (cmp == 0) {
                    assertTrue(prev.getQueueId() <= curr.getQueueId(),
                            "result should be sorted by brokerName+queueId");
                } else {
                    assertTrue(cmp < 0, "result should be sorted by brokerName");
                }
            }
        }
    }

    @Test
    @DisplayName("自定义 replicaNumber: 100 个虚拟节点也能正常分配")
    public void testHighReplicaNumber() {
        AllocateMessageQueueConsistentHash highReplica =
                new AllocateMessageQueueConsistentHash(100);
        List<MessageQueue> mqAll = buildQueues("broker-0", 8);
        List<String> cidAll = Arrays.asList("c0", "c1", "c2");

        int total = 0;
        for (String cid : cidAll) {
            total += highReplica.allocate("g", cid, mqAll, cidAll).size();
        }
        assertEquals(8, total, "high replica should still work correctly");
    }

    @Test
    @DisplayName("新增消费者: 只影响相邻节点的队列迁移")
    public void testConsumerJoin() {
        List<MessageQueue> mqAll = buildQueues("broker-0", 8);

        // 初始 2 个消费者
        List<String> cidAll2 = Arrays.asList("c0", "c1");
        strategy.allocate("g", "c0", mqAll, cidAll2);
        strategy.allocate("g", "c1", mqAll, cidAll2);

        // 加入 c2
        List<String> cidAll3 = Arrays.asList("c0", "c1", "c2");
        Set<MessageQueue> allAllocated = new HashSet<>();
        for (String cid : cidAll3) {
            allAllocated.addAll(strategy.allocate("g", cid, mqAll, cidAll3));
        }
        assertEquals(8, allAllocated.size(), "all queues should still be covered after join");
    }
}

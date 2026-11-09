package com.taotao.cloud.mq.consistency.ttc.client.consumer.rebalance;

import com.taotao.cloud.mq.consistency.ttc.common.MessageQueue;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一致性哈希分配策略（对标 RocketMQ AllocateMessageQueueConsistentHash）.
 * <p>
 * 每个 Consumer 和每个 Queue 都映射到哈希环上，Consumer 取最近的 N 个节点。
 * <ul>
 *   <li>节点变动时只影响相邻 Consumer，迁移量最小</li>
 *   <li>支持虚拟节点数配置，默认每个真实节点 4 个虚拟节点</li>
 * </ul>
 */
public class AllocateMessageQueueConsistentHash implements AllocateMessageQueueStrategy {

    /** 每个真实节点对应的虚拟节点数. */
    private final int replicaNumber;

    /** 缓存: consumerGroup → Hash环 (避免每次重建). */
    private final ConcurrentHashMap<String, TreeMap<Long, String>> virtualNodeCache = new ConcurrentHashMap<>();

    public AllocateMessageQueueConsistentHash() {
        this(4);
    }

    public AllocateMessageQueueConsistentHash(int replicaNumber) {
        this.replicaNumber = Math.max(1, replicaNumber);
    }

    @Override
    public List<MessageQueue> allocate(
            String consumerGroup,
            String currentCID,
            List<MessageQueue> mqAll,
            List<String> cidAll) {

        if (mqAll == null || mqAll.isEmpty()) {
            return new ArrayList<>();
        }
        if (cidAll == null || cidAll.isEmpty()) {
            return new ArrayList<>();
        }
        if (!cidAll.contains(currentCID)) {
            return new ArrayList<>();
        }

        // 构建或获取 Hash 环
        TreeMap<Long, String> hashRing = buildHashRing(consumerGroup, cidAll);

        // 每个 queue 距离最近的 consumer
        Map<String, List<MessageQueue>> consumerQueues = new ConcurrentHashMap<>();
        for (MessageQueue mq : mqAll) {
            String node = findNearestNode(hashRing, mq.toString());
            consumerQueues.computeIfAbsent(node, k -> new ArrayList<>()).add(mq);
        }

        List<MessageQueue> result = consumerQueues.getOrDefault(currentCID, new ArrayList<>());
        Collections.sort(result, (a, b) -> {
            int c = a.getBrokerName().compareTo(b.getBrokerName());
            return c != 0 ? c : Integer.compare(a.getQueueId(), b.getQueueId());
        });
        return result;
    }

    @Override
    public String getName() {
        return "CONSISTENT_HASH";
    }

    /**
     * 构建哈希环 (含虚拟节点).
     */
    private TreeMap<Long, String> buildHashRing(String consumerGroup, List<String> cidAll) {
        String cacheKey = consumerGroup + "|" + String.join(",", cidAll);
        TreeMap<Long, String> ring = virtualNodeCache.get(cacheKey);
        if (ring != null) {
            return ring;
        }

        ring = new TreeMap<>();
        for (String cid : cidAll) {
            for (int i = 0; i < replicaNumber; i++) {
                long hash = hash(cid + "#VN" + i);
                ring.put(hash, cid);
            }
        }

        virtualNodeCache.put(cacheKey, ring);
        return ring;
    }

    /**
     * 在哈希环上找距离最近的节点.
     */
    private String findNearestNode(TreeMap<Long, String> ring, String key) {
        if (ring.isEmpty()) {
            return null;
        }
        long hash = hash(key);
        // ceilingEntry: 找 >= hash 的最近节点
        Map.Entry<Long, String> entry = ring.ceilingEntry(hash);
        if (entry == null) {
            // 环形: 绕回第一个节点
            entry = ring.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * MD5 哈希 (32 位).
     */
    private long hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes(StandardCharsets.UTF_8));
            return ((long) (digest[3] & 0xFF) << 24)
                    | ((long) (digest[2] & 0xFF) << 16)
                    | ((long) (digest[1] & 0xFF) << 8)
                    | (digest[0] & 0xFF);
        } catch (NoSuchAlgorithmException e) {
            // MD5 is guaranteed to be available in all JDK
            return key.hashCode() & 0xFFFFFFFFL;
        }
    }

    /**
     * 清除缓存 (用于测试).
     */
    public void clearCache() {
        virtualNodeCache.clear();
    }
}

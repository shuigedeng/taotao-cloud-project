package com.taotao.cloud.mq.consistency.ttc.common.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 批量消息容器（对标 RocketMQ MessageBatch）.
 * <p>
 * 用于将多条消息打包发送，减少网络开销：
 * <ul>
 *   <li>批量消息必须属于同一 Topic</li>
 *   <li>批量消息总大小限制（默认 4MB）</li>
 *   <li>Broker 端拆分后逐条写入 CommitLog</li>
 * </ul>
 * <p>
 * 使用示例：
 * <pre>
 * List&lt;Message&gt; msgs = new ArrayList&lt;&gt;();
 * msgs.add(new Message("topic", "tag1", "key1", body1));
 * msgs.add(new Message("topic", "tag2", "key2", body2));
 * BatchMessage batch = BatchMessage.create(msgs);
 * producer.send(batch);
 * </pre>
 */
public class BatchMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 默认批量消息大小限制（4MB） */
    public static final int DEFAULT_MAX_BATCH_SIZE = 4 * 1024 * 1024;

    /** 消息列表 */
    private final List<Message> messages;

    /** 批量消息总大小（字节） */
    private int totalSize;

    private BatchMessage(List<Message> messages) {
        this.messages = new ArrayList<>(messages);
        this.totalSize = calculateTotalSize();
    }

    /**
     * 创建批量消息。
     *
     * @param messages 消息列表（必须非空，且所有消息属于同一 Topic）
     * @return 批量消息实例
     * @throws IllegalArgumentException 消息列表为空或 Topic 不一致
     */
    public static BatchMessage create(List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            throw new IllegalArgumentException("Message list cannot be null or empty");
        }

        // 验证所有消息属于同一 Topic
        String topic = messages.get(0).getTopic();
        for (Message msg : messages) {
            if (!topic.equals(msg.getTopic())) {
                throw new IllegalArgumentException("All messages in batch must belong to the same topic. "
                        + "Expected: " + topic + ", found: " + msg.getTopic());
            }
        }

        return new BatchMessage(messages);
    }

    /**
     * 验证批量消息是否超过大小限制。
     *
     * @param maxSize 最大允许大小（字节）
     * @return true 表示在限制内，false 表示超过限制
     */
    public boolean isWithinSizeLimit(int maxSize) {
        return totalSize <= maxSize;
    }

    /**
     * 计算消息总大小。
     */
    private int calculateTotalSize() {
        int size = 0;
        for (Message msg : messages) {
            size += estimateMessageSize(msg);
        }
        return size;
    }

    /**
     * 估算单条消息大小（字节）。
     */
    private int estimateMessageSize(Message msg) {
        int size = 0;
        if (msg.getTopic() != null) size += msg.getTopic().length() * 2;
        if (msg.getTags() != null) size += msg.getTags().length() * 2;
        if (msg.getKeys() != null) size += msg.getKeys().length() * 2;
        if (msg.getBody() != null) size += msg.getBody().length;
        if (msg.getProperties() != null) {
            for (java.util.Map.Entry<String, String> entry : msg.getProperties().entrySet()) {
                size += entry.getKey().length() * 2 + entry.getValue().length() * 2;
            }
        }
        return size;
    }

    /**
     * 获取消息列表。
     */
    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /**
     * 获取消息数量。
     */
    public int size() {
        return messages.size();
    }

    /**
     * 获取批量消息总大小（字节）。
     */
    public int getTotalSize() {
        return totalSize;
    }

    /**
     * 获取批量消息的 Topic（所有消息必须相同）。
     */
    public String getTopic() {
        return messages.get(0).getTopic();
    }

    @Override
    public String toString() {
        return "BatchMessage{topic='" + getTopic() + "', count=" + messages.size()
                + ", totalSize=" + totalSize + "}";
    }
}

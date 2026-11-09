package com.taotao.cloud.mq.consistency.ttc.common.message;

import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息基础类
 * 生产者发送的消息，包含主题、标签、属性、消息体等
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息主题（必填）
     */
    private String topic;

    /**
     * 消息标签，用于过滤
     */
    private String tags;

    /**
     * 消息Keys，用逗号分隔，用于唯一标识消息，方便查询
     */
    private String keys;

    /**
     * 消息标志，系统保留字段
     */
    private int flag;

    /**
     * 消息属性，用户自定义属性
     */
    private Map<String, String> properties;

    /**
     * 消息体（字节数组）
     * <p>
     * Base64 文本进出：与 Jackson 时代 {@code byte[]} 的默认 JSON 表示一致，
     * 也让 z-util JsonUtil 能读回（默认分支会把 byte[] 当数字数组、读时抛类型不匹配）。
     */
    @JsonSerialize(using = com.taotao.cloud.mq.consistency.ttc.common.util.Base64Serializer.class)
    @JsonDeserialize(using = com.taotao.cloud.mq.consistency.ttc.common.util.Base64Serializer.class)
    private byte[] body;

    /**
     * 事务ID，用于事务消息
     */
    private String transactionId;

    public Message() {
        this.properties = new HashMap<>();
    }

    public Message(String topic, byte[] body) {
        this();
        this.topic = topic;
        this.body = body;
    }

    public Message(String topic, String tags, byte[] body) {
        this(topic, body);
        this.tags = tags;
    }

    public Message(String topic, String tags, String keys, byte[] body) {
        this(topic, tags, body);
        this.keys = keys;
    }

    // ==================== Getters and Setters ====================

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getProperty(String name) {
        return this.properties != null ? this.properties.get(name) : null;
    }

    public void putProperty(String name, String value) {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(name, value);
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    // ==================== Utility Methods ====================

    /**
     * 获取消息体长度
     */
    public int getBodyLength() {
        return body != null ? body.length : 0;
    }

    /**
     * 获取延迟级别
     */
    public int getDelayTimeLevel() {
        String delayLevel = this.getProperty("DELAY");
        if (delayLevel != null) {
            try {
                return Integer.parseInt(delayLevel);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 设置延迟级别
     *
     * @param level 延迟级别，从1开始
     */
    public void setDelayTimeLevel(int level) {
        this.putProperty("DELAY", String.valueOf(level));
    }

    @Override
    public String toString() {
        return "Message{" +
                "topic='" + topic + '\'' +
                ", tags='" + tags + '\'' +
                ", keys='" + keys + '\'' +
                ", flag=" + flag +
                ", bodyLength=" + getBodyLength() +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}

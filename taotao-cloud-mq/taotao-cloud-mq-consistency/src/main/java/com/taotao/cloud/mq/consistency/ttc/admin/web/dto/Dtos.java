package com.taotao.cloud.mq.consistency.ttc.admin.web.dto;

import java.util.List;

/**
 * 控制台 REST 的出参 DTO.
 * <p>
 * 字段名与类型严格对齐 z-mq-admin 前端 {@code src/api/index.ts} 里的 interface ——
 * 前端 {@code ClusterOverview} / {@code BrokerInfo} / {@code TopicInfo} / {@code MessageItem} /
 * {@code ConsumerGroupInfo} 五份声明逐字对应这里五个类，Jackson 序列化时不做任何改名。
 */
public final class Dtos {

    private Dtos() {
    }

    /** 对应前端 ClusterOverview. */
    public static class ClusterOverviewDto {
        private int namesrvCount;
        private int brokerCount;
        private int topicCount;
        private int consumerGroupCount;
        private long totalMessages;
        private long messagesPerMinute;
        private long messagesPerSecond;
        private long storageBytes;

        public int getNamesrvCount() { return namesrvCount; }
        public void setNamesrvCount(int v) { this.namesrvCount = v; }
        public int getBrokerCount() { return brokerCount; }
        public void setBrokerCount(int v) { this.brokerCount = v; }
        public int getTopicCount() { return topicCount; }
        public void setTopicCount(int v) { this.topicCount = v; }
        public int getConsumerGroupCount() { return consumerGroupCount; }
        public void setConsumerGroupCount(int v) { this.consumerGroupCount = v; }
        public long getTotalMessages() { return totalMessages; }
        public void setTotalMessages(long v) { this.totalMessages = v; }
        public long getMessagesPerMinute() { return messagesPerMinute; }
        public void setMessagesPerMinute(long v) { this.messagesPerMinute = v; }
        public long getMessagesPerSecond() { return messagesPerSecond; }
        public void setMessagesPerSecond(long v) { this.messagesPerSecond = v; }
        public long getStorageBytes() { return storageBytes; }
        public void setStorageBytes(long v) { this.storageBytes = v; }
    }

    /** 对应前端 BrokerInfo. */
    public static class BrokerInfoDto {
        private String brokerName;
        private String cluster;
        private int brokerId;
        private String addr;
        private String version;
        private int producerCount;
        private int consumerCount;
        private int queues;
        private int topics;
        private int inTps;
        private int outTps;
        private long lastUpdateMs;

        public String getBrokerName() { return brokerName; }
        public void setBrokerName(String v) { this.brokerName = v; }
        public String getCluster() { return cluster; }
        public void setCluster(String v) { this.cluster = v; }
        public int getBrokerId() { return brokerId; }
        public void setBrokerId(int v) { this.brokerId = v; }
        public String getAddr() { return addr; }
        public void setAddr(String v) { this.addr = v; }
        public String getVersion() { return version; }
        public void setVersion(String v) { this.version = v; }
        public int getProducerCount() { return producerCount; }
        public void setProducerCount(int v) { this.producerCount = v; }
        public int getConsumerCount() { return consumerCount; }
        public void setConsumerCount(int v) { this.consumerCount = v; }
        public int getQueues() { return queues; }
        public void setQueues(int v) { this.queues = v; }
        public int getTopics() { return topics; }
        public void setTopics(int v) { this.topics = v; }
        public int getInTps() { return inTps; }
        public void setInTps(int v) { this.inTps = v; }
        public int getOutTps() { return outTps; }
        public void setOutTps(int v) { this.outTps = v; }
        public long getLastUpdateMs() { return lastUpdateMs; }
        public void setLastUpdateMs(long v) { this.lastUpdateMs = v; }
    }

    /** 对应前端 TopicInfo. */
    public static class TopicInfoDto {
        private String topic;
        private int readQueueNums;
        private int writeQueueNums;
        private int perm;
        private String broker;
        private boolean order;

        public String getTopic() { return topic; }
        public void setTopic(String v) { this.topic = v; }
        public int getReadQueueNums() { return readQueueNums; }
        public void setReadQueueNums(int v) { this.readQueueNums = v; }
        public int getWriteQueueNums() { return writeQueueNums; }
        public void setWriteQueueNums(int v) { this.writeQueueNums = v; }
        public int getPerm() { return perm; }
        public void setPerm(int v) { this.perm = v; }
        public String getBroker() { return broker; }
        public void setBroker(String v) { this.broker = v; }
        public boolean isOrder() { return order; }
        public void setOrder(boolean v) { this.order = v; }
    }

    /** 对应前端 MessageItem. */
    public static class MessageItemDto {
        private String msgId;
        private String topic;
        private int queueId;
        private long queueOffset;
        private long bornTimestamp;
        private long storeTimestamp;
        private String body;
        private String keys;
        private String tags;

        public String getMsgId() { return msgId; }
        public void setMsgId(String v) { this.msgId = v; }
        public String getTopic() { return topic; }
        public void setTopic(String v) { this.topic = v; }
        public int getQueueId() { return queueId; }
        public void setQueueId(int v) { this.queueId = v; }
        public long getQueueOffset() { return queueOffset; }
        public void setQueueOffset(long v) { this.queueOffset = v; }
        public long getBornTimestamp() { return bornTimestamp; }
        public void setBornTimestamp(long v) { this.bornTimestamp = v; }
        public long getStoreTimestamp() { return storeTimestamp; }
        public void setStoreTimestamp(long v) { this.storeTimestamp = v; }
        public String getBody() { return body; }
        public void setBody(String v) { this.body = v; }
        public String getKeys() { return keys; }
        public void setKeys(String v) { this.keys = v; }
        public String getTags() { return tags; }
        public void setTags(String v) { this.tags = v; }
    }

    /** 对应前端 ConsumerGroupInfo. */
    public static class ConsumerGroupInfoDto {
        private String group;
        private String state;
        private int consumers;
        private List<String> topics;
        private long totalLag;
        private int tps;

        public String getGroup() { return group; }
        public void setGroup(String v) { this.group = v; }
        public String getState() { return state; }
        public void setState(String v) { this.state = v; }
        public int getConsumers() { return consumers; }
        public void setConsumers(int v) { this.consumers = v; }
        public List<String> getTopics() { return topics; }
        public void setTopics(List<String> v) { this.topics = v; }
        public long getTotalLag() { return totalLag; }
        public void setTotalLag(long v) { this.totalLag = v; }
        public int getTps() { return tps; }
        public void setTps(int v) { this.tps = v; }
    }
}

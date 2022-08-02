package com.taotao.cloud.sys.biz.modules.kafka.dtos;

import lombok.Data;

@Data
public class TopicLogSize {
    private String topic;
    private int partition;
    private long logSize;
    private long minOffset;
    private long timestamp;

    public TopicLogSize() {
    }

    public TopicLogSize(String topic, long logSize) {
        this.topic = topic;
        this.logSize = logSize;
    }

    public TopicLogSize(String topic, int partition, long logSize) {
        this.topic = topic;
        this.partition = partition;
        this.logSize = logSize;
    }

    public TopicLogSize(String topic, int partition, long logSize, long minOffset) {
        this.topic = topic;
        this.partition = partition;
        this.logSize = logSize;
        this.minOffset = minOffset;
    }
}

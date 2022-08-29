package com.taotao.cloud.sys.api.model.dto.kafka;

public class ExtendConsumerRecord {
    private Object value;
    private int partition;
    private long offset;
    private String timestamp;

    public ExtendConsumerRecord() {
    }

    public ExtendConsumerRecord(Object value, int partition, long offset, String timestamp) {
        this.value = value;
        this.partition = partition;
        this.offset = offset;
        this.timestamp = timestamp;
    }

}

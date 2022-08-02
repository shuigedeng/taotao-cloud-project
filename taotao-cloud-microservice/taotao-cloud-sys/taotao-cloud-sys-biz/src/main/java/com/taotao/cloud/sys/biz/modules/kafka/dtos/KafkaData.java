package com.taotao.cloud.sys.biz.modules.kafka.dtos;

public class KafkaData implements Comparable<KafkaData> {
    protected Long offset;
    protected Object data;
    protected long timestamp;

    public KafkaData(Long offset, Object data) {
        this.offset = offset;
        this.data = data;
    }

    public KafkaData(Long offset, Object data, long timestamp) {
        this.offset = offset;
        this.data = data;
        this.timestamp = timestamp;
    }

    public KafkaData() {
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public int compareTo(KafkaData o) {
        return o.offset.compareTo(this.offset);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

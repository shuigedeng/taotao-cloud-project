package com.taotao.cloud.sys.api.model.dto.kafka;

public class PartitionKafkaData extends KafkaData {
    private int partition;

    public PartitionKafkaData(Long offset, Object data, long timestamp, int partition) {
        super(offset, data, timestamp);
        this.partition = partition;
    }

    public PartitionKafkaData() {
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    @Override
    public int compareTo(KafkaData o) {
        return (int) (o.timestamp - this.timestamp);
    }
}

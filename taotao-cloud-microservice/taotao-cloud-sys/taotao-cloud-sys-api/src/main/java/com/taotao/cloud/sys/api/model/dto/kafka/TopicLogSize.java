package com.taotao.cloud.sys.api.model.dto.kafka;

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

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}

	public long getLogSize() {
		return logSize;
	}

	public void setLogSize(long logSize) {
		this.logSize = logSize;
	}

	public long getMinOffset() {
		return minOffset;
	}

	public void setMinOffset(long minOffset) {
		this.minOffset = minOffset;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}

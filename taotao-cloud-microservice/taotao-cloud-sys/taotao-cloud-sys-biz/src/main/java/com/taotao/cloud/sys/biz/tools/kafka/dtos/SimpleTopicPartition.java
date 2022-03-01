package com.taotao.cloud.sys.biz.tools.kafka.dtos;


public class SimpleTopicPartition {
    private String topic;
    private int partition;

    public SimpleTopicPartition() {
    }

    public SimpleTopicPartition(String topic, int partition) {
        this.topic = topic;
        this.partition = partition;
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
}

package com.taotao.cloud.sys.api.model.dto.kafka;


public class DataConsumerParam {
    protected String clusterName;
    protected String topic;
    protected int partition;
    protected int perPartitionSize;
    protected String serializer;
    protected String classloaderName;

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
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

	public int getPerPartitionSize() {
		return perPartitionSize;
	}

	public void setPerPartitionSize(int perPartitionSize) {
		this.perPartitionSize = perPartitionSize;
	}

	public String getSerializer() {
		return serializer;
	}

	public void setSerializer(String serializer) {
		this.serializer = serializer;
	}

	public String getClassloaderName() {
		return classloaderName;
	}

	public void setClassloaderName(String classloaderName) {
		this.classloaderName = classloaderName;
	}
}

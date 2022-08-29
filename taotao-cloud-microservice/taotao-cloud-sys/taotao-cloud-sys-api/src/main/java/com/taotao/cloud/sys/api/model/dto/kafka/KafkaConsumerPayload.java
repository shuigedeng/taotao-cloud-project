package com.taotao.cloud.sys.api.model.dto.kafka;

public class KafkaConsumerPayload {
    private String clusterName;
    private String topic;
    private String serializable;
    private String classloader;
    private boolean start;

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

	public String getSerializable() {
		return serializable;
	}

	public void setSerializable(String serializable) {
		this.serializable = serializable;
	}

	public String getClassloader() {
		return classloader;
	}

	public void setClassloader(String classloader) {
		this.classloader = classloader;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}
}

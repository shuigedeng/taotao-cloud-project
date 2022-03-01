package com.taotao.cloud.sys.biz.tools.kafka.dtos;


public class SendJsonDataParam {
    protected String clusterName;
    protected String topic;
    protected String key;
    protected String data;

    public SendJsonDataParam() {
    }

    /**
     * @param key
     * @param data
     */
    public SendJsonDataParam(String clusterName, String topic, String key, String data) {
        this.clusterName = clusterName;
        this.topic = topic;
        this.key = key;
        this.data = data;
    }

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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}

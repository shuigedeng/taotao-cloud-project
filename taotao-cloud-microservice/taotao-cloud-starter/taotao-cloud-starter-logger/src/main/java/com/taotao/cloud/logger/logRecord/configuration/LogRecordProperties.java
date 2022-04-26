package com.taotao.cloud.logger.logRecord.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "log-record")
public class LogRecordProperties {

    private RabbitMqProperties rabbitMqProperties;

    private RocketMqProperties rocketMqProperties;

    private StreamProperties stream;

    private String dataPipeline;

    private int poolSize = 4;

    public static class RabbitMqProperties {
        private String host;
        private int port;
        private String username;
        private String password;
        private String queueName;
        private String exchangeName;
        private String routingKey;

	    public String getHost() {
		    return host;
	    }

	    public void setHost(String host) {
		    this.host = host;
	    }

	    public int getPort() {
		    return port;
	    }

	    public void setPort(int port) {
		    this.port = port;
	    }

	    public String getUsername() {
		    return username;
	    }

	    public void setUsername(String username) {
		    this.username = username;
	    }

	    public String getPassword() {
		    return password;
	    }

	    public void setPassword(String password) {
		    this.password = password;
	    }

	    public String getQueueName() {
		    return queueName;
	    }

	    public void setQueueName(String queueName) {
		    this.queueName = queueName;
	    }

	    public String getExchangeName() {
		    return exchangeName;
	    }

	    public void setExchangeName(String exchangeName) {
		    this.exchangeName = exchangeName;
	    }

	    public String getRoutingKey() {
		    return routingKey;
	    }

	    public void setRoutingKey(String routingKey) {
		    this.routingKey = routingKey;
	    }
    }

    public static class RocketMqProperties {
        private String topic = "logRecord";
        private String tag = "";
        private String namesrvAddr = "localhost:9876";
        private String groupName = "logRecord";
        private int maxMessageSize = 4000000;
        private int sendMsgTimeout = 3000;
        private int retryTimesWhenSendFailed = 2;

	    public String getTopic() {
		    return topic;
	    }

	    public void setTopic(String topic) {
		    this.topic = topic;
	    }

	    public String getTag() {
		    return tag;
	    }

	    public void setTag(String tag) {
		    this.tag = tag;
	    }

	    public String getNamesrvAddr() {
		    return namesrvAddr;
	    }

	    public void setNamesrvAddr(String namesrvAddr) {
		    this.namesrvAddr = namesrvAddr;
	    }

	    public String getGroupName() {
		    return groupName;
	    }

	    public void setGroupName(String groupName) {
		    this.groupName = groupName;
	    }

	    public int getMaxMessageSize() {
		    return maxMessageSize;
	    }

	    public void setMaxMessageSize(int maxMessageSize) {
		    this.maxMessageSize = maxMessageSize;
	    }

	    public int getSendMsgTimeout() {
		    return sendMsgTimeout;
	    }

	    public void setSendMsgTimeout(int sendMsgTimeout) {
		    this.sendMsgTimeout = sendMsgTimeout;
	    }

	    public int getRetryTimesWhenSendFailed() {
		    return retryTimesWhenSendFailed;
	    }

	    public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
		    this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
	    }
    }

    public static class StreamProperties {
        /**
         * 默认对应消息中间件topic rocketmq的topic, RabbitMq的 exchangeName
         */
        private String destination;

        /**
         * 默认对应的分组
         */
        private String group;

        /**
         * 默认的binder（对应的消息中间件）
         */
        private String binder;

	    public String getDestination() {
		    return destination;
	    }

	    public void setDestination(String destination) {
		    this.destination = destination;
	    }

	    public String getGroup() {
		    return group;
	    }

	    public void setGroup(String group) {
		    this.group = group;
	    }

	    public String getBinder() {
		    return binder;
	    }

	    public void setBinder(String binder) {
		    this.binder = binder;
	    }
    }

	public RabbitMqProperties getRabbitMqProperties() {
		return rabbitMqProperties;
	}

	public void setRabbitMqProperties(
		RabbitMqProperties rabbitMqProperties) {
		this.rabbitMqProperties = rabbitMqProperties;
	}

	public RocketMqProperties getRocketMqProperties() {
		return rocketMqProperties;
	}

	public void setRocketMqProperties(
		RocketMqProperties rocketMqProperties) {
		this.rocketMqProperties = rocketMqProperties;
	}

	public StreamProperties getStream() {
		return stream;
	}

	public void setStream(
		StreamProperties stream) {
		this.stream = stream;
	}

	public String getDataPipeline() {
		return dataPipeline;
	}

	public void setDataPipeline(String dataPipeline) {
		this.dataPipeline = dataPipeline;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
}

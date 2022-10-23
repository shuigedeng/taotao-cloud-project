package com.taotao.cloud.mq.rocketmq.rocketmq.env;

import org.apache.rocketmq.common.topic.TopicValidator;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 完善 RocketMQ 4.7.x 版本的配置
 *
 * @link https://github.com/apache/rocketmq-spring/blob/master/rocketmq-spring-boot/src/main/java/org/apache/rocketmq/spring/autoconfigure/RocketMQProperties.java
 */
@ConfigurationProperties(prefix = "taotao.cloud.mq.rocketmq.consumer")
public class FixedRocketMQConsumerProperties {

	/**
	 * Group name of consumer.
	 */
	private String group;

	/**
	 * Namespace for this MQ Consumer instance.
	 */
	private String namespace;

	/**
	 * Topic name of consumer.
	 */
	private String topic;

	/**
	 * Control message mode, if you want all subscribers receive message all message, broadcasting
	 * is a good choice.
	 */
	private String messageModel = "CLUSTERING";

	/**
	 * Control how to selector message.
	 */
	private String selectorType = "TAG";

	/**
	 * Control which message can be select.
	 */
	private String selectorExpression = "*";

	/**
	 * The property of "access-key".
	 */
	private String accessKey;

	/**
	 * The property of "secret-key".
	 */
	private String secretKey;

	/**
	 * Maximum number of messages pulled each time.
	 */
	private int pullBatchSize = 10;

	/**
	 * Switch flag instance for message trace.
	 */
	private boolean enableMsgTrace = false;

	/**
	 * The name value of message trace topic.If you don't config,you can use the default trace topic
	 * name.
	 */
	private String customizedTraceTopic = TopicValidator.RMQ_SYS_TRACE_TOPIC;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getMessageModel() {
		return messageModel;
	}

	public void setMessageModel(String messageModel) {
		this.messageModel = messageModel;
	}

	public String getSelectorType() {
		return selectorType;
	}

	public void setSelectorType(String selectorType) {
		this.selectorType = selectorType;
	}

	public String getSelectorExpression() {
		return selectorExpression;
	}

	public void setSelectorExpression(String selectorExpression) {
		this.selectorExpression = selectorExpression;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public int getPullBatchSize() {
		return pullBatchSize;
	}

	public void setPullBatchSize(int pullBatchSize) {
		this.pullBatchSize = pullBatchSize;
	}

	public boolean isEnableMsgTrace() {
		return enableMsgTrace;
	}

	public void setEnableMsgTrace(boolean enableMsgTrace) {
		this.enableMsgTrace = enableMsgTrace;
	}

	public String getCustomizedTraceTopic() {
		return customizedTraceTopic;
	}

	public void setCustomizedTraceTopic(String customizedTraceTopic) {
		this.customizedTraceTopic = customizedTraceTopic;
	}
}

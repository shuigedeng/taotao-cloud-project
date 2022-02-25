package com.taotao.cloud.stream.framework.trigger.model;


import java.io.Serializable;

/**
 * 延时任务消息
 */
public class TimeTriggerMsg implements Serializable {


	private static final long serialVersionUID = 8897917127201859535L;

	/**
	 * 执行器beanId
	 */
	private String triggerExecutor;

	/**
	 * 执行器 执行时间
	 */
	private Long triggerTime;

	/**
	 * 执行器参数
	 */
	private Object param;

	/**
	 * 唯一KEY
	 */
	private String uniqueKey;

	/**
	 * 信息队列主题
	 */
	private String topic;

	public TimeTriggerMsg() {
	}


	public TimeTriggerMsg(String triggerExecutor, Long triggerTime, Object param,
		String uniqueKey, String topic) {
		this.triggerExecutor = triggerExecutor;
		this.triggerTime = triggerTime;
		this.param = param;
		this.uniqueKey = uniqueKey;
		this.topic = topic;
	}

	public String getTriggerExecutor() {
		return triggerExecutor;
	}

	public void setTriggerExecutor(String triggerExecutor) {
		this.triggerExecutor = triggerExecutor;
	}

	public Long getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(Long triggerTime) {
		this.triggerTime = triggerTime;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}

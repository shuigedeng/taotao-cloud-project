package com.taotao.cloud.disruptor.event;

import java.util.EventObject;

/**
 * 事件(Event) 就是通过 Disruptor 进行交换的数据类型。
 */
public abstract class DisruptorEvent extends EventObject {

	/** System time when the event happened */
	private final long timestamp;
	/** Event Name */
	private String event;
	/** Event Tag */
	private String tag;
	/** Event Keys */
	private String key;
	/** Event body */
	private Object body;
	
	/**
	 * Create a new ConsumeEvent.
	 * @param source the object on which the event initially occurred (never {@code null})
	 */
	public DisruptorEvent(Object source) {
		super(source);
		this.timestamp = System.currentTimeMillis();
	}

	/**
	 * Return the system time in milliseconds when the event happened.
	 * @return system time in milliseconds 
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	public String getRouteExpression() {
		return new StringBuilder("/").append(getEvent()).append("/").append(getTag()).append("/")
				.append(getKey()).toString();
		
	}
	
	public void setSource(Object source){
		this.source = source;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("DisruptorEvent [event :").append(getEvent()).append(",tag :").append(getTag()).append(", key :")
				.append(getKey()).append("]").toString();
	}
	
}

/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.disruptor.event;

import java.util.EventObject;

/**
 * 事件(Event) 就是通过 Disruptor 进行交换的数据类型。
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:08:58
 */
public abstract class DisruptorEvent extends EventObject {

	/**
	 * System time when the event happened
	 */
	private final long timestamp;
	/**
	 * Event Name
	 */
	private String event;
	/**
	 * Event Tag
	 */
	private String tag;
	/**
	 * Event Keys
	 */
	private String key;
	/**
	 * Event body
	 */
	private Object body;

	/**
	 * Create a new ConsumeEvent.
	 *
	 * @param source the object on which the event initially occurred (never {@code null})
	 */
	public DisruptorEvent(Object source) {
		super(source);
		this.timestamp = System.currentTimeMillis();
	}

	/**
	 * Return the system time in milliseconds when the event happened.
	 *
	 * @return system time in milliseconds
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}

	public String getRouteExpression() {
		return new StringBuilder("/").append(getEvent()).append("/").append(getTag()).append("/")
			.append(getKey()).toString();

	}

	public void setSource(Object source) {
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
		return new StringBuilder("DisruptorEvent [event :").append(getEvent()).append(",tag :")
			.append(getTag()).append(", key :")
			.append(getKey()).append("]").toString();
	}

}

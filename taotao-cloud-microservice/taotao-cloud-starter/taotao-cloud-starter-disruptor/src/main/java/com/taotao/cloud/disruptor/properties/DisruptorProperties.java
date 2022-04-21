/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.disruptor.properties;

import com.taotao.cloud.disruptor.context.EventHandlerDefinition;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * DisruptorProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:24:34
 */
@ConfigurationProperties(prefix = DisruptorProperties.PREFIX)
public class DisruptorProperties {

	public static final String PREFIX = "taotao.cloud.disruptor";

	/**
	 * Enable Disruptor.
	 */
	private boolean enabled = false;
	/**
	 * 是否自动创建RingBuffer对象
	 */
	private boolean ringBuffer = false;
	/**
	 * RingBuffer缓冲区大小, 默认 1024
	 */
	private int ringBufferSize = 1024;
	/**
	 * 消息消费线程池大小, 默认 4
	 */
	private int ringThreadNumbers = 4;
	/**
	 * 是否对生产者，如果是则通过 RingBuffer.createMultiProducer创建一个多生产者的RingBuffer，
	 * 否则通过RingBuffer.createSingleProducer创建一个单生产者的RingBuffer
	 */
	private boolean multiProducer = false;
	/**
	 * 消息出来责任链
	 */
	private List<EventHandlerDefinition> handlerDefinitions = new ArrayList<>();

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean getRingBuffer() {
		return ringBuffer;
	}

	public void setRingBuffer(boolean ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public boolean getMultiProducer() {
		return multiProducer;
	}

	public void setMultiProducer(boolean multiProducer) {
		this.multiProducer = multiProducer;
	}

	public int getRingBufferSize() {
		return ringBufferSize;
	}

	public void setRingBufferSize(int ringBufferSize) {
		this.ringBufferSize = ringBufferSize;
	}

	public int getRingThreadNumbers() {
		return ringThreadNumbers;
	}

	public void setRingThreadNumbers(int ringThreadNumbers) {
		this.ringThreadNumbers = ringThreadNumbers;
	}

	public List<EventHandlerDefinition> getHandlerDefinitions() {
		return handlerDefinitions;
	}

	public void setHandlerDefinitions(List<EventHandlerDefinition> handlerDefinitions) {
		this.handlerDefinitions = handlerDefinitions;
	}

}

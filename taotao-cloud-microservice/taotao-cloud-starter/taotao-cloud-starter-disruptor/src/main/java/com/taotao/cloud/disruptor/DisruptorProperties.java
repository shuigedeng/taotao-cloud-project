package com.taotao.cloud.disruptor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.lmax.disruptor.spring.boot.config.EventHandlerDefinition;

@ConfigurationProperties(DisruptorProperties.PREFIX)
public class DisruptorProperties {

	public static final String PREFIX = "spring.disruptor";

	/** Enable Disruptor. */
	private boolean enabled = false;
	/** 是否自动创建RingBuffer对象 */
	private boolean ringBuffer = false;
	/** RingBuffer缓冲区大小, 默认 1024 */
	private int ringBufferSize = 1024;
	/** 消息消费线程池大小, 默认 4 */
	private int ringThreadNumbers = 4;
	/** 是否对生产者，如果是则通过 RingBuffer.createMultiProducer创建一个多生产者的RingBuffer，否则通过RingBuffer.createSingleProducer创建一个单生产者的RingBuffer */
	private boolean multiProducer = false;
	/** 消息出来责任链 */
	private List<EventHandlerDefinition> handlerDefinitions = new ArrayList<EventHandlerDefinition>();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isRingBuffer() {
		return ringBuffer;
	}

	public void setRingBuffer(boolean ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public boolean isMultiProducer() {
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

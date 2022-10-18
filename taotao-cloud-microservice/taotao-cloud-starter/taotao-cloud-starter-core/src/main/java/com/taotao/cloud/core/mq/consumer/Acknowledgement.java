package com.taotao.cloud.core.mq.consumer;

/**
 * ACK 确认
 */
@FunctionalInterface
public interface Acknowledgement {

	/**
	 * 提交
	 */
	void acknowledge();
}

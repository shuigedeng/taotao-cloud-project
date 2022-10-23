package com.taotao.cloud.mq.common.consumer;

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

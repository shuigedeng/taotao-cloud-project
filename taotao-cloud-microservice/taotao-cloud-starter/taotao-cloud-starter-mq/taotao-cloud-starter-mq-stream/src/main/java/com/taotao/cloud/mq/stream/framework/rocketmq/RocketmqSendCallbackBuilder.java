package com.taotao.cloud.mq.stream.framework.rocketmq;

/**
 * RocketmqSendCallbackBuilder
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-25 10:01:00
 */
public class RocketmqSendCallbackBuilder {


	public static RocketmqSendCallback commonCallback() {
		return new RocketmqSendCallback();
	}

}

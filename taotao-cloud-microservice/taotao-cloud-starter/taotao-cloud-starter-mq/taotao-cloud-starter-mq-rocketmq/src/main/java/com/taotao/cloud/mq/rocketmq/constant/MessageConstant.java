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
package com.taotao.cloud.mq.rocketmq.constant;


import com.taotao.cloud.common.constant.StrPool;

/**
 * 消息中心常量
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:53:40
 */
public class MessageConstant {

	/**
	 * 生产者服务名称
	 */
	public static final String MESSAGE_PRODUCER_SERVICE = "mate-message-producer";

	/**
	 * 消费者服务名称
	 */
	public static final String MESSAGE_CONSUMER_SERVICE = "mate-message-consumer";


	/**
	 * 短信消息
	 */
	public static final String SMS_MESSAGE = "sms";

	/**
	 * 邮件消息
	 */
	public static final String EMAIL_MESSAGE = "email";

	/**
	 * 订单消息
	 */
	public static final String ORDER_MESSAGE = "order";

	/**
	 * 生产者标识
	 */
	public static final String OUTPUT = "output";

	/**
	 * 消费者标识
	 */
	public static final String INPUT = "input";

	/**
	 * 消息生产者
	 */
	public static final String SMS_MESSAGE_OUTPUT = SMS_MESSAGE + StrPool.DASH + OUTPUT;

	/**
	 * 邮件生产者
	 */
	public static final String EMAIL_MESSAGE_OUTPUT = EMAIL_MESSAGE + StrPool.DASH + OUTPUT;

	/**
	 * 订单生产者
	 */
	public static final String ORDER_MESSAGE_OUTPUT = ORDER_MESSAGE + StrPool.DASH + OUTPUT;

	/**
	 * 短信消费者
	 */
	public static final String SMS_MESSAGE_INPUT = SMS_MESSAGE + StrPool.DASH + INPUT;

	/**
	 * 邮件消费者
	 */
	public static final String EMAIL_MESSAGE_INPUT = EMAIL_MESSAGE + StrPool.DASH + INPUT;

	/**
	 * 订单消费者
	 */
	public static final String ORDER_MESSAGE_INPUT = ORDER_MESSAGE + StrPool.DASH + INPUT;

	/**
	 * 订单组
	 */
	public static final String ORDER_BINDER_GROUP =
		ORDER_MESSAGE + StrPool.DASH + "binder-group";


}

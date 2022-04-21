/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.rocketmq.channel;

import com.taotao.cloud.rocketmq.constant.MessageConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 消费者Channel
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:50:45
 */
public interface TaoTaoCloudSink {

	String SMS_MESSAGE_INPUT = MessageConstant.SMS_MESSAGE_INPUT;

	String EMAIL_MESSAGE_INPUT = MessageConstant.EMAIL_MESSAGE_INPUT;

	String ORDER_MESSAGE_INPUT = MessageConstant.ORDER_MESSAGE_INPUT;

	/**
	 * 短消息消费者
	 *
	 * @return {@link org.springframework.messaging.SubscribableChannel }
	 * @author shuigedeng
	 * @since 2021-09-07 20:51:07
	 */
	@Input(SMS_MESSAGE_INPUT)
	SubscribableChannel smsInput();

	/**
	 * 邮件消费者
	 *
	 * @return {@link org.springframework.messaging.SubscribableChannel }
	 * @author shuigedeng
	 * @since 2021-09-07 20:51:13
	 */
	@Input(EMAIL_MESSAGE_INPUT)
	SubscribableChannel emailInput();

	/**
	 * 订单消费者
	 *
	 * @return {@link org.springframework.messaging.SubscribableChannel }
	 * @author shuigedeng
	 * @since 2021-09-07 20:51:20
	 */
	@Input(ORDER_MESSAGE_INPUT)
	SubscribableChannel orderInput();


}

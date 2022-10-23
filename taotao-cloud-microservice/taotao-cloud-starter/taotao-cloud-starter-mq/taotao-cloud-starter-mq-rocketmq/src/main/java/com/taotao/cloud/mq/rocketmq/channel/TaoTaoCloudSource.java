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
package com.taotao.cloud.mq.rocketmq.channel;

import com.taotao.cloud.mq.rocketmq.constant.MessageConstant;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 生产者Channel
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:51:42
 */
public interface TaoTaoCloudSource {

	/**
	 * 短消息通道
	 *
	 * @return {@link MessageChannel }
	 * @author shuigedeng
	 * @since 2021-09-07 20:52:46
	 */
	@Output(MessageConstant.SMS_MESSAGE_OUTPUT)
	MessageChannel smsOutput();

	/**
	 * 邮件通道
	 *
	 * @return {@link MessageChannel }
	 * @author shuigedeng
	 * @since 2021-09-07 20:52:50
	 */
	@Output(MessageConstant.EMAIL_MESSAGE_OUTPUT)
	MessageChannel emailOutput();

	/**
	 * 订单通道
	 *
	 * @return {@link MessageChannel }
	 * @author shuigedeng
	 * @since 2021-09-07 20:52:56
	 */
	@Output(MessageConstant.ORDER_MESSAGE_OUTPUT)
	MessageChannel orderOutput();
}

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

package com.taotao.cloud.order.application.stream.rocketmq; /// *
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
// package com.taotao.cloud.order.application.rocketmq;
//
// import org.springframework.cloud.stream.annotation.StreamListener;
// import org.springframework.messaging.Message;
// import org.springframework.stereotype.Service;
//
/// **
// * 积分服务的消费者，接收到下单成功后增加积分
// */
// @Service
// public class IntegralReceiveService {
//
//	@StreamListener("inputWithTx")
//	public void receive(Message message) {
//		//模拟消费异常
//		String consumeError = (String) message.getHeaders().get("consumeError");
//		if ("1".equals(consumeError)) {
//			System.err.println("============Exception：积分进程挂了，消费消息失败");
//			//模拟插入订单后服务器挂了，没有commit事务消息
//			throw new RuntimeException("积分服务器挂了");
//		}
//
//		LogUtils.info("============收到订单信息，增加积分:" + message);
//	}
//
//	/**
//	 * 消费死信队列
//	 */
//	@StreamListener("inputDlq")
//	public void receiveDlq(Message message) {
//		String orderId = (String) message.getHeaders().get("orderId");
//		System.err.println("============消费死信队列消息，记录日志并预警：" + orderId);
//	}
// }

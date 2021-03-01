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
package com.taotao.cloud.demo.rocketmq.transactional.listener;

import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.demo.rocketmq.transactional.model.Order;
import com.taotao.cloud.demo.rocketmq.transactional.service.IOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

/**
 * OrderTransactionListenerImpl
 */
@RocketMQTransactionListener(txProducerGroup = "order-tx-produce-group", corePoolSize = 5, maximumPoolSize = 10)
public class OrderTransactionListenerImpl implements RocketMQLocalTransactionListener {

	@Autowired
	private IOrderService orderService;

	/**
	 * 提交本地事务
	 */
	@Override
	public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
		//插入订单数据
		String orderJson = new String(((byte[]) message.getPayload()));
		Order order = JsonUtil.toObject(orderJson, Order.class);
		orderService.save(order);

		String produceError = (String) message.getHeaders().get("produceError");
		if ("1".equals(produceError)) {
			System.err.println("============Exception：订单进程挂了，事务消息没提交");
			//模拟插入订单后服务器挂了，没有commit事务消息
			throw new RuntimeException("============订单服务器挂了");
		}

		//提交事务消息
		return RocketMQLocalTransactionState.COMMIT;
	}

	/**
	 * 事务回查接口
	 * <p>
	 * 如果事务消息一直没提交，则定时判断订单数据是否已经插入 是：提交事务消息 否：回滚事务消息
	 */
	@Override
	public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
		String orderId = (String) message.getHeaders().get("orderId");
		System.out.println("============事务回查-orderId：" + orderId);
		//判断之前的事务是否已经提交：订单记录是否已经保存
		int count = 1;
		//select count(1) from t_order where order_id = ${orderId}
		System.out.println("============事务回查-订单已生成-提交事务消息");
		return count > 0 ? RocketMQLocalTransactionState.COMMIT
			: RocketMQLocalTransactionState.ROLLBACK;
	}
}

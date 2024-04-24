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

package com.taotao.cloud.order.application.stream.producer;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

/**
 * 发送之后用于控制原子性的类 在消息发送之后，收到rocketmq的发送结果通知后才提交的本地事务。
 *
 * @link <a href="https://blog.csdn.net/guzhangyu12345/article/details/107989633">...</a>
 */
// @RocketMQTransactionListener(txProducerGroup = MessageConstant.ORDER_BINDER_GROUP)
@RocketMQTransactionListener
public class OrderTransactionListener implements RocketMQLocalTransactionListener {

    /**
     * rocketmq 消息发送成功之后，提交本地事务
     *
     * @param message 消息
     * @param o 　args
     * @return RocketMQLocalTransactionState
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        // MessageHeaders headers = message.getHeaders();
        // String transactionId = String.valueOf(headers.get(RocketMQHeaders.TRANSACTION_ID));
        // Long shareId = Long.valueOf(String.valueOf(headers.get("share_id")));
        // Order order = null;
        // try {
        // 	order = JSONObject.parseObject(new String((byte[]) message.getPayload()), Order.class);
        // } catch (Exception e) {
        // 	LogUtils.error(e);
        // }
        //
        //
        // Long args = (Long) o;
        // log.info(String.format("half message\npayload:%s, arg:%s, transactionId:%s", order, args,
        // message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID)));
        return RocketMQLocalTransactionState.COMMIT;
    }

    /**
     * rocketmq 回查,判断提交还是回滚
     *
     * @param message Message
     * @return RocketMQLocalTransactionState
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        // Order order = JSON.parseObject(new String((byte[]) message.getPayload()), Order.class);
        // // 业务查询本地事务是否执行成功
        // List<Order> orders = new ArrayList<>();
        // orders.add(order);
        // // 根据message去查询本地事务是否执行成功，如果成功，则commit
        // if (orders.size() > 0) {
        // 	return RocketMQLocalTransactionState.COMMIT;
        // } else {
        // 	return RocketMQLocalTransactionState.ROLLBACK;
        // }
        return RocketMQLocalTransactionState.COMMIT;
    }
}

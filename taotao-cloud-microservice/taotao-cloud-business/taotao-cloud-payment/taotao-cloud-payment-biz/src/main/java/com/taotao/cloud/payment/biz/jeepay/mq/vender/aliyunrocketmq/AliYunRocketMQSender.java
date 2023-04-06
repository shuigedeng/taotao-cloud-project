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

package com.taotao.cloud.payment.biz.jeepay.mq.vender.aliyunrocketmq;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.taotao.cloud.payment.biz.jeepay.mq.constant.MQVenderCS;
import com.taotao.cloud.payment.biz.jeepay.mq.model.AbstractMQ;
import com.taotao.cloud.payment.biz.jeepay.mq.vender.IMQSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** 阿里云rocketMQ 消息发送器的实现 */
@Slf4j
@Component
@ConditionalOnProperty(name = MQVenderCS.YML_VENDER_KEY, havingValue = MQVenderCS.ALIYUN_ROCKET_MQ)
public class AliYunRocketMQSender implements IMQSender {

    /** 最大延迟24小时 */
    private static final int MAX_DELAY_TIME = 60 * 60 * 24;
    /** 最小延迟1秒 */
    private static final int MIN_DELAY_TIME = 1;

    @Autowired
    private AliYunRocketMQFactory aliYunRocketMQFactory;

    private Producer producerClient;

    @Override
    public void send(AbstractMQ mqModel) {
        Message message = new Message(
                mqModel.getMQName(),
                AliYunRocketMQFactory.defaultTag,
                mqModel.toMessage().getBytes());
        sendMessage(message);
    }

    @Override
    public void send(AbstractMQ mqModel, int delaySeconds) {
        Message message = new Message(
                mqModel.getMQName(),
                AliYunRocketMQFactory.defaultTag,
                mqModel.toMessage().getBytes());
        if (delaySeconds > 0) {
            long delayTime = System.currentTimeMillis() + delayTimeCorrector(delaySeconds) * 1000;
            // 设置消息需要被投递的时间。
            message.setStartDeliverTime(delayTime);
        }
        sendMessage(message);
    }

    private void sendMessage(Message message) {
        if (producerClient == null) {
            producerClient = aliYunRocketMQFactory.producerClient();
        }
        producerClient.start();
        SendResult sendResult = producerClient.send(message);
        log.info("消息队列推送返回结果：{}", JSONObject.toJSONString(sendResult));
    }

    /** 检查延迟时间的有效性并返回校正后的延迟时间 */
    private int delayTimeCorrector(int delay) {
        if (delay < MIN_DELAY_TIME) {
            return MIN_DELAY_TIME;
        }
        if (delay > MAX_DELAY_TIME) {
            return MAX_DELAY_TIME;
        }
        return delay;
    }
}

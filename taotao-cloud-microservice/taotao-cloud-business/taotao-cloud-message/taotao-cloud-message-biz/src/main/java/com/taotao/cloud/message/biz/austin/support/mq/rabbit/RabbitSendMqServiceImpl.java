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

package com.taotao.cloud.message.biz.austin.support.mq.rabbit;

import com.taotao.cloud.message.biz.austin.support.constans.MessageQueuePipeline;
import com.taotao.cloud.message.biz.austin.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author xzcawl @Date 2022/7/15 17:29
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.RABBIT_MQ)
public class RabbitSendMqServiceImpl implements SendMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${austin.rabbitmq.topic.name}")
    private String confTopic;

    @Value("${austin.rabbitmq.exchange.name}")
    private String exchangeName;

    @Override
    public void send(String topic, String jsonValue, String tagId) {
        if (topic.equals(confTopic)) {
            rabbitTemplate.convertAndSend(exchangeName, confTopic, jsonValue);
        } else {
            log.error("RabbitSendMqServiceImpl send topic error! topic:{},confTopic:{}", topic, confTopic);
        }
    }

    @Override
    public void send(String topic, String jsonValue) {
        send(topic, jsonValue, null);
    }
}

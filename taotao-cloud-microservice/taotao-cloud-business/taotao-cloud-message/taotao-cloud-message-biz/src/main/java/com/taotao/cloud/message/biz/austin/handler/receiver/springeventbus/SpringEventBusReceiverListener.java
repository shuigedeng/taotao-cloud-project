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

package com.taotao.cloud.message.biz.austin.handler.receiver.springeventbus;

import com.alibaba.fastjson.JSON;
import com.taotao.cloud.message.biz.austin.support.constans.MessageQueuePipeline;
import com.taotao.cloud.message.biz.austin.support.mq.springeventbus.AustinSpringEventBusEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * 描述：
 *
 * @author tony
 * @date 2023/2/6 11:19
 */
@Service
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.SPRING_EVENT_BUS)
public class SpringEventBusReceiverListener implements ApplicationListener<AustinSpringEventBusEvent> {

    @Autowired
    private SpringEventBusReceiver springEventBusReceiver;

    @Value("${austin.business.topic.name}")
    private String sendTopic;

    @Value("${austin.business.recall.topic.name}")
    private String recallTopic;

    @Override
    public void onApplicationEvent(AustinSpringEventBusEvent event) {
        String topic = event.getAustinSpringEventSource().getTopic();
        String jsonValue = event.getAustinSpringEventSource().getJsonValue();
        if (topic.equals(sendTopic)) {
            springEventBusReceiver.consume(JSON.parseArray(jsonValue, TaskInfo.class));
        } else if (topic.equals(recallTopic)) {
            springEventBusReceiver.recall(JSON.parseObject(jsonValue, MessageTemplate.class));
        }
    }
}

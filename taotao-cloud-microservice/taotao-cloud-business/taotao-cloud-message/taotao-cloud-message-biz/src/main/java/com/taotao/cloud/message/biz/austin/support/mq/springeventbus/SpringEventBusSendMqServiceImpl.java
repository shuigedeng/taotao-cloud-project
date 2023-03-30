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

package com.taotao.cloud.message.biz.austin.support.mq.springeventbus;

import com.taotao.cloud.message.biz.austin.support.constans.MessageQueuePipeline;
import com.taotao.cloud.message.biz.austin.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 描述：
 *
 * @author tony
 * @date 2023/2/6 11:11
 */
@Slf4j
@Service
@ConditionalOnProperty(
        name = "austin.mq.pipeline",
        havingValue = MessageQueuePipeline.SPRING_EVENT_BUS)
public class SpringEventBusSendMqServiceImpl implements SendMqService {

    @Autowired private ApplicationContext applicationContext;

    @Override
    public void send(String topic, String jsonValue, String tagId) {
        AustinSpringEventSource source =
                AustinSpringEventSource.builder()
                        .topic(topic)
                        .jsonValue(jsonValue)
                        .tagId(tagId)
                        .build();
        AustinSpringEventBusEvent austinSpringEventBusEvent =
                new AustinSpringEventBusEvent(this, source);
        applicationContext.publishEvent(austinSpringEventBusEvent);
    }

    @Override
    public void send(String topic, String jsonValue) {
        send(topic, jsonValue, null);
    }
}

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

package com.taotao.cloud.payment.biz.jeepay.mq.vender.activemq;

import com.jeequan.jeepay.components.mq.constant.MQVenderCS;
import com.jeequan.jeepay.components.mq.model.AbstractMQ;
import com.jeequan.jeepay.components.mq.vender.IMQSender;
import jakarta.jms.TextMessage;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * activeMQ 消息发送器的实现
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/7/23 16:52
 */
@Component
@ConditionalOnProperty(name = MQVenderCS.YML_VENDER_KEY, havingValue = MQVenderCS.ACTIVE_MQ)
public class ActiveMQSender implements IMQSender {

    @Autowired private ActiveMQConfig activeMQConfig;

    @Autowired private JmsTemplate jmsTemplate;

    @Override
    public void send(AbstractMQ mqModel) {
        jmsTemplate.convertAndSend(activeMQConfig.getDestination(mqModel), mqModel.toMessage());
    }

    @Override
    public void send(AbstractMQ mqModel, int delay) {
        jmsTemplate.send(
                activeMQConfig.getDestination(mqModel),
                session -> {
                    TextMessage tm = session.createTextMessage(mqModel.toMessage());
                    tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay * 1000);
                    tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 1 * 1000);
                    tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 1);
                    return tm;
                });
    }
}

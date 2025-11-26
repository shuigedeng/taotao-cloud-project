package com.taotao.cloud.message.biz.austin.handler.receiver.springeventbus;

import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.message.biz.austin.common.domain.RecallTaskInfo;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.handler.receiver.MessageReceiver;
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
public class SpringEventBusReceiverListener implements ApplicationListener<AustinSpringEventBusEvent>, MessageReceiver {

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
            springEventBusReceiver.recall(JSON.parseObject(jsonValue, RecallTaskInfo.class));
        }
    }
}

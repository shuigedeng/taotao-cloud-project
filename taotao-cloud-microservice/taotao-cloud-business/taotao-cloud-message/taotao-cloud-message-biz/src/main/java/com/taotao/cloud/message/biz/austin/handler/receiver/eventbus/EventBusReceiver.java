package com.taotao.cloud.message.biz.austin.handler.receiver.eventbus;

import com.google.common.eventbus.Subscribe;
import com.taotao.cloud.message.biz.austin.common.domain.RecallTaskInfo;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.handler.receiver.MessageReceiver;
import com.taotao.cloud.message.biz.austin.handler.receiver.service.ConsumeService;
import com.taotao.cloud.message.biz.austin.support.constans.MessageQueuePipeline;
import com.taotao.cloud.message.biz.austin.support.mq.eventbus.EventBusListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shuigedeng
 */
@Component
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.EVENT_BUS)
public class EventBusReceiver implements EventBusListener, MessageReceiver {

    @Autowired
    private ConsumeService consumeService;

    @Override
    @Subscribe
    public void consume(List<TaskInfo> lists) {
        consumeService.consume2Send(lists);

    }

    @Override
    @Subscribe
    public void recall(RecallTaskInfo recallTaskInfo) {
        consumeService.consume2recall(recallTaskInfo);
    }
}

package com.taotao.cloud.message.biz.austin.handler.receiver.eventbus;

import com.google.common.eventbus.Subscribe;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.handler.receiver.service.ConsumeService;
import com.taotao.cloud.message.biz.austin.support.constans.MessageQueuePipeline;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import com.taotao.cloud.message.biz.austin.support.mq.eventbus.EventBusListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 3y
 */
@Component
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.EVENT_BUS)
public class EventBusReceiver implements EventBusListener {

	@Autowired
	private ConsumeService consumeService;

	@Override
	@Subscribe
	public void consume(List<TaskInfo> lists) {
		consumeService.consume2Send(lists);

	}

	@Override
	@Subscribe
	public void recall(MessageTemplate messageTemplate) {
		consumeService.consume2recall(messageTemplate);
	}
}

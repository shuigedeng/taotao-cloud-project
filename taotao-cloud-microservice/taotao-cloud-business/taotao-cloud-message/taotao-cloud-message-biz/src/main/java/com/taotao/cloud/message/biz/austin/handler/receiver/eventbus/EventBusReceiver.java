package com.taotao.cloud.message.biz.austin.handler.receiver.eventbus;

import com.google.common.eventbus.Subscribe;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.handler.receiver.service.ConsumeService;
import com.java3y.austin.support.constans.MessageQueuePipeline;
import com.java3y.austin.support.domain.MessageTemplate;
import com.java3y.austin.support.mq.eventbus.EventBusListener;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

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
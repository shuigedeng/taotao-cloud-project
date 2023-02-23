package com.taotao.cloud.message.biz.austin.handler.receiver.springeventbus;

import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.handler.receiver.service.ConsumeService;
import com.taotao.cloud.message.biz.austin.support.constans.MessageQueuePipeline;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 描述：
 *
 * @author tony
 * @date 2023/2/6 11:18
 */
@Component
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.SPRING_EVENT_BUS)
public class SpringEventBusReceiver {

	@Autowired
	private ConsumeService consumeService;

	public void consume(List<TaskInfo> lists) {
		consumeService.consume2Send(lists);
	}

	public void recall(MessageTemplate messageTemplate) {
		consumeService.consume2recall(messageTemplate);
	}
}

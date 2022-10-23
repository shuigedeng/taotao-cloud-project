package com.taotao.cloud.mq.stream.framework.trigger.delay.queue;

import com.taotao.cloud.mq.stream.framework.trigger.delay.AbstractDelayQueueMachineFactory;
import com.taotao.cloud.mq.stream.framework.trigger.enums.DelayQueueEnums;
import org.springframework.stereotype.Component;

/**
 * 促销延迟队列
 */
@Component
public class PromotionDelayQueue extends AbstractDelayQueueMachineFactory {

	@Override
	public String getDelayQueueName() {
		return DelayQueueEnums.PROMOTION.name();
	}
}

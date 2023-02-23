package com.taotao.cloud.message.biz.austin.support.mq.eventbus;


import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import java.util.List;

/**
 * @author 3y 监听器
 */
public interface EventBusListener {


	/**
	 * 消费消息
	 *
	 * @param lists
	 */
	void consume(List<TaskInfo> lists);

	/**
	 * 撤回消息
	 *
	 * @param messageTemplate
	 */
	void recall(MessageTemplate messageTemplate);
}

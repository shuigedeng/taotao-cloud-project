package com.taotao.cloud.message.biz.austin.handler.receiver.service;


import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import java.util.List;

/**
 * 消费消息服务
 *
 * @author 3y
 */
public interface ConsumeService {

	/**
	 * 从MQ拉到消息进行消费，发送消息
	 *
	 * @param taskInfoLists
	 */
	void consume2Send(List<TaskInfo> taskInfoLists);


	/**
	 * 从MQ拉到消息进行消费，撤回消息
	 *
	 * @param messageTemplate
	 */
	void consume2recall(MessageTemplate messageTemplate);


}

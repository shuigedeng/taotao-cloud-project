package com.taotao.cloud.mq.stream.consumer.trigger;

/**
 * 延时任务执行器接口
 */
public interface TimeTriggerExecutor {

	/**
	 * 执行任务
	 *
	 * @param object 任务参数
	 */
	void execute(Object object);

}

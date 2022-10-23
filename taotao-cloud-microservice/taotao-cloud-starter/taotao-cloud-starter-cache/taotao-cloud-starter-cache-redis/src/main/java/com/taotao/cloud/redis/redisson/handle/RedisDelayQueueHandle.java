package com.taotao.cloud.redis.redisson.handle;


/**
 * 延迟队列执行器
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-30 09:03:51
 */
public interface RedisDelayQueueHandle {


	/**
	 * 执行延迟队列
	 *
	 * @param obj obj
	 * @since 2022-06-30 08:50:49
	 */
	void execute(Object obj);

}

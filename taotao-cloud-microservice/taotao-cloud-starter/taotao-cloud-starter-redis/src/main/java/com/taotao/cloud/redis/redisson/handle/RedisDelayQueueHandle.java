package com.taotao.cloud.redis.redisson.handle;

/**
 * 延迟队列执行器
 */
public interface RedisDelayQueueHandle<T> {

	/**
	 * 执行延迟队列
	 *
	 * @param t t
	 * @author shuigedeng
	 * @since 2022-01-29 16:14:18
	 */
	void execute(T t);

}

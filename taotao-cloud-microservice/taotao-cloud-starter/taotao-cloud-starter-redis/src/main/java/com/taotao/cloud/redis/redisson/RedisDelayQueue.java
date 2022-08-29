
package com.taotao.cloud.redis.redisson;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 封装 Redis 延迟队列工具
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:45:41
 */
public class RedisDelayQueue {

	@Autowired
	private RedissonClient redissonClient;

	/**
	 * 添加延迟队列
	 *
	 * @param value     队列值
	 * @param delay     延迟时间
	 * @param timeUnit  时间单位
	 * @param queueCode 队列键
	 * @since 2022-04-27 17:45:48
	 */
	public <T> void addDelayQueue(T value, long delay, TimeUnit timeUnit, String queueCode) {
		try {
			RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueCode);
			RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
			delayedQueue.offer(value, delay, timeUnit);
			LogUtils.info("添加延时队列成功，队列键：{}，队列值：{}，延迟时间：{}", queueCode, value,
				timeUnit.toSeconds(delay) + "秒");
		} catch (Exception e) {
			LogUtils.error("添加延时队列失败：{}", e.getMessage());
			throw new RuntimeException("添加延时队列失败");
		}
	}

	/**
	 * 获取延迟队列
	 *
	 * @param queueCode
	 * @return {@link T }
	 * @since 2022-04-27 17:45:48
	 */
	public <T> T getDelayQueue(String queueCode) throws InterruptedException {
		RBlockingDeque<Map> blockingDeque = redissonClient.getBlockingDeque(queueCode);
		T value = (T) blockingDeque.take();
		return value;
	}
}

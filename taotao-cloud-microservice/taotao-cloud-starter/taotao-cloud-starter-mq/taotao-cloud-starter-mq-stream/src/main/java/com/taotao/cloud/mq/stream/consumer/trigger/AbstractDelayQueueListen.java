package com.taotao.cloud.mq.stream.consumer.trigger;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.utils.collection.CollectionUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.DefaultTypedTuple;

/**
 * 延时队列工厂
 */
public abstract class AbstractDelayQueueListen implements ApplicationRunner {

	@Autowired
	private RedisRepository redisRepository;

	/**
	 * 延时队列机器开始运作
	 */
	private void startDelayQueueMachine() {
		LogUtils.info("延时队列机器{}开始运作", setDelayQueueName());

		//监听redis队列
		while (true) {
			try {
				//获取当前时间的时间戳
				long now = System.currentTimeMillis() / 1000;
				//获取当前时间前需要执行的任务列表
				Set<Object> tuples = redisRepository.zRangeByScore(setDelayQueueName(), 0, now);

				//如果任务不为空
				if (!CollectionUtils.isEmpty(tuples)) {
					LogUtils.info("执行任务:{}", JSONUtil.toJsonStr(tuples));

					for (Object t : tuples) {
						DefaultTypedTuple tuple = (DefaultTypedTuple) t;
						String jobId = (String) tuple.getValue();
						//移除缓存，如果移除成功则表示当前线程处理了延时任务，则执行延时任务
						Long num = redisRepository.zRem(setDelayQueueName(), jobId);
						//如果移除成功, 则执行
						if (num > 0) {
							ThreadUtil.execute(() -> invoke(jobId));
						}
					}
				}
			} catch (Exception e) {
				LogUtils.error("处理延时任务发生异常,异常原因为{}", e.getMessage(), e);
			} finally {
				//间隔一秒钟搞一次
				try {
					TimeUnit.SECONDS.sleep(5L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

	}

	/**
	 * 最终执行的任务方法
	 *
	 * @param jobId 任务id
	 */
	public abstract void invoke(String jobId);


	/**
	 * 要实现延时队列的名字
	 *
	 * @return 促销延时队列名称
	 */
	public abstract String setDelayQueueName();


	/**
	 * 监听队列
	 */
	public void init() {
		ThreadUtil.execute(this::startDelayQueueMachine);
	}

}

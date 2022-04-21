package com.taotao.cloud.stream.framework.trigger.delay;

import com.taotao.cloud.common.utils.date.DateUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 延时队列工厂
 */
public abstract class AbstractDelayQueueMachineFactory implements DelayQueueMachine {

	@Autowired
	private RedisRepository cache;

	/**
	 * 插入任务id
	 *
	 * @param jobId       任务id(队列内唯一)
	 * @param triggerTime 执行时间 时间戳（毫秒）
	 * @return 是否插入成功
	 */
	@Override
	public boolean addJob(String jobId, Long triggerTime) {
		//redis 中排序时间
		long delaySeconds = triggerTime / 1000;
		//增加延时任务 参数依次为：队列名称、执行时间、任务id
		boolean result = cache.zAdd(getDelayQueueName(), jobId, delaySeconds);
		LogUtil.info("增加延时任务, 缓存key {}, 执行时间 {},任务id {}", getDelayQueueName(),
			DateUtil.toString(triggerTime), jobId);
		return result;
	}

}

package com.taotao.cloud.redis.redisson;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.redisson.handle.RedisDelayQueueHandle;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import org.springframework.boot.CommandLineRunner;

/**
 * 启动延迟队列
 */
public class RedisDelayQueueRunner implements CommandLineRunner {

	private static ExecutorService executor;

	static {
		init();
	}

	synchronized public static void init() {
		if (null != executor) {
			executor.shutdownNow();
		}

		// 最佳的线程数 = CPU可用核心数 / (1 - 阻塞系数)
		int blockingCoefficient = 0;
		int poolSize = Runtime.getRuntime().availableProcessors() / (1
			- blockingCoefficient);

		executor = ExecutorBuilder.create()
			.setCorePoolSize(poolSize)
			.setMaxPoolSize(poolSize)
			.setKeepAliveTime(0L)
			.setThreadFactory(r -> ThreadFactoryBuilder
				.create()
				.setNamePrefix("taotao-cloud-redis-delay-queue-thread")
				.setDaemon(false)
				.build()
				.newThread(r)
			)
			.useSynchronousQueue()
			.build();
	}

	private final RedisDelayQueue redisDelayQueue;

	public RedisDelayQueueRunner(RedisDelayQueue redisDelayQueue) {
		this.redisDelayQueue = redisDelayQueue;
	}

	@Override
	public void run(String... args) {
		RedisDelayQueueEnum[] queueEnums = RedisDelayQueueEnum.values();
		for (RedisDelayQueueEnum queueEnum : queueEnums) {
			executor.submit(() -> {
				try {
					while (true) {
						Object value = redisDelayQueue.getDelayQueue(queueEnum.getCode());
						if (Objects.isNull(value)) {
							try {
								Thread.sleep(1000);
								continue;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

						RedisDelayQueueHandle redisDelayQueueHandle = ContextUtil.getBean(
							RedisDelayQueueHandle.class,
							queueEnum.getBeanId(),
							true);

						if (Objects.nonNull(redisDelayQueueHandle)) {
							ThreadUtil.execute(() -> redisDelayQueueHandle.execute(value));
							//redisDelayQueueHandle.execute(value);
							LogUtil.info("RedisDelayQueueRunner run success");
						}
					}
				} catch (InterruptedException e) {
					LogUtil.error("(Redis延迟队列异常中断) {}", e.getMessage());
				}
			});
		}
		LogUtil.info("(Redis延迟队列启动成功)");
	}
}

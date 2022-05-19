/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.redis.runner;

import cn.hutool.core.net.NetUtil;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.taotao.cloud.common.support.lock.DistributedLock;
import com.taotao.cloud.common.support.lock.ZLock;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * IdGeneratorCommandLineRunner
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:59:18
 */
public class IdGeneratorCommandLineRunner implements CommandLineRunner, ApplicationContextAware {

	private ApplicationContext applicationContext;

	/**
	 * 分布式锁Key
	 */
	private static final String CACHE_ID_GENERATOR = "LOCK_ID_GENERATOR";

	/**
	 * 最大机器号Key
	 */
	private static final String CACHE_WORKERID_MAXID = "CACHE_WORKERID_MAXID";

	/**
	 * 已分配的机器号Key
	 */
	private static final String CACHE_ID_IP = "CACHE_ID_IP";

	@Override
	public void run(String... args) {
		// AnnotationConfigApplicationContext autowireCapableBeanFactory = (AnnotationConfigApplicationContext )applicationContext;
		// autowireCapableBeanFactory.register(IdGenerator.class);

		idGeneratorWithDistributedLock();
	}

	public void idGeneratorWithDistributedLock() {
		RedisRepository redisRepository = applicationContext.getBean(RedisRepository.class);
		//获取mac地址
		String macAddress = NetUtil.getLocalhost().getHostAddress();
		LogUtil.info("{} 配置分布式workerid 缓存========开始", macAddress);
		boolean existWorkerId = redisRepository.opsForHash().hasKey(CACHE_ID_IP, macAddress);
		//若已缓存在缓存中，直接跳过不设置
		if (existWorkerId) {
			Integer workerId = (Integer) redisRepository.opsForHash().get(CACHE_ID_IP, macAddress);
			LogUtil.info("{} 已配置分布式workerid ...", macAddress);
			initWorkerId(workerId);
			return;
		}

		DistributedLock distributedLock = applicationContext.getBean(DistributedLock.class);
		try {
			//分布式锁等待120秒，执行时长最大120秒
			ZLock locked = distributedLock.tryLock(CACHE_ID_GENERATOR, 120, TimeUnit.SECONDS);
			if (Objects.isNull(locked)) {
				throw new RuntimeException(macAddress + "设置分布式Id机器号失败");
			}

			ValueOperations<String, Object> stringOperation = redisRepository.opsForValue();
			boolean initWorkerId = Boolean.TRUE.equals(stringOperation.setIfAbsent(CACHE_WORKERID_MAXID, 1));
			if (!initWorkerId) {
				//若已存在key，对最大的机器号自增1
				stringOperation.increment(CACHE_WORKERID_MAXID);
			}
			Integer workerId = (Integer) stringOperation.get(CACHE_WORKERID_MAXID);
			initWorkerId(workerId);
			//设置mac地址 - workerid 到hash结构
			redisRepository.opsForHash().put(CACHE_ID_IP, macAddress, workerId);
			LogUtil.info("已配置分布式workerid ,{} - {}", macAddress, workerId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				distributedLock.unlock(CACHE_ID_GENERATOR);
			} catch (Exception e) {
				e.printStackTrace();
			}
			LogUtil.info("{} 配置分布式workerid 缓存 ======== 结束", macAddress);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private void initWorkerId(Integer workerId) {
		if (Objects.nonNull(workerId)) {
			IdGeneratorOptions options = new IdGeneratorOptions(workerId.shortValue());
			YitIdHelper.setIdGenerator(options);
		}
	}

	public static class IdGenerator {
		/**
		 * ID生成器
		 */
		public static String IDGENERATOR = "idgenerator_";
		private static final String ID_IDX = IDGENERATOR + "index_";
		//ID缓存有效时间 定时刷新有效期
		private static final long CacheTimeout = 60 * 60 * 24;
		//30分钟续期一次 如果Redis被清空可以早点续期
		private static final long ScheduleTimeout = 60 * 30;
		private static final byte WorkerIdBitLength = 16;
		//65535
		private static final int MaxWorkerIdNumberByMode = (1 << WorkerIdBitLength) - 1;
		private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

		private short workerId = -1;
		private String cacheKey;
		private RedisRepository redisRepository;

		/**
		 * 初始化雪花生成器WorkerID， 通过Redis实现集群获取不同的编号， 如果相同会出现ID重复
		 */
		@PostConstruct
		private void initIdWorker() {
			RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
			if (Objects.nonNull(redisRepository)) {
				this.redisRepository = redisRepository;

				RedisAtomicLong redisAtomicLong = new RedisAtomicLong(ID_IDX, redisRepository.getConnectionFactory());
				for (int i = 0; i <= MaxWorkerIdNumberByMode; i++) {
					long andInc = redisAtomicLong.getAndIncrement();
					long result = andInc % (MaxWorkerIdNumberByMode + 1);

					//计数超出上限之后重新计数
					if (andInc >= MaxWorkerIdNumberByMode) {
						redisAtomicLong.set(andInc % (MaxWorkerIdNumberByMode));
					}

					cacheKey = ID_IDX + result;
					boolean useSuccess = Boolean.TRUE.equals(redisRepository.opsForValue().setIfAbsent(cacheKey, System.currentTimeMillis(), CacheTimeout, TimeUnit.SECONDS));
					if (useSuccess) {
						workerId = (short) result;
						break;
					}
				}
				if (workerId == -1) {
					throw new RuntimeException(String.format("已尝试生成%d个ID生成器编号, 无法获取到可用编号", MaxWorkerIdNumberByMode + 1));
				}
				LogUtil.info("当前ID生成器编号: " + workerId);
				IdGeneratorOptions options = new IdGeneratorOptions(workerId);
				options.WorkerIdBitLength = WorkerIdBitLength;
				YitIdHelper.setIdGenerator(options);

				ThreadPoolTaskExecutor threadPoolTaskExecutor = ContextUtil.getBean(ThreadPoolTaskExecutor.class, true);
				scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, threadPoolTaskExecutor.getThreadPoolExecutor().getThreadFactory());
				//提前一分钟续期
				scheduledThreadPoolExecutor.scheduleWithFixedDelay(resetExpire, ScheduleTimeout, ScheduleTimeout, TimeUnit.SECONDS);
			}
		}

		@PreDestroy
		private void onDestroy() {
			//正常关闭时删除当前生成器编号
			if (Objects.nonNull(redisRepository)) {
				redisRepository.del(cacheKey);
			}
		}

		private final Runnable resetExpire = () -> {
			if (Objects.nonNull(redisRepository)) {
				//重新设值, 如果Redis被意外清空或者掉线可以把当前编号重新锁定
				redisRepository.opsForValue().set(cacheKey, System.currentTimeMillis(), CacheTimeout, TimeUnit.SECONDS);
			}
		};
	}
}

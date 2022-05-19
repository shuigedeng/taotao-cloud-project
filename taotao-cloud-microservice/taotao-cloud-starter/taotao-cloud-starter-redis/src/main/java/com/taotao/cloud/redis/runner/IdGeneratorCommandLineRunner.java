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
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.properties.IdGeneratorProperties;
import com.taotao.cloud.redis.properties.IdGeneratorProperties.IdGeneratorEnum;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.ValueOperations;

/**
 * IdGeneratorCommandLineRunner
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:59:18
 */
public class IdGeneratorCommandLineRunner implements CommandLineRunner, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Autowired
	private IdGeneratorProperties idGeneratorProperties;

	/**
	 * 分布式锁Key
	 */
	private static final String CACHE_ID_GENERATOR = "LOCK_ID_GENERATOR";

	/**
	 * 最大机器号Key
	 */
	private static final String CACHE_WORKERID_MAXID = "CACHE_WORKER_ID_MAXID";

	/**
	 * 已分配的机器号Key
	 */
	private static final String CACHE_ID_IP = "CACHE_ID_IP";

	@Override
	public void run(String... args) {
		if (idGeneratorProperties.getType().equals(IdGeneratorEnum.REDIS_LOCK)) {
			idGeneratorWithDistributedLock();
		}
	}

	public void idGeneratorWithDistributedLock() {
		RedisRepository redisRepository = applicationContext.getBean(RedisRepository.class);
		//获取mac地址
		String macAddress = NetUtil.getLocalMacAddress();
		boolean existWorkerId = redisRepository.opsForHash().hasKey(CACHE_ID_IP, macAddress);
		//若已缓存在缓存中，直接跳过不设置
		if (existWorkerId) {
			Integer workerId = (Integer) redisRepository.opsForHash().get(CACHE_ID_IP, macAddress);
			LogUtil.info("配置分布式workerId {} - {}", macAddress, workerId);
			initWorkerId(workerId);
			return;
		}

		DistributedLock distributedLock = applicationContext.getBean(DistributedLock.class);
		ZLock lock = null;
		try {
			//分布式锁等待120秒，执行时长最大120秒
			lock = distributedLock.tryLock(CACHE_ID_GENERATOR, 120, TimeUnit.SECONDS);
			if (Objects.isNull(lock)) {
				throw new RuntimeException(macAddress + "设置分布式Id机器号失败");
			}

			ValueOperations<String, Object> stringOperation = redisRepository.opsForValue();
			boolean initWorkerId = Boolean.TRUE.equals(
				stringOperation.setIfAbsent(CACHE_WORKERID_MAXID, 1));
			if (!initWorkerId) {
				//若已存在key，对最大的机器号自增1
				stringOperation.increment(CACHE_WORKERID_MAXID);
			}

			Integer workerId = (Integer) stringOperation.get(CACHE_WORKERID_MAXID);
			initWorkerId(workerId);
			//设置mac地址 - workerId 到hash结构
			redisRepository.opsForHash().put(CACHE_ID_IP, macAddress, workerId);
			LogUtil.info("配置分布式workerId {} - {}", macAddress, workerId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (Objects.nonNull(lock)) {
				try {
					distributedLock.unlock(lock);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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

}

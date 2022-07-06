package com.taotao.cloud.web.idgenerator;

import cn.hutool.core.net.NetUtil;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.taotao.cloud.common.support.lock.DistributedLock;
import com.taotao.cloud.common.support.lock.ZLock;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.ValueOperations;

/**
 * id生成器
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-19 11:18:07
 */
public class RedisLockIdGenerator implements CommandLineRunner, ApplicationContextAware {

	private ApplicationContext applicationContext;

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

	private RedisRepository redisRepository;
	private DistributedLock distributedLock;

	public RedisLockIdGenerator(RedisRepository redisRepository, DistributedLock distributedLock) {
		this.redisRepository = redisRepository;
		this.distributedLock = distributedLock;
	}

	@Override
	public void run(String... args) {
		idGeneratorWithDistributedLock();
	}

	public void idGeneratorWithDistributedLock() {
		if (Objects.isNull(redisRepository)) {
			this.redisRepository = applicationContext.getBean(RedisRepository.class);
		}
		if (Objects.isNull(distributedLock)) {
			this.distributedLock = applicationContext.getBean(DistributedLock.class);
		}

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


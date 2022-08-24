package com.taotao.cloud.idgenerator.generator;

import cn.hutool.core.util.StrUtil;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.common.ThreadFactoryCreator;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

/**
 * id生成器
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-19 11:18:07
 */
public class RedisIdGenerator implements DisposableBean, CommandLineRunner {

	/**
	 * ID缓存有效时间 定时刷新有效期
	 */
	private static final long CACHE_TIMEOUT = 60 * 60 * 24;
	/**
	 * 30分钟续期一次 如果Redis被清空可以早点续期
	 */
	private static final long SCHEDULE_TIMEOUT = 60 * 30;
	private static final byte WORKER_ID_BIT_LENGTH = 16;
	/**
	 * 65535
	 */
	private static final int MAX_WORKER_ID_NUMBER_BY_MODE = (1 << WORKER_ID_BIT_LENGTH) - 1;

	private short workerId = -1;
	private String cacheKey;
	private RedisRepository redisRepository;

	public RedisIdGenerator(RedisRepository redisRepository) {
		this.redisRepository = redisRepository;
	}

	/**
	 * 初始化雪花生成器WorkerID， 通过Redis实现集群获取不同的编号， 如果相同会出现ID重复
	 *
	 * @since 2022-07-22 15:17:33
	 */
	private void initIdWorker() {
		if (Objects.isNull(redisRepository)) {
			this.redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		}

		RedisAtomicLong redisAtomicLong = new RedisAtomicLong(
			RedisConstant.ID_GENERATOR_INDEX_PREFIX,
			redisRepository.getConnectionFactory());

		for (int i = 0; i <= MAX_WORKER_ID_NUMBER_BY_MODE; i++) {
			long andInc = redisAtomicLong.getAndIncrement();
			long result = andInc % (MAX_WORKER_ID_NUMBER_BY_MODE + 1);

			//计数超出上限之后重新计数
			if (andInc >= MAX_WORKER_ID_NUMBER_BY_MODE) {
				redisAtomicLong.set(andInc % (MAX_WORKER_ID_NUMBER_BY_MODE));
			}

			cacheKey = RedisConstant.ID_GENERATOR_INDEX_PREFIX + result;
			boolean useSuccess = Boolean.TRUE.equals(redisRepository.opsForValue()
				.setIfAbsent(cacheKey, System.currentTimeMillis(), CACHE_TIMEOUT, TimeUnit.SECONDS));
			if (useSuccess) {
				workerId = (short) result;
				break;
			}
		}
		if (workerId == -1) {
			throw new RuntimeException(String.format("已尝试生成%d个ID生成器编号, 无法获取到可用编号", MAX_WORKER_ID_NUMBER_BY_MODE + 1));
		}

		LogUtil.info("当前ID生成器编号: " + workerId);
		IdGeneratorOptions options = new IdGeneratorOptions(workerId);
		options.WorkerIdBitLength = WORKER_ID_BIT_LENGTH;
		YitIdHelper.setIdGenerator(options);

		//提前一分钟续期
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
			1,
			ThreadFactoryCreator.create("taotao-cloud-idgenerator-scheduled-task"));
		scheduledThreadPoolExecutor.scheduleWithFixedDelay(resetExpire,
			SCHEDULE_TIMEOUT,
			SCHEDULE_TIMEOUT,
			TimeUnit.SECONDS);
	}

	private final Runnable resetExpire = () -> {
		if (Objects.nonNull(redisRepository)) {
			//重新设值, 如果Redis被意外清空或者掉线可以把当前编号重新锁定
			redisRepository.opsForValue().set(cacheKey, System.currentTimeMillis(), CACHE_TIMEOUT, TimeUnit.SECONDS);
		}
	};

	@Override
	public void destroy() throws Exception {
		//正常关闭时删除当前生成器编号
		if (Objects.nonNull(redisRepository) && StrUtil.isBlank(cacheKey)) {
			redisRepository.del(cacheKey);
		}
	}

	@Override
	public void run(String... args) throws Exception {
		initIdWorker();
	}
}

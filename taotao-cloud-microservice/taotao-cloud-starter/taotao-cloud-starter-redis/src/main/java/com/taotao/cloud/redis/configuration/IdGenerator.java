package com.taotao.cloud.redis.configuration;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

@Configuration
@ConditionalOnBean({RedisRepository.class})
public class IdGenerator implements DisposableBean, InitializingBean {

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
	private void initIdWorker() {
		RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		if (Objects.nonNull(redisRepository)) {
			this.redisRepository = redisRepository;

			RedisAtomicLong redisAtomicLong = new RedisAtomicLong(ID_IDX,
				redisRepository.getConnectionFactory());
			for (int i = 0; i <= MaxWorkerIdNumberByMode; i++) {
				long andInc = redisAtomicLong.getAndIncrement();
				long result = andInc % (MaxWorkerIdNumberByMode + 1);

				//计数超出上限之后重新计数
				if (andInc >= MaxWorkerIdNumberByMode) {
					redisAtomicLong.set(andInc % (MaxWorkerIdNumberByMode));
				}

				cacheKey = ID_IDX + result;
				boolean useSuccess = Boolean.TRUE.equals(redisRepository.opsForValue()
					.setIfAbsent(cacheKey, System.currentTimeMillis(), CacheTimeout,
						TimeUnit.SECONDS));
				if (useSuccess) {
					workerId = (short) result;
					break;
				}
			}
			if (workerId == -1) {
				throw new RuntimeException(
					String.format("已尝试生成%d个ID生成器编号, 无法获取到可用编号", MaxWorkerIdNumberByMode + 1));
			}
			LogUtil.info("当前ID生成器编号: " + workerId);
			IdGeneratorOptions options = new IdGeneratorOptions(workerId);
			options.WorkerIdBitLength = WorkerIdBitLength;
			YitIdHelper.setIdGenerator(options);

			//提前一分钟续期
			scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1,
				(t) -> new Thread("taotao-cloud-idgenerator-scheduled-task"));
			scheduledThreadPoolExecutor.scheduleWithFixedDelay(resetExpire, ScheduleTimeout,
				ScheduleTimeout, TimeUnit.SECONDS);
		}
	}

	private void onDestroy() {
		//正常关闭时删除当前生成器编号
		if (Objects.nonNull(redisRepository)) {
			redisRepository.del(cacheKey);
		}
	}

	private final Runnable resetExpire = () -> {
		if (Objects.nonNull(redisRepository)) {
			//重新设值, 如果Redis被意外清空或者掉线可以把当前编号重新锁定
			redisRepository.opsForValue()
				.set(cacheKey, System.currentTimeMillis(), CacheTimeout, TimeUnit.SECONDS);
		}
	};

	@Override
	public void destroy() throws Exception {
		onDestroy();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initIdWorker();
	}
}

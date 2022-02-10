package com.taotao.cloud.web.schedule.core;

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.web.schedule.ScheduledException;
import com.taotao.cloud.web.schedule.core.interceptor.ScheduledRunnable;
import com.taotao.cloud.web.schedule.model.ScheduledJobModel;
import com.taotao.cloud.web.schedule.properties.ScheduledPluginProperties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * SuperScheduledManager
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:01:18
 */
public class ScheduledManager {

	protected final Log logger = LogFactory.getLog(getClass());

	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private ScheduledConfig scheduledConfig;

	@Autowired
	private ScheduledPluginProperties plugInPropertiesScheduled;

	/**
	 * 修改Scheduled的执行周期
	 *
	 * @param name scheduled的名称
	 * @param cron cron表达式
	 */
	public void setScheduledCron(String name, String cron) {
		//终止原先的任务
		cancelScheduled(name);
		//创建新的任务
		ScheduledJobModel scheduledJobModel = scheduledConfig.getScheduledSource(name);
		scheduledJobModel.clear();
		scheduledJobModel.setCron(cron);
		scheduledJobModel.setCancel(false);
		addScheduled(name, scheduledJobModel);

		RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.SCHEDULED_UPDATE_CRON_TOPIC, scheduledJobModel);
		}
	}

	/**
	 * 修改Scheduled的fixedDelay
	 *
	 * @param name       scheduled的名称
	 * @param fixedDelay 上一次执行完毕时间点之后多长时间再执行
	 */
	public void setScheduledFixedDelay(String name, Long fixedDelay) {
		//终止原先的任务
		cancelScheduled(name);
		//创建新的任务
		ScheduledJobModel scheduledJobModel = scheduledConfig.getScheduledSource(name);
		scheduledJobModel.clear();
		scheduledJobModel.setFixedDelay(fixedDelay);
		scheduledJobModel.setCancel(false);
		addScheduled(name, scheduledJobModel);

		RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.SCHEDULED_UPDATE_FIXED_DELAY_TOPIC,
				scheduledJobModel);
		}
	}

	/**
	 * 修改Scheduled的fixedRate
	 *
	 * @param name      scheduled的名称
	 * @param fixedRate 上一次开始执行之后多长时间再执行
	 */
	public void setScheduledFixedRate(String name, Long fixedRate) {
		//终止原先的任务
		cancelScheduled(name);
		//创建新的任务
		ScheduledJobModel scheduledJobModel = scheduledConfig.getScheduledSource(name);
		scheduledJobModel.clear();
		scheduledJobModel.setFixedRate(fixedRate);
		scheduledJobModel.setCancel(false);
		addScheduled(name, scheduledJobModel);

		RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.SCHEDULED_UPDATE_FIXED_RATE_TOPIC,
				scheduledJobModel);
		}
	}

	/**
	 * 查询所有启动的Scheduled
	 */
	public List<String> getRunScheduledName() {
		Set<String> names = scheduledConfig.getNameToScheduledFuture().keySet();
		return new ArrayList<>(names);
	}

	/**
	 * 查询所有的Scheduled
	 */
	public List<String> getAllSuperScheduledName() {
		Set<String> names = scheduledConfig.getNameToRunnable().keySet();
		return new ArrayList<>(names);
	}

	/**
	 * 终止Scheduled
	 *
	 * @param name scheduled的名称
	 */
	public void cancelScheduled(String name) {
		ScheduledFuture scheduledFuture = scheduledConfig.getScheduledFuture(name);
		scheduledFuture.cancel(true);
		scheduledConfig.removeScheduledFuture(name);
		logger.info(df.format(LocalDateTime.now()) + "任务" + name + "已经终止...");

		ScheduledJobModel scheduledJobModel = scheduledConfig.getScheduledSource(name);
		scheduledJobModel.setCancel(true);
		RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.SCHEDULED_CANCEL_TOPIC, scheduledJobModel);
		}
	}

	/**
	 * 启动Scheduled
	 *
	 * @param name              scheduled的名称
	 * @param scheduledJobModel 定时任务的源信息
	 */
	public void addScheduled(String name, ScheduledJobModel scheduledJobModel) {
		if (getRunScheduledName().contains(name)) {
			throw new ScheduledException("定时任务" + name + "已经被启动过了");
		}
		if (!scheduledJobModel.check()) {
			throw new ScheduledException("定时任务" + name + "源数据内容错误");
		}

		scheduledJobModel.refreshType();

		Runnable runnable = scheduledConfig.getRunnable(name);
		ThreadPoolTaskScheduler taskScheduler = scheduledConfig.getTaskScheduler();

		ScheduledFuture<?> schedule = ScheduledFutureFactory.create(taskScheduler,
			scheduledJobModel,
			runnable);
		logger.info(df.format(LocalDateTime.now()) + "任务" + name + "已经启动...");

		scheduledConfig.addScheduledSource(name, scheduledJobModel);
		scheduledConfig.addScheduledFuture(name, schedule);

		//scheduledSource.setCancel(false);
		//RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		//if (Objects.nonNull(redisRepository)) {
		//	redisRepository.send(RedisConstant.SCHEDULED_START_TOPIC, scheduledSource);
		//}
	}

	/**
	 * 以cron类型启动Scheduled
	 *
	 * @param name scheduled的名称
	 * @param cron cron表达式
	 */
	public void addCronScheduled(String name, String cron) {
		ScheduledJobModel scheduledJobModel = new ScheduledJobModel();
		scheduledJobModel.setCron(cron);
		scheduledJobModel.setCancel(false);

		addScheduled(name, scheduledJobModel);

		RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.SCHEDULED_ADD_CRON_TOPIC, scheduledJobModel);
		}
	}

	/**
	 * 以fixedDelay类型启动Scheduled
	 *
	 * @param name         scheduled的名称
	 * @param fixedDelay   上一次执行完毕时间点之后多长时间再执行
	 * @param initialDelay 第一次执行的延迟时间
	 */
	public void addFixedDelayScheduled(String name, Long fixedDelay, Long... initialDelay) {
		ScheduledJobModel scheduledJobModel = new ScheduledJobModel();
		scheduledJobModel.setFixedDelay(fixedDelay);
		scheduledJobModel.setCancel(false);
		if (initialDelay != null && initialDelay.length == 1) {
			scheduledJobModel.setInitialDelay(initialDelay[0]);
		} else if (initialDelay != null && initialDelay.length > 1) {
			throw new ScheduledException("第一次执行的延迟时间只能传入一个参数");
		}

		addScheduled(name, scheduledJobModel);

		RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.SCHEDULED_ADD_FIXED_DELAY_TOPIC, scheduledJobModel);
		}
	}

	/**
	 * 以fixedRate类型启动Scheduled
	 *
	 * @param name         scheduled的名称
	 * @param fixedRate    上一次开始执行之后多长时间再执行
	 * @param initialDelay 第一次执行的延迟时间
	 */
	public void addFixedRateScheduled(String name, Long fixedRate, Long... initialDelay) {
		ScheduledJobModel scheduledJobModel = new ScheduledJobModel();
		scheduledJobModel.setFixedRate(fixedRate);
		scheduledJobModel.setCancel(false);
		if (initialDelay != null && initialDelay.length == 1) {
			scheduledJobModel.setInitialDelay(initialDelay[0]);
		} else if (initialDelay != null && initialDelay.length > 1) {
			throw new ScheduledException("第一次执行的延迟时间只能传入一个参数");
		}

		addScheduled(name, scheduledJobModel);

		RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.SCHEDULED_ADD_FIXED_RATE_TOPIC, scheduledJobModel);
		}
	}

	/**
	 * 手动执行一次任务
	 *
	 * @param name scheduled的名称
	 */
	public void runScheduled(String name) {
		Runnable runnable = scheduledConfig.getRunnable(name);
		runnable.run();

		ScheduledJobModel scheduledJobModel = scheduledConfig.getScheduledSource(name);
		scheduledJobModel.setNum(scheduledJobModel.getNum() + 1);
		RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.SCHEDULED_RUN_ONCE_TOPIC, scheduledJobModel);
		}
	}

	/**
	 * 结束正在执行中的任务，跳过这次运行 只有在每个前置增强器结束之后才会判断是否需要跳过此次运行
	 *
	 * @param name scheduled的名称
	 */
	public void callOffScheduled(String name) {
		ScheduledRunnable scheduledRunnable = scheduledConfig.getSuperScheduledRunnable(
			name);
		scheduledRunnable.getContext().setCallOff(true);

		ScheduledJobModel scheduledJobModel = scheduledConfig.getScheduledSource(name);
		scheduledJobModel.setNum(scheduledJobModel.getNum() + 1);
		RedisRepository redisRepository = ContextUtil.getBean(RedisRepository.class, true);
		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.SCHEDULED_RUN_ONCE_TOPIC, scheduledJobModel);
		}
	}

	///**
	// * 获取日志信息
	// *
	// * @param fileName 日志文件名
	// */
	//public List<ScheduledLogModel> getScheduledLogs(String fileName) {
	//	String logPath = plugInPropertiesScheduled.getLogPath();
	//	List<ScheduledLogModel> scheduledLogModels = SerializableUtils.fromIncFile(logPath, fileName);
	//	return scheduledLogModels;
	//}
	//
	///**
	// * 获取日志文件信息
	// */
	//public List<ScheduledLogFile> getScheduledLogFiles() {
	//	String logPath = plugInPropertiesScheduled.getLogPath();
	//	return SerializableUtils.getScheduledLogFiles(logPath);
	//}
}

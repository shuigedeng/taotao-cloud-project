package com.taotao.cloud.web.schedule;

import com.taotao.cloud.common.utils.LogUtil;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;

/**
 * 抽象 Dynamic Schedule 实现，基于 SchedulingConfigurer 实现
 *
 * @author jitwxs
 * @date 2021年03月27日 16:41
 */
public abstract class AbstractDSHandler<T extends IDSTaskInfo> implements SchedulingConfigurer {

	private DSContainer<T> dsContainer;

	private final String CLASS_NAME = getClass().getSimpleName();

	/**
	 * 获取所有的任务信息
	 */
	protected abstract List<T> listTaskInfo();

	/**
	 * 做具体的任务逻辑
	 * <p>
	 * <p/> 该方法执行时位于跟 SpringBoot @Scheduled 注解相同的线程池内。如果内部仍需要开子线程池执行，请务必同步等待子线程池执行完毕，否则可能会影响预期效果。
	 */
	protected abstract void doProcess(T taskInfo) throws Throwable;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		dsContainer = new DSContainer<>(taskRegistrar, CLASS_NAME);
		// 每隔 100ms 调度一次，用于读取所有任务
		taskRegistrar.addFixedDelayTask(this::scheduleTask, 1000);
	}

	/**
	 * 调度任务，加载所有任务并注册
	 */
	private void scheduleTask() {
		CollectionUtils.emptyIfNull(listTaskInfo()).forEach(taskInfo ->
			dsContainer.checkTask(taskInfo, new TriggerTask(() ->
				this.execute(taskInfo),
				triggerContext -> new CronTrigger(taskInfo.getCron()).nextExecutionTime(
					triggerContext)
			))
		);
	}

	private void execute(final T taskInfo) {
		final long taskId = taskInfo.getId();

		try {
			Semaphore semaphore = dsContainer.getSemaphore(taskId);
			if (Objects.isNull(semaphore)) {
				LogUtil.error("{} semaphore is null, taskId: {}", CLASS_NAME, taskId);
				return;
			}

			if (semaphore.tryAcquire(3, TimeUnit.SECONDS)) {
				try {
					doProcess(taskInfo);
				} catch (Throwable throwable) {
					LogUtil.error("{} doProcess error, taskId: {}", CLASS_NAME, taskId, throwable);
				} finally {
					semaphore.release();
				}
			} else {
				LogUtil.warn("{} too many executor, taskId: {}", CLASS_NAME, taskId);
			}

		} catch (InterruptedException e) {
			LogUtil.warn("{} interruptedException error, taskId: {}", CLASS_NAME, taskId);
		} catch (Exception e) {
			LogUtil.error("{} execute error, taskId: {}", CLASS_NAME, taskId, e);
		}
	}
}

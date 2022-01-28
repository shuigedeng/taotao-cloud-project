package com.taotao.cloud.web.schedule;

import com.taotao.cloud.common.utils.LogUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;

/**
 * 存放 IDSTaskInfo 容器
 *
 * @author jitwxs
 * @date 2021年03月27日 16:29
 */
public class DSContainer<T extends IDSTaskInfo> {

	/**
	 * IDSTaskInfo和真实任务的关联关系
	 * <p>
	 * <task_id, <Task, <Scheduled, Semaphore>>>
	 */
	private final Map<Long, Pair<T, Pair<ScheduledTask, Semaphore>>> scheduleMap = new ConcurrentHashMap<>();

	private final ScheduledTaskRegistrar taskRegistrar;

	private final String name;

	public DSContainer(ScheduledTaskRegistrar scheduledTaskRegistrar, final String name) {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(10);
		taskScheduler.setThreadNamePrefix("taotao-cloud-scheduling-executor");
		taskScheduler.setErrorHandler(LogUtil::error);
		taskScheduler.initialize();
		scheduledTaskRegistrar.setTaskScheduler(taskScheduler);

		this.taskRegistrar = scheduledTaskRegistrar;
		this.name = name;
	}

	/**
	 * 注册任务
	 *
	 * @param taskInfo    任务信息
	 * @param triggerTask 任务的触发规则
	 */
	public void checkTask(final T taskInfo, final TriggerTask triggerTask) {
		final long taskId = taskInfo.getId();

		if (scheduleMap.containsKey(taskId)) {
			if (taskInfo.isValid()) {
				final T oldTaskInfo = scheduleMap.get(taskId).getLeft();

				if (oldTaskInfo.isChange(taskInfo)) {
					LogUtil.info(
						"DSContainer will register {} again because task config change, taskId: {}",
						name, taskId);
					cancelTask(taskId);
					registerTask(taskInfo, triggerTask);
				}
			} else {
				LogUtil.info("DSContainer will cancelTask {} because task not valid, taskId: {}",
					name, taskId);
				cancelTask(taskId);
			}
		} else {
			if (taskInfo.isValid()) {
				LogUtil.info("DSContainer will register {} task, taskId: {}", name, taskId);
				registerTask(taskInfo, triggerTask);
			}
		}
	}

	/**
	 * 获取 Semaphore，确保任务不会被多个线程同时执行
	 */
	public Semaphore getSemaphore(final long taskId) {
		return this.scheduleMap.get(taskId).getRight().getRight();
	}

	public void registerTask(final T taskInfo, final TriggerTask triggerTask) {
		final ScheduledTask latestTask = taskRegistrar.scheduleTriggerTask(triggerTask);
		this.scheduleMap.put(taskInfo.getId(),
			Pair.of(taskInfo, Pair.of(latestTask, new Semaphore(1))));
	}

	public void cancelTask(final long taskId) {
		final Pair<T, Pair<ScheduledTask, Semaphore>> pair = this.scheduleMap.remove(taskId);
		if (pair != null) {
			pair.getRight().getLeft().cancel();
		}
	}
}

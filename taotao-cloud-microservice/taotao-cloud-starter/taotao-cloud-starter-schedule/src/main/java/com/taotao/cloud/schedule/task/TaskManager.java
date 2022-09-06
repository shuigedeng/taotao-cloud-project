package com.taotao.cloud.schedule.task;

import com.taotao.cloud.schedule.model.constant.TaskPolicyConstant;
import com.taotao.cloud.schedule.model.constant.TaskRunTypeConstant;
import com.taotao.cloud.schedule.model.entity.Task;
import com.taotao.cloud.schedule.utils.CronUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class TaskManager {
	private final Logger logger = LoggerFactory.getLogger(TaskManager.class);

	/**
	 * ConcurrentHashMap保证线程安全
	 */
	private final Map<String, ScheduledFuture<?>> taskScheduledMap = new ConcurrentHashMap<>();

	/**
	 * 存入被执行的中的task实体 key:taskId,value:task对象
	 */
	private Map<String, Task> taskMap = new ConcurrentHashMap<>();

	/**
	 * 任务线程最大值，可以根据情况调整
	 */
	private static final int MAX_POOL_SIZE = 20;

	/**
	 * 引用自定义配置Bean对象
	 */
	@Resource(name = "taskScheduler")
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Resource
	private TaskMapper taskMapper;

	public void initTask() {
		// 读取自动执行任务列表
		List<Task> tasks = taskMapper.taskList();
		// 过滤获取自动执行或者运行中的任务
		List<Task> startTasks = tasks.stream().filter(t -> t.getPolicy() == 2 || t.getSituation() == 1).toList();
		logger.info("初始化任务列表：{}", startTasks.size());
		// 设置线程大小，根据当前任务的并发情况设置，最大值为 MaxPoolSize，
		int taskSize = startTasks.size();
		int poolSize = taskSize == 0 ? MAX_POOL_SIZE : Math.min(taskSize, MAX_POOL_SIZE);
		threadPoolTaskScheduler.setPoolSize(poolSize);
		for (Task task : startTasks) {
			this.start(task, TaskRunTypeConstant.SYSTEM_RUN);
		}
	}

	public void destroyTask() {
		logger.info("######## 结束任务 #########");
		//查询运行中的任务，进行停止操作
		clear();
	}

	public void start(Task task, String runType) {
		String taskId = task.getId();
		Integer policy = task.getPolicy();
		String cron = task.getCronExpression();

		//创建任务
		logger.info("======== 创建任务：{} ========", taskId);

		//如果线程已存在则先关闭，再开启新的
		if (taskScheduledMap.get(taskId) != null) {
			logger.info("重复任务：" + taskId);
			close(taskId);
		}

		//执行start(自动策略或者人为触发)
		if (policy.equals(TaskPolicyConstant.AUTO) || runType.equals(TaskRunTypeConstant.SYSTEM_RUN)) {
			logger.info("======== 执行任务：{} ========", taskId);
			// 每次执行时，将读取的下次执行修改为真实的下次执行下级
			Date nextCurrentTime = CronUtils.nextCurrentTime(task.getCronExpression());
			task.setNextRunTime(nextCurrentTime);
			TaskRunnable taskRunnable = new TaskRunnable(taskId);

			// 将task对象加入内存
			taskMap.put(taskId, task);

			// 执行TaskRunnable的run方法
			ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(taskRunnable, new CronTrigger(cron));
			taskScheduledMap.put(taskId, schedule);

			// 更新任务状态为已执行
			// 执行中
			task.setSituation(1);
			taskMapper.update(task);
		}
	}

	/**
	 * 关闭redis的任务
	 *
	 * @param taskId
	 */
	public void close(String taskId) {
		if (taskScheduledMap.get(taskId) != null) {
			ScheduledFuture<?> scheduledFuture = taskScheduledMap.get(taskId);
			scheduledFuture.cancel(true);
			taskScheduledMap.remove(taskId);
			taskMap.remove(taskId);
			logger.info("关闭任务：" + taskId);
		}
	}

	public void stop(String taskId) {
		if (taskScheduledMap.get(taskId) != null) {
			ScheduledFuture<?> scheduledFuture = taskScheduledMap.get(taskId);
			// 调用取消
			// 如果参数为true并且任务正在运行，那么这个任务将被取消
			// 如果参数为false并且任务正在运行，那么这个任务将不会被取消
			scheduledFuture.cancel(true);
			taskScheduledMap.remove(taskId);
			taskMap.remove(taskId);
			logger.info("停止任务：" + taskId);

			//修改任务状况为停止
			Task task = new Task();
			task.setId(taskId);
			//已暂停
			task.setSituation(2);
			taskMapper.update(task);
		}
	}

	public void clear() {
		for (Map.Entry<String, ScheduledFuture<?>> entry : taskScheduledMap.entrySet()) {
			String taskId = entry.getKey();
			stop(taskId);
		}
	}


	public Map<String, Task> getTaskMap() {
		return taskMap;
	}

	public void setTaskMap(Map<String, Task> taskMap) {
		this.taskMap = taskMap;
	}
}

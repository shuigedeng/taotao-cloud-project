package com.taotao.cloud.schedule.core;

import com.taotao.cloud.schedule.core.interceptor.ScheduledRunnable;
import com.taotao.cloud.schedule.model.ScheduledJobModel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * ScheduledConfig
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 16:58:50
 */
public class ScheduledConfig {

	/**
	 * 执行定时任务的线程池
	 */
	private ThreadPoolTaskScheduler taskScheduler;

	/**
	 * 定时任务名称与定时任务回调钩子  的关联关系容器
	 */
	private Map<String, ScheduledFuture> nameToScheduledFuture = new ConcurrentHashMap<>();

	/**
	 * 定时任务名称与定时任务需要执行的逻辑  的关联关系容器
	 */
	private Map<String, Runnable> nameToRunnable = new ConcurrentHashMap<>();

	/**
	 * 定时任务名称与任务执行器  的关联关系容器
	 */
	private Map<String, ScheduledRunnable> nameToSuperScheduledRunnable = new ConcurrentHashMap<>();

	/**
	 * 定时任务名称与定时任务的源信息  的关联关系容器
	 */
	private Map<String, ScheduledJobModel> nameToScheduledSource = new ConcurrentHashMap<>();


	public void addScheduledSource(String name, ScheduledJobModel scheduledJobModel) {
		this.nameToScheduledSource.put(name, scheduledJobModel);
	}

	public ScheduledJobModel getScheduledSource(String name) {
		return nameToScheduledSource.get(name);
	}

	public void addSuperScheduledRunnable(String name,
		ScheduledRunnable scheduledRunnable) {
		this.nameToSuperScheduledRunnable.put(name, scheduledRunnable);
	}

	public ScheduledRunnable getSuperScheduledRunnable(String name) {
		return nameToSuperScheduledRunnable.get(name);
	}

	public Runnable getRunnable(String name) {
		return nameToRunnable.get(name);
	}

	public ScheduledFuture getScheduledFuture(String name) {
		return nameToScheduledFuture.get(name);
	}

	public void addScheduledFuture(String name, ScheduledFuture scheduledFuture) {
		this.nameToScheduledFuture.put(name, scheduledFuture);
	}

	public void addRunnable(String name, Runnable runnable) {
		this.nameToRunnable.put(name, runnable);
	}

	public ThreadPoolTaskScheduler getTaskScheduler() {
		return taskScheduler;
	}

	public void setTaskScheduler(ThreadPoolTaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}

	public Map<String, ScheduledFuture> getNameToScheduledFuture() {
		return nameToScheduledFuture;
	}

	public void setNameToScheduledFuture(Map<String, ScheduledFuture> nameToScheduledFuture) {
		this.nameToScheduledFuture = nameToScheduledFuture;
	}

	public Map<String, Runnable> getNameToRunnable() {
		return nameToRunnable;
	}

	public void setNameToRunnable(Map<String, Runnable> nameToRunnable) {
		this.nameToRunnable = nameToRunnable;
	}

	public void removeScheduledFuture(String name) {
		nameToScheduledFuture.remove(name);
	}

	public Map<String, ScheduledJobModel> getNameToScheduledSource() {
		return nameToScheduledSource;
	}

	public void setNameToScheduledSource(Map<String, ScheduledJobModel> nameToScheduledSource) {
		this.nameToScheduledSource = nameToScheduledSource;
	}

	public Map<String, ScheduledRunnable> getNameToSuperScheduledRunnable() {
		return nameToSuperScheduledRunnable;
	}

	public void setNameToSuperScheduledRunnable(
		Map<String, ScheduledRunnable> nameToSuperScheduledRunnable) {
		this.nameToSuperScheduledRunnable = nameToSuperScheduledRunnable;
	}
}

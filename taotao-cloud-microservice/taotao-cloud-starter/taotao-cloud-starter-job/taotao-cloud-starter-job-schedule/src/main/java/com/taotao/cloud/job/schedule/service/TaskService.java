package com.taotao.cloud.job.schedule.service;


import com.taotao.cloud.job.schedule.model.entity.TaskLog;
import com.taotao.cloud.job.schedule.model.param.TaskParam;
import com.taotao.cloud.job.schedule.model.vo.TaskVo;

public interface TaskService {

	/***
	 *任务列表查询
	 * @return o
	 */
	Object taskList();

	/**
	 * 新增任务
	 *
	 * @param param 新增参数
	 */
	void addTask(TaskParam param);

	/**
	 * 修改任务
	 *
	 * @param param 修改参数
	 */
	void updateTask(TaskParam param);

	/**
	 * 执行任务
	 *
	 * @param id 任务id
	 */
	void invokeTask(String id);


	/**
	 * 暂停任务
	 *
	 * @param id 任务id
	 */
	void stopTask(String id);

	/**
	 * 删除任务
	 *
	 * @param id 任务id
	 */
	void deleteTask(String id);

	/**
	 * 禁用任务
	 *
	 * @param id 任务id
	 */
	void forbidTask(String id);

	/**
	 * 查询详情
	 *
	 * @param id 任务id
	 */
	TaskVo getTaskById(String id);

	/**
	 * 任务日志
	 */
	void insertTaskLog(TaskLog log);
}

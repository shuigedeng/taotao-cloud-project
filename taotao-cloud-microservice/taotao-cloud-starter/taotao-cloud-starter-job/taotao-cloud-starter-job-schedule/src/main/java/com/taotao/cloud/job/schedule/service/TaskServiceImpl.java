package com.taotao.cloud.job.schedule.service;


import com.taotao.cloud.job.schedule.model.constant.TaskRunTypeConstant;
import com.taotao.cloud.job.schedule.model.entity.Task;
import com.taotao.cloud.job.schedule.model.entity.TaskLog;
import com.taotao.cloud.job.schedule.model.param.TaskParam;
import com.taotao.cloud.job.schedule.model.vo.TaskVo;
import com.taotao.cloud.job.schedule.task.TaskManager;
import com.taotao.cloud.job.schedule.task.TaskMapper;
import com.taotao.cloud.job.schedule.utils.CronUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class TaskServiceImpl implements TaskService {

	@Resource
	private TaskMapper taskMapper;

	@Resource
	private TaskManager taskManager;

	/***
	 *任务列表查询
	 * @return o
	 */
	@Override
	public Object taskList() {
		return taskMapper.taskList();
	}

	/**
	 * 新增列表
	 *
	 * @param param 新增参数
	 */
	@Override
	public void addTask(TaskParam param) {
		// 解析表达式，此表达式由后端根据规则进行解析，可以直接由前端进行传递
		String cron = CronUtils.dateConvertToCron(param);

		//查询执行周期
		Date nextTime = CronUtils.nextCurrentTime(cron);
		//生成实体
		Task task = new Task();
		BeanUtils.copyProperties(param, task);
		task.setId(UUID.randomUUID().toString());
		task.setDelFlag(0);
		task.setCronExpression(cron);
		task.setNextRunTime(nextTime);
		// 执行策略(1手动-暂停状态(2)，2-自动-执行中状态(1))
		Integer situation = param.getPolicy() == 1 ? 2 : 1;
		task.setSituation(situation);
		//设置版本好为0
		task.setVersion(0);
		//正常
		task.setStatus(0);
		task.setCreateBy("");
		task.setCreateTime(new Date());
		task.setUpdateBy("");
		task.setUpdateTime(new Date());
		//插入数据库
		taskMapper.insert(task);

		// 执行任务
		String runType = param.getPolicy() == 1 ? TaskRunTypeConstant.USER_RUN : TaskRunTypeConstant.SYSTEM_RUN;
		taskManager.start(task, runType);
	}

	/**
	 * 修改任务
	 *
	 * @param param 修改参数
	 */
	@Override
	public void updateTask(TaskParam param) {
		Task task = taskMapper.selectTaskById(param.getId());
		if (task == null) {
			throw new RuntimeException("更新失败,任务不存在");
		}

		//解析表达式
		String cron = CronUtils.dateConvertToCron(param);
		//查询执行周期
		Date nextTime = CronUtils.nextCurrentTime(cron);
		//生成实体
		BeanUtils.copyProperties(param, task);
		task.setCronExpression(cron);
		task.setNextRunTime(nextTime);
		// 执行策略(1手动-暂停状态(2)，2-自动-执行中状态(1))
		int situation = param.getPolicy() == 1 ? 2 : 1;
		task.setSituation(situation);
		task.setStatus(0);//正常
		task.setUpdateBy("");
		task.setUpdateTime(new Date());
		//插入数据库
		taskMapper.update(task);

		// 执行任务
		String runType = param.getPolicy() == 1 ? TaskRunTypeConstant.USER_RUN : TaskRunTypeConstant.SYSTEM_RUN;
		taskManager.start(task, runType);
	}


	/**
	 * 执行任务
	 *
	 * @param id 任务id
	 */
	@Override
	public void invokeTask(String id) {
		Task task = taskMapper.selectTaskById(id);

		if (task == null) {
			throw new RuntimeException("执行失败,任务不存在");
		}

		// 执行
		taskManager.start(task, TaskRunTypeConstant.SYSTEM_RUN);
	}

	/**
	 * 暂停任务
	 *
	 * @param id 任务id
	 */
	@Override
	public void stopTask(String id) {
		Task task = taskMapper.selectTaskById(id);

		if (task == null) {
			throw new RuntimeException("暂停任务失败,任务不存在");
		}
		taskManager.stop(id);
	}

	/**
	 * 删除任务
	 *
	 * @param id 任务id
	 */
	@Override
	public void deleteTask(String id) {
		Task task = taskMapper.selectTaskById(id);

		if (task == null) {
			throw new RuntimeException("删除任务失败,任务不存在");
		}
		taskManager.stop(id);
		//数据库删除
		taskMapper.deleteTask(id);
	}

	/**
	 * 禁用任务
	 *
	 * @param id 任务id
	 */
	@Override
	public void forbidTask(String id) {
		Task task = taskMapper.selectTaskById(id);

		if (task == null) {
			throw new RuntimeException("禁用失败,任务不存在");
		}

		//停止任务
		taskManager.stop(id);

		//禁用
		task.setStatus(1);
		taskMapper.update(task);
	}

	/**
	 * 查询详情
	 *
	 * @param id 任务id
	 */
	@Override
	public TaskVo getTaskById(String id) {
		Task task = taskMapper.selectTaskById(id);
		TaskVo taskVo = new TaskVo();
		BeanUtils.copyProperties(task, taskVo);
		List<String> nextExecution = (List<String>) CronUtils.getNextExecution(task.getCronExpression(), 8, true);
		taskVo.setNext(nextExecution);
		return taskVo;
	}

	/**
	 * 任务日志
	 */
	@Override
	@Async
	public void insertTaskLog(TaskLog log) {
		taskMapper.insertTaskLog(log);
	}
}

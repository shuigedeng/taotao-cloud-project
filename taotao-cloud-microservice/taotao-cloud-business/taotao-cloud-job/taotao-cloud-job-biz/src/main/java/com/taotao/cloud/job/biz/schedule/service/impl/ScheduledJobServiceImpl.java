package com.taotao.cloud.job.biz.schedule.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.job.biz.schedule.entity.ScheduledJob;
import com.taotao.cloud.job.biz.schedule.entity.ScheduledJobLog;
import com.taotao.cloud.job.biz.schedule.mapper.ScheduledJobLogMapper;
import com.taotao.cloud.job.biz.schedule.mapper.ScheduledJobMapper;
import com.taotao.cloud.job.biz.schedule.model.TaskParam;
import com.taotao.cloud.job.biz.schedule.model.TaskVo;
import com.taotao.cloud.job.biz.schedule.service.ScheduledJobService;
import com.taotao.cloud.job.biz.util.JobUtils;
import com.taotao.cloud.job.schedule.constant.TaskRunTypeConstant;
import com.taotao.cloud.job.schedule.model.ScheduledTask;
import com.taotao.cloud.job.schedule.task.TaskManager;
import com.taotao.cloud.job.schedule.utils.CronUtils;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduledJobServiceImpl implements ScheduledJobService {

	@Resource
	private ScheduledJobLogMapper scheduledJobLogMapper;

	@Resource
	private TaskManager taskManager;

	@Resource
	private ScheduledJobMapper scheduledJobMapper;


	/***
	 *任务列表查询
	 * @return o
	 */
	@Override
	public List<ScheduledJob> taskList() {
		return scheduledJobLogMapper.selectList(new QueryWrapper<>());
	}

	/**
	 * 新增列表
	 *
	 * @param param 新增参数
	 */
	@Override
	@Transactional
	public void addTask(TaskParam param) {
		// 解析表达式，此表达式由后端根据规则进行解析，可以直接由前端进行传递
		String cron = JobUtils.dateConvertToCron(param);

		//查询执行周期
		Date nextTime = CronUtils.nextCurrentTime(cron);

		//生成实体
		ScheduledJob scheduledJob = new ScheduledJob();
		BeanUtils.copyProperties(param, scheduledJob);
		scheduledJob.setId(UUID.randomUUID().toString());
		scheduledJob.setDelFlag(false);
		scheduledJob.setCronExpression(cron);
		scheduledJob.setNextRunTime(nextTime);

		// 执行策略(1手动-暂停状态(2)，2-自动-执行中状态(1))
		Integer situation = param.getPolicy() == 1 ? 2 : 1;
		scheduledJob.setSituation(situation);
		//设置版本好为0
		scheduledJob.setVersion(0);

		//正常
		scheduledJob.setStatus(0);
		scheduledJob.setCreateBy(0L);
		scheduledJob.setCreateTime(LocalDateTime.now());
		scheduledJob.setUpdateBy(0L);
		scheduledJob.setUpdateTime(LocalDateTime.now());

		//插入数据库
		scheduledJobLogMapper.insert(scheduledJob);

		// 执行任务
		String runType = param.getPolicy() == 1 ? TaskRunTypeConstant.USER_RUN
			: TaskRunTypeConstant.SYSTEM_RUN;

		ScheduledTask scheduledTask = new ScheduledTask();
		BeanUtil.copyProperties(scheduledJob, scheduledTask);
		taskManager.start(scheduledTask, runType);
	}

	/**
	 * 修改任务
	 *
	 * @param param 修改参数
	 */
	@Override
	public void updateTask(TaskParam param) {
		ScheduledJob scheduledJob = scheduledJobLogMapper.selectById(param.getId());
		if (scheduledJob == null) {
			throw new RuntimeException("更新失败,任务不存在");
		}

		//解析表达式
		String cron = JobUtils.dateConvertToCron(param);
		//查询执行周期
		Date nextTime = CronUtils.nextCurrentTime(cron);
		//生成实体
		BeanUtils.copyProperties(param, scheduledJob);
		scheduledJob.setCronExpression(cron);
		scheduledJob.setNextRunTime(nextTime);
		// 执行策略(1手动-暂停状态(2)，2-自动-执行中状态(1))
		int situation = param.getPolicy() == 1 ? 2 : 1;
		scheduledJob.setSituation(situation);
		scheduledJob.setStatus(0);//正常
		scheduledJob.setUpdateBy(0L);
		scheduledJob.setUpdateTime(LocalDateTime.now());
		//插入数据库
		scheduledJobLogMapper.updateById(scheduledJob);

		// 执行任务
		String runType = param.getPolicy() == 1 ? TaskRunTypeConstant.USER_RUN
			: TaskRunTypeConstant.SYSTEM_RUN;

		ScheduledTask scheduledTask = new ScheduledTask();
		BeanUtil.copyProperties(scheduledJob, scheduledTask);
		taskManager.start(scheduledTask, runType);
	}


	/**
	 * 执行任务
	 *
	 * @param id 任务id
	 */
	@Override
	public void invokeTask(String id) {
		ScheduledJob scheduledJob = scheduledJobLogMapper.selectById(id);

		if (scheduledJob == null) {
			throw new RuntimeException("执行失败,任务不存在");
		}

		ScheduledTask scheduledTask = new ScheduledTask();
		BeanUtil.copyProperties(scheduledJob, scheduledTask);
		// 执行
		taskManager.start(scheduledTask, TaskRunTypeConstant.SYSTEM_RUN);
	}

	/**
	 * 暂停任务
	 *
	 * @param id 任务id
	 */
	@Override
	public void stopTask(String id) {
		ScheduledJob scheduledJob = scheduledJobLogMapper.selectById(id);

		if (scheduledJob == null) {
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
		ScheduledJob scheduledJob = scheduledJobLogMapper.selectById(id);

		if (scheduledJob == null) {
			throw new RuntimeException("删除任务失败,任务不存在");
		}
		taskManager.stop(id);
		//数据库删除
		scheduledJobLogMapper.deleteById(id);
	}

	/**
	 * 禁用任务
	 *
	 * @param id 任务id
	 */
	@Override
	public void forbidTask(String id) {
		ScheduledJob scheduledJob = scheduledJobLogMapper.selectById(id);

		if (scheduledJob == null) {
			throw new RuntimeException("禁用失败,任务不存在");
		}

		//停止任务
		taskManager.stop(id);

		//禁用
		scheduledJob.setStatus(1);

		scheduledJobLogMapper.updateById(scheduledJob);
	}

	/**
	 * 查询详情
	 *
	 * @param id 任务id
	 */
	@Override
	public TaskVo getTaskById(String id) {
		ScheduledJob scheduledJob = scheduledJobLogMapper.selectById(id);
		TaskVo taskVo = new TaskVo();
		BeanUtils.copyProperties(scheduledJob, taskVo);
		List<String> nextExecution = (List<String>) CronUtils.getNextExecution(
			scheduledJob.getCronExpression(), 8, true);
		taskVo.setNext(nextExecution);
		return taskVo;
	}

	/**
	 * 任务日志
	 */
	@Override
	@Async
	public void insertTaskLog(ScheduledJobLog log) {
		scheduledJobMapper.insert(log);
	}
}

/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.task.job.schedule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.job.schedule.constant.TaskRunTypeConstant;
import com.taotao.boot.job.schedule.model.ScheduledTask;
import com.taotao.boot.job.schedule.task.TaskManager;
import com.taotao.boot.job.schedule.utils.CronUtils;
import com.taotao.cloud.sys.api.model.dto.ScheduledJobDTO;
import com.taotao.cloud.sys.biz.task.job.schedule.entity.ScheduledJob;
import com.taotao.cloud.sys.biz.task.job.schedule.mapper.ScheduledJobMapper;
import com.taotao.cloud.sys.biz.utils.JobUtils;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 计划工作服务实现类
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-09 15:05:03
 */
@Service
@AllArgsConstructor
public class ScheduledJobServiceImpl extends ServiceImpl<ScheduledJobMapper, ScheduledJob> implements ScheduledJobService {

	private final TaskManager taskManager;
	private final ScheduledJobMapper scheduledJobMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addTask(ScheduledJobDTO param) {
		// 解析表达式，此表达式由后端根据规则进行解析，可以直接由前端进行传递
		String cron = JobUtils.dateConvertToCron(param);
		// 查询执行周期
		Date nextTime = CronUtils.nextCurrentTime(cron);

		// 生成实体
		ScheduledJob scheduledJob = BeanUtil.copyProperties(param, ScheduledJob.class);
		scheduledJob.setId(UUID.randomUUID().toString());
		scheduledJob.setDelFlag(false);
		scheduledJob.setCronExpression(cron);
		scheduledJob.setNextRunTime(nextTime);
		// 执行策略(1手动-暂停状态(2)，2-自动-执行中状态(1))
		Integer situation = param.getPolicy() == 1 ? 2 : 1;
		scheduledJob.setSituation(situation);
		// 设置版本好为0
		scheduledJob.setVersion(0);
		// 正常
		scheduledJob.setStatus(0);
		scheduledJob.setCreateBy(0L);
		scheduledJob.setCreateTime(LocalDateTime.now());
		scheduledJob.setUpdateBy(0L);
		scheduledJob.setUpdateTime(LocalDateTime.now());

		// 插入数据库
		scheduledJobMapper.insert(scheduledJob);

		// 执行任务
		String runType = param.getPolicy() == 1 ? TaskRunTypeConstant.USER_RUN : TaskRunTypeConstant.SYSTEM_RUN;
		ScheduledTask scheduledTask = BeanUtil.copyProperties(scheduledJob, ScheduledTask.class);
		taskManager.start(scheduledTask, runType);

		return true;
	}

	@Override
	@Transactional
	public boolean updateTask(ScheduledJobDTO param) {
		ScheduledJob scheduledJob = scheduledJobMapper.selectById(param.getId());
		if (scheduledJob == null) {
			throw new RuntimeException("更新失败,任务不存在");
		}

		// 解析表达式
		String cron = JobUtils.dateConvertToCron(param);
		// 查询执行周期
		Date nextTime = CronUtils.nextCurrentTime(cron);
		// 生成实体
		BeanUtils.copyProperties(param, scheduledJob);
		scheduledJob.setCronExpression(cron);
		scheduledJob.setNextRunTime(nextTime);
		// 执行策略(1手动-暂停状态(2)，2-自动-执行中状态(1))
		int situation = param.getPolicy() == 1 ? 2 : 1;
		scheduledJob.setSituation(situation);
		scheduledJob.setStatus(0); // 正常
		scheduledJob.setUpdateBy(0L);
		scheduledJob.setUpdateTime(LocalDateTime.now());
		// 插入数据库
		scheduledJobMapper.updateById(scheduledJob);

		String runType = param.getPolicy() == 1 ? TaskRunTypeConstant.USER_RUN : TaskRunTypeConstant.SYSTEM_RUN;
		// 执行任务
		ScheduledTask scheduledTask = BeanUtil.copyProperties(scheduledJob, ScheduledTask.class);
		taskManager.start(scheduledTask, runType);

		return true;
	}

	@Override
	@Transactional
	public boolean invokeTask(String id) {
		ScheduledJob scheduledJob = scheduledJobMapper.selectById(id);

		if (scheduledJob == null) {
			throw new RuntimeException("执行失败,任务不存在");
		}

		ScheduledTask scheduledTask = BeanUtil.copyProperties(scheduledJob, ScheduledTask.class);

		// 执行
		taskManager.start(scheduledTask, TaskRunTypeConstant.SYSTEM_RUN);

		return true;
	}

	@Override
	@Transactional
	public boolean stopTask(String id) {
		ScheduledJob scheduledJob = scheduledJobMapper.selectById(id);

		if (scheduledJob == null) {
			throw new RuntimeException("暂停任务失败,任务不存在");
		}

		taskManager.stop(id);

		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteTask(String id) {
		ScheduledJob scheduledJob = scheduledJobMapper.selectById(id);

		if (scheduledJob == null) {
			throw new RuntimeException("删除任务失败,任务不存在");
		}

		taskManager.stop(id);
		// 数据库删除
		scheduledJobMapper.deleteById(id);

		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean forbidTask(String id) {
		ScheduledJob scheduledJob = scheduledJobMapper.selectById(id);

		if (scheduledJob == null) {
			throw new RuntimeException("禁用失败,任务不存在");
		}

		// 停止任务
		taskManager.stop(id);

		// 禁用
		scheduledJob.setStatus(1);

		scheduledJobMapper.updateById(scheduledJob);

		return true;
	}

	@Override
	public ScheduledJobVO getTaskById(String id) {
		ScheduledJob scheduledJob = scheduledJobMapper.selectById(id);
		ScheduledJobVO scheduledJobVO = new ScheduledJobVO();
		BeanUtils.copyProperties(scheduledJob, scheduledJobVO);
		List<String> nextExecution =
			(List<String>) CronUtils.getNextExecution(scheduledJob.getCronExpression(), 8, true);
		scheduledJobVO.setNext(nextExecution);
		return scheduledJobVO;
	}


	@Override
	public IPage<ScheduledJob> page(ScheduledJobPageQuery pageQuery) {
		IPage<ScheduledJob> page = pageQuery.buildMpPage();
		return scheduledJobMapper.selectPage(page, new LambdaQueryWrapper<>());
	}
}

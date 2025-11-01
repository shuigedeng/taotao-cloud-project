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
import com.taotao.cloud.sys.biz.task.job.schedule.entity.ScheduledJobLog;
import com.taotao.cloud.sys.biz.task.job.schedule.mapper.ScheduledJobLogMapper;
import java.time.LocalDateTime;
import lombok.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 计划工作日志服务实现类
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-09 15:17:29
 */
@Service
@AllArgsConstructor
public class ScheduledJobLogServiceImpl extends ServiceImpl<ScheduledJobLogMapper, ScheduledJobLog> implements ScheduledJobLogService {

	private final ScheduledJobLogMapper scheduledJobLogMapper;

	@Override
	@Async
	public void insertTaskLog(ScheduledJobLog log) {
		scheduledJobLogMapper.insert(log);
	}

	@Override
	public IPage<ScheduledJobLog> page(ScheduledJobLogPageQuery pageQuery) {
		IPage<ScheduledJobLog> page = pageQuery.buildMpPage();
		LambdaQueryWrapper<ScheduledJobLog> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(StrUtil.isNotBlank(pageQuery.getTaskId()), ScheduledJobLog::getTaskId, pageQuery.getTaskId());
		return scheduledJobLogMapper.selectPage(page, wrapper);
	}

    @Override
	@Transactional
    public void add() {
		ScheduledJobLog jobLog = new ScheduledJobLog();
		jobLog.setTime("asdfasf");
		jobLog.setExceptionInfo("asdfasf");
		jobLog.setStatus(2);
		jobLog.setTaskId("sdf");

		jobLog.setId("1");
		jobLog.setCreateTime(LocalDateTime.now());
		jobLog.setUpdateTime(LocalDateTime.now());

		this.save(jobLog);

    }
}

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

package com.taotao.cloud.sys.biz.job.quartz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.job.api.model.page.QuartzJobLogPageQuery;
import com.taotao.cloud.job.biz.quartz.entity.QuartzJobLog;
import com.taotao.cloud.job.biz.quartz.mapper.QuartzJobLogMapper;
import com.taotao.cloud.job.biz.quartz.service.QuartzJobLogService;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 定时任务日志
 */
@Service
public class QuartzJobLogServiceImpl extends ServiceImpl<QuartzJobLogMapper, QuartzJobLog> implements QuartzJobLogService {

	private final QuartzJobLogMapper quartzJobLogMapper;

	public QuartzJobLogServiceImpl(QuartzJobLogMapper quartzJobLogMapper) {
		this.quartzJobLogMapper = quartzJobLogMapper;
	}

	@Override
	@Async("asyncExecutor")
	public void add(QuartzJobLog quartzJobLog) {
		quartzJobLog.setCreateTime(LocalDateTime.now());
		quartzJobLogMapper.insert(quartzJobLog);
	}

	@Override
	public IPage<QuartzJobLog> page(QuartzJobLogPageQuery quartzJobLogPageQuery) {
		LambdaQueryWrapper<QuartzJobLog> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(QuartzJobLog::getClassName, quartzJobLogPageQuery.getClassName())
			.eq(Objects.nonNull(quartzJobLogPageQuery.getSuccess()),
				QuartzJobLog::getIsSuccess,
				quartzJobLogPageQuery.getSuccess())
			.orderByDesc(QuartzJobLog::getId);

		return this.quartzJobLogMapper.selectPage(quartzJobLogPageQuery.buildMpPage(), wrapper);
	}

	@Override
	public QuartzJobLog findById(Long id) {
		return quartzJobLogMapper.selectById(id);
	}
}

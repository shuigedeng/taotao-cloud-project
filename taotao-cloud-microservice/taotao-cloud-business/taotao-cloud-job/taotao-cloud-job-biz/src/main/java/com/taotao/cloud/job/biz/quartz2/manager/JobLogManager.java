/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.job.biz.quartz2.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.job.biz.quartz2.dao.JobLogDO;
import com.taotao.cloud.job.biz.quartz2.dao.JobLogMapper;
import com.taotao.cloud.job.biz.quartz2.model.JobLogConvert;
import com.taotao.cloud.job.biz.quartz2.model.JobLogDTO;
import com.taotao.cloud.job.biz.quartz2.model.JobLogPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/7 21:21
 */
@Component
@RequiredArgsConstructor
public class JobLogManager {

	private final JobLogMapper jobLogMapper;

	public void addJobLog(JobLogDTO dto) {
		jobLogMapper.insert(JobLogConvert.INSTANCE.convert(dto));
	}

	public JobLogDO findById(Long id) {
		return jobLogMapper.selectById(id);
	}

	public Page<JobLogDO> page(JobLogPageDTO pageDTO) {
		LambdaQueryWrapper<JobLogDO> wrapper = Wrappers.<JobLogDO>lambdaQuery()
			.eq(StringUtils.isNotBlank(pageDTO.getJobName()), JobLogDO::getJobName,
				pageDTO.getJobName());

		return jobLogMapper.selectPage(Page.of(pageDTO.getCurrent(), pageDTO.getSize()), wrapper);
	}

}

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

package com.taotao.cloud.job.biz.quartz.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.job.biz.quartz.core.convert.JobConvert;
import com.taotao.cloud.job.biz.quartz.core.dto.JobDTO;
import com.taotao.cloud.job.biz.quartz.core.dto.JobPageDTO;
import com.taotao.cloud.job.biz.quartz.dao.dataobject.JobDO;
import com.taotao.cloud.job.biz.quartz.dao.mysql.JobMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/7 21:21
 */
@Component
@RequiredArgsConstructor
public class JobManager {

	private final JobMapper jobMapper;

	public void add(JobDTO dto) {
		jobMapper.insert(JobConvert.INSTANCE.convert(dto));
	}

	public void update(JobDTO dto) {
		jobMapper.updateById(JobConvert.INSTANCE.convert(dto));
	}

	public JobDO findById(Long id) {
		return jobMapper.selectById(id);
	}

	public int deleteById(Long id) {
		return jobMapper.deleteById(id);
	}

	public Page<JobDO> page(JobPageDTO page) {
		LambdaQueryWrapper<JobDO> wrapper = Wrappers.<JobDO>lambdaQuery()
			.like(StringUtils.isNotBlank(page.getJobName()), JobDO::getJobName, page.getJobName());

		return jobMapper.selectPage(Page.of(page.getCurrent(), page.getSize()), wrapper);
	}

}

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

package com.taotao.cloud.job.biz.quartz2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.taotao.cloud.job.biz.quartz2.manager.JobLogManager;
import com.taotao.cloud.job.biz.quartz2.model.convert.JobLogConvert;
import com.taotao.cloud.job.biz.quartz2.model.dto.JobLogDTO;
import com.taotao.cloud.job.biz.quartz2.model.dto.JobLogPageDTO;
import com.taotao.cloud.job.quartz.quartz2.core.service.ArtJobLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 定时任务调度日志表
 *
 * @author fxz
 * @date 2022-04-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobLogService implements ArtJobLogService {

	private final JobLogManager jobLogManager;

	/**
	 * 保存job执行日志
	 *
	 * @param jobBeanName jobBeanName
	 * @param jobMessage  job日志信息
	 * @param ex          异常信息
	 */
	@Override
	public void addJobLog(String jobBeanName, String jobMessage, String ex) {
		JobLogDTO dto = JobLogDTO.builder()
			.jobName(jobBeanName)
			.jobMessage(jobMessage)
			.exceptionInfo(ex)
			.status(StringUtils.isBlank(ex) ? "0" : "1")
			.build();
		jobLogManager.addJobLog(dto);
	}

	/**
	 * 分页
	 */
	public IPage<JobLogDTO> page(JobLogPageDTO pageDTO) {
		return JobLogConvert.INSTANCE.convertPage(jobLogManager.page(pageDTO));
	}

	/**
	 * 获取单条
	 */
	public JobLogDTO findById(Long id) {
		return JobLogConvert.INSTANCE.convert(jobLogManager.findById(id));
	}

}

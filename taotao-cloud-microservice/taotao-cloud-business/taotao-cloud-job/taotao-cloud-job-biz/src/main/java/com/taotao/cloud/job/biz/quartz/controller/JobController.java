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

package com.taotao.cloud.job.biz.quartz.controller;

import cn.hutool.core.lang.Assert;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.job.biz.quartz.core.dto.JobDTO;
import com.taotao.cloud.job.biz.quartz.core.dto.JobPageDTO;
import com.taotao.cloud.job.biz.quartz.service.JobService;
import com.taotao.cloud.job.quartz.quartz.core.utils.CronUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时任务调度
 *
 * @author fxz
 * @date 2022-04-03
 */
@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

	private final JobService jobService;

	@PostMapping(value = "/add")
	public Result<JobDTO> add(@RequestBody JobDTO dto) {
		validJob(dto);
		return Result.success(jobService.add(dto));
	}

	@PostMapping(value = "/update")
	public Result<JobDTO> update(@RequestBody JobDTO dto) {
		validJob(dto);
		return Result.success(jobService.update(dto));
	}

	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.success(jobService.deleteByJobId(id));
	}

	@GetMapping(value = "/findById")
	public Result<JobDTO> findById(Long id) {
		return Result.success(jobService.findById(id));
	}

	@GetMapping(value = "/page")
	public Result<PageResult<JobDTO>> page(JobPageDTO page) {
		return Result.success(PageResult.convertMybatisPage(jobService.page(page), JobDTO.class));
	}

	/**
	 * 定时任务状态修改
	 */
	@PutMapping("/changeStatus")
	public Result<Boolean> changeStatus(@RequestBody JobDTO dto) {
		return Result.success(jobService.changeStatus(dto));
	}

	/**
	 * 定时任务立即执行一次
	 */
	@PutMapping("/run")
	public Result<Boolean> run(@RequestBody JobDTO dto) {
		jobService.run(dto);
		return Result.success();
	}

	private void validJob(JobDTO dto) {
		Assert.isTrue(CronUtils.isValid(dto.getCronExpression()),
			"新增任务'" + dto.getJobName() + "'失败，Cron表达式不正确");
	}

}

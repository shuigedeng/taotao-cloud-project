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

package com.taotao.cloud.sys.biz.task.job.schedule.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.data.mybatis.mybatisplus.MpUtils;
import com.taotao.boot.web.annotation.BusinessApi;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 安排工作控制器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-09 15:09:53
 */
@BusinessApi
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/job/schedule")
@Tag(name = "schedule任务管理API", description = "schedule任务管理API")
public class ScheduledJobController {

	private final ScheduledJobService scheduledJobService;
	private final IFeignFileApi fileApi;

	@GetMapping("/testFeignFileApi")
	@Operation(summary = "feign测试调用", description = "feign测试调用")
	public Result<Boolean> jobList() {

		FeignFileResponse sdfasdf = fileApi.findByCode("sdfasdf");
		LogUtils.info(String.valueOf(sdfasdf));

		return Result.success(true);
	}

	@GetMapping("/page")
	@Operation(summary = "分页查询任务列表", description = "分页查询任务列表")
	@RequestLogger
	public Result<PageResult<ScheduledJobVO>> page(ScheduledJobPageQuery pageQuery) {
		IPage<ScheduledJob> page = scheduledJobService.page(pageQuery);
		return Result.success(MpUtils.convertMybatisPage(page, ScheduledJobVO.class));
	}

	@PostMapping("/job")
	@Operation(summary = "新增任务", description = "新增任务")
	@RequestLogger
	public Result<Boolean> addTask(@Validated @RequestBody ScheduledJobDTO param) {
		return Result.success(scheduledJobService.addTask(param));
	}

	@PutMapping("/job")
	@Operation(summary = "更新任务", description = "更新任务")
	@RequestLogger
	public Result<Boolean> updateTask(@Validated @RequestBody ScheduledJobDTO param) {
		return Result.success(scheduledJobService.updateTask(param));
	}

	@DeleteMapping("job/{id}")
	@Operation(summary = "删除任务", description = "删除任务")
	@RequestLogger
	public Result<Boolean> deleteTask(@PathVariable("id") String id) {
		return Result.success(scheduledJobService.deleteTask(id));
	}

	@PostMapping("/job/stop/{id}")
	@Operation(summary = "暂停任务", description = "暂停任务")
	@RequestLogger
	public Result<Boolean> stopTask(@PathVariable("id") String id) {
		return Result.success(scheduledJobService.stopTask(id));
	}

	@PostMapping("/job/invoke/{id}")
	@Operation(summary = "执行任务", description = "执行任务")
	@RequestLogger
	public Result<Boolean> invokeTask(@PathVariable("id") String id) {
		return Result.success(scheduledJobService.invokeTask(id));
	}

	@GetMapping("/job/info/{id}")
	@Operation(summary = "查询详情", description = "查询详情")
	@RequestLogger
	public Result<ScheduledJobVO> getTaskById(@PathVariable("id") String id) {
		return Result.success(scheduledJobService.getTaskById(id));
	}
}

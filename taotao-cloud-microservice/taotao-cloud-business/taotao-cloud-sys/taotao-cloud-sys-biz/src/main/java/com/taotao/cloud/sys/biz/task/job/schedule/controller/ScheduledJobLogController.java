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
import com.taotao.boot.data.mybatis.mybatisplus.MpUtils;
import com.taotao.boot.security.spring.annotation.NotAuth;
import com.taotao.boot.webmvc.annotation.BusinessApi;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 将工作日志控制器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-09 15:20:36
 */
@BusinessApi
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/job/schedule/log")
@Tag(name = "schedule任务管理日志API", description = "schedule任务管理日志API")
public class ScheduledJobLogController {

	private final ScheduledJobLogService scheduledJobLogService;

	@GetMapping("/page")
	@Operation(summary = "分页查询任务列表", description = "分页查询任务列表")
	@RequestLogger
	public Result<PageResult<ScheduledJobLogVO>> page(ScheduledJobLogPageQuery pageQuery) {
		IPage<ScheduledJobLog> page = scheduledJobLogService.page(pageQuery);
		return Result.success(MpUtils.convertMybatisPage(page, ScheduledJobLogVO.class));
	}

	@GetMapping("/addTest")
	@Operation(summary = "分页查询任务列表", description = "分页查询任务列表")
	@RequestLogger
	@NotAuth
	public Result<Boolean> addTest(ScheduledJobLogPageQuery pageQuery) {
		scheduledJobLogService.add();
		return Result.success();
	}
}

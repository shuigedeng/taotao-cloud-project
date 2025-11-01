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

package com.taotao.cloud.sys.biz.task.job.quartz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.data.mybatis.mybatisplus.MpUtils;
import com.taotao.boot.web.annotation.BusinessApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 石英工作日志控制器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-09 15:19:05
 */
@BusinessApi
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/job/quartz/log")
@Tag(name = "quartz定时任务管理日志API", description = "quartz定时任务管理日志API")
public class QuartzJobLogController {

	private final QuartzJobLogService quartzJobLogService;

	@GetMapping("/page")
	@Operation(summary = "分页查询任务日志", description = "分页查询任务日志")
	public Result<PageResult<QuartzJobLogVO>> page(QuartzJobLogPageQuery quartzJobLogPageQuery) {
		IPage<QuartzJobLog> page = quartzJobLogService.page(quartzJobLogPageQuery);
		return Result.success(MpUtils.convertMybatisPage(page, QuartzJobLogVO.class));
	}

	@GetMapping("/{id}")
	@Operation(summary = "查询单个任务日志", description = "查询单个任务日志")
	public Result<QuartzJobLogVO> findById(@PathVariable Long id) {
		QuartzJobLog quartzJobLog = quartzJobLogService.findById(id);
		return Result.success(BeanUtil.copyProperties(quartzJobLog, QuartzJobLogVO.class));
	}
}

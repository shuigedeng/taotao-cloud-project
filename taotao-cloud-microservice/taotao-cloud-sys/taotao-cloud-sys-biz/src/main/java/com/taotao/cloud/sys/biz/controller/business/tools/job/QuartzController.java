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
package com.taotao.cloud.sys.biz.controller.business.tools.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.idempotent.annotation.Idempotent;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.api.web.dto.quartz.QuartzJobDto;
import com.taotao.cloud.sys.api.web.dto.quartz.QuartzJobQueryCriteria;
import com.taotao.cloud.sys.api.web.dto.quartz.QuartzLogDto;
import com.taotao.cloud.sys.api.web.dto.quartz.QuartzLogQueryCriteria;
import com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob;
import com.taotao.cloud.sys.biz.model.entity.quartz.QuartzLog;
import com.taotao.cloud.sys.biz.service.business.IQuartzJobService;
import com.taotao.cloud.sys.biz.service.business.IQuartzLogService;
import com.taotao.cloud.web.quartz.QuartzJobModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * QuartzController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 16:38:25
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/sys/tools/job/quart")
@Tag(name = "工具管理端-quartz定时任务管理API", description = "工具管理端-quartz定时任务管理API")
public class QuartzController {

	private static final String ENTITY_NAME = "quartzJob";

	private final IQuartzJobService quartzJobService;
	private final IQuartzLogService quartzLogService;

	@Operation(summary = "查询定时任务", description = "查询定时任务")
	@RequestLogger("查询定时任务")
	@GetMapping
	@PreAuthorize("@el.check('admin','timing:list')")
	public Result<Map<String, Object>> getJobs(QuartzJobQueryCriteria criteria, Pageable pageable) {
		Map<String, Object> stringObjectMap = quartzJobService.queryAll(criteria, pageable);
		return Result.success(stringObjectMap);
	}

	@Operation(summary = "导出任务数据", description = "导出任务数据")
	@RequestLogger("导出任务数据")
	@GetMapping(value = "/download")
	@PreAuthorize("@el.check('admin','timing:list')")
	public void download(HttpServletResponse response, QuartzJobQueryCriteria criteria)
		throws IOException {
		List<QuartzJob> quartzJobs = quartzJobService.queryAll(criteria);
		List<QuartzJobDto> collect = quartzJobs.stream().filter(Objects::nonNull)
			.map(e -> BeanUtil.copyProperties(e, QuartzJobDto.class))
			.collect(Collectors.toList());

		quartzJobService.download(collect, response);
	}

	@Operation(summary = "导出日志数据", description = "导出日志数据")
	@RequestLogger("导出日志数据")
	@GetMapping(value = "/logs/download")
	@PreAuthorize("@el.check('admin','timing:list')")
	public void downloadLog(HttpServletResponse response, QuartzLogQueryCriteria criteria)
		throws IOException {
		List<QuartzLog> quartzLogs = quartzLogService.queryAll(criteria);
		List<QuartzLogDto> collect = quartzLogs.stream().filter(Objects::nonNull)
			.map(e -> BeanUtil.copyProperties(e, QuartzLogDto.class))
			.collect(Collectors.toList());

		quartzLogService.download(collect, response);
	}

	@Operation(summary = "查询任务执行日志", description = "查询任务执行日志")
	@RequestLogger("查询任务执行日志")
	@GetMapping(value = "/logs")
	@PreAuthorize("@el.check('admin','timing:list')")
	public Result<Map<String, Object>> getJobLogs(QuartzLogQueryCriteria criteria,
		Pageable pageable) {
		Map<String, Object> stringObjectMap = quartzLogService.queryAll(criteria, pageable);
		return Result.success(stringObjectMap);
	}

	@Operation(summary = "新增定时任务", description = "新增定时任务")
	@RequestLogger("新增定时任务")
	@Idempotent(key = "create", perFix = "quartzJob")
	@PostMapping
	@PreAuthorize("@el.check('admin','timing:add')")
	public Result<Boolean> create(@Validated @RequestBody QuartzJobModel jobModel) {
		if (jobModel.getId() != null) {
			throw new BusinessException("A new " + ENTITY_NAME + " cannot already have an ID");
		}
		QuartzJob job = new QuartzJob();
		BeanUtil.copyProperties(jobModel, job);
		return Result.success(quartzJobService.save(job));
	}

	@Operation(summary = "修改定时任务", description = "修改定时任务")
	@RequestLogger("修改定时任务")
	@Idempotent(key = "update", perFix = "quartzJob")
	@PutMapping
	@PreAuthorize("@el.check('admin','timing:edit')")
	public Result<Boolean> update(@Validated @RequestBody QuartzJobModel jobModel) {
		QuartzJob job = new QuartzJob();
		BeanUtil.copyProperties(jobModel, job);
		quartzJobService.updateById(job);
		return Result.success(true);
	}

	@Operation(summary = "更改定时任务状态", description = "更改定时任务状态")
	@RequestLogger("更改定时任务状态")
	@Idempotent(key = "updateIsPause", perFix = "quartzJob")
	@PutMapping(value = "/{id}")
	@PreAuthorize("@el.check('admin','timing:edit')")
	public Result<Boolean> updateIsPause(@PathVariable Long id) {
		quartzJobService.updateIsPause(
			quartzJobService.getOne(new LambdaQueryWrapper<QuartzJob>()
				.eq(QuartzJob::getId, id)));
		return Result.success(true);
	}

	@Operation(summary = "执行定时任务", description = "执行定时任务")
	@RequestLogger("执行定时任务")
	@Idempotent(key = "execution", perFix = "quartzJob")
	@PutMapping(value = "/exec/{id}")
	@PreAuthorize("@el.check('admin','timing:edit')")
	public Result<Boolean> execution(@PathVariable Long id) {
		quartzJobService.execution(
			quartzJobService.getOne(new LambdaQueryWrapper<QuartzJob>().eq(QuartzJob::getId, id)));
		return Result.success(true);
	}

	@Operation(summary = "删除定时任务", description = "删除定时任务")
	@RequestLogger("删除定时任务")
	@Idempotent(key = "delete", perFix = "quartzJob")
	@DeleteMapping
	@PreAuthorize("@el.check('admin','timing:del')")
	public Result<Boolean> delete(@RequestBody Integer[] ids) {
		quartzJobService.removeByIds(new ArrayList<>(Arrays.asList(ids)));
		return Result.success(true);
	}
}

package com.taotao.cloud.job.biz.quartz.controller;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.job.biz.quartz.param.QuartzJobLogQuery;
import com.taotao.cloud.job.biz.quartz.service.QuartzJobLogService;
import com.taotao.cloud.job.biz.quartz.vo.QuartzJobLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/job/quartz/log")
@Tag(name = "quartz任务管理日志API", description = "quartz任务管理日志API")
public class QuartzJobLogController {

	@Resource
	private QuartzJobLogService quartzJobLogService;

	@GetMapping("/page")
	@Operation(summary = "分页查询任务日志", description = "分页查询任务日志")
	public Result<PageResult<QuartzJobLogVO>> page(QuartzJobLogQuery quartzJobLogQuery) {
		return Result.success(quartzJobLogService.page(quartzJobLogQuery));
	}

	@GetMapping("/findById")
	@Operation(summary = "查询单个任务日志", description = "查询单个任务日志")
	public Result<QuartzJobLogVO> findById(Long id) {
		return Result.success(quartzJobLogService.findById(id));
	}

}

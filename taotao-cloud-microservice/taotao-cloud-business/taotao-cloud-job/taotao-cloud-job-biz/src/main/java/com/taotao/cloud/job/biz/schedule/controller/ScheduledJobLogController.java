package com.taotao.cloud.job.biz.schedule.controller;

import com.taotao.cloud.job.biz.schedule.service.ScheduledJobLogService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/job/schedule/log")
@Tag(name = "schedule任务管理日志API", description = "schedule任务管理日志API")
public class ScheduledJobLogController {

	@Resource
	private ScheduledJobLogService scheduledJobLogService;

	@GetMapping("/list")
	@RequestLogger
	@Operation(summary = "分页查询schedule任务管理日志", description = "分页查询schedule任务管理日志")
	public Object taskList() {
		return scheduledJobLogService.taskList();
	}


}

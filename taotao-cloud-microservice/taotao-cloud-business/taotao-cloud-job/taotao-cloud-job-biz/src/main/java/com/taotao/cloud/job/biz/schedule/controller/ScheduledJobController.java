package com.taotao.cloud.job.biz.schedule.controller;

import com.taotao.cloud.job.biz.schedule.model.TaskParam;
import com.taotao.cloud.job.biz.schedule.model.TaskVo;
import com.taotao.cloud.job.biz.schedule.service.ScheduledJobService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/job/schedule")
@Tag(name = "schedule任务管理API", description = "schedule任务管理API")
public class ScheduledJobController {

	@Resource
	private ScheduledJobService scheduledJobService;

	@GetMapping("/list")
	@Operation(summary = "任务列表", description = "任务列表")
	@RequestLogger
	public Object taskList() {
		return scheduledJobService.taskList();
	}

	@PostMapping("/add")
	@Operation(summary = "新增任务", description = "新增任务")
	@RequestLogger
	public void addTask(@RequestBody TaskParam param) {
		scheduledJobService.addTask(param);
	}

	@PutMapping("/update")
	@Operation(summary = "更新任务", description = "更新任务")
	@RequestLogger
	public void updateTask(@RequestBody TaskParam param) {
		scheduledJobService.updateTask(param);
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除任务", description = "删除任务")
	@RequestLogger
	public void deleteTask(@PathVariable("id") String id) {
		scheduledJobService.deleteTask(id);
	}

	@PostMapping("stop/{id}")
	@Operation(summary = "暂停任务", description = "暂停任务")
	@RequestLogger
	public void stopTask(@PathVariable("id") String id) {
		scheduledJobService.stopTask(id);
	}


	@PostMapping("invoke/{id}")
	@Operation(summary = "执行任务", description = "执行任务")
	@RequestLogger
	public void invokeTask(@PathVariable("id") String id) {
		scheduledJobService.invokeTask(id);
	}


	@GetMapping("info/{id}")
	@Operation(summary = "查询详情", description = "查询详情")
	@RequestLogger
	public TaskVo getTaskById(@PathVariable("id") String id) {
		return scheduledJobService.getTaskById(id);
	}

}

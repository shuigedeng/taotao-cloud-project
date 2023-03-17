package com.taotao.cloud.job.biz.schedule.controller;

import com.taotao.cloud.job.biz.schedule.model.TaskParam;
import com.taotao.cloud.job.biz.schedule.model.TaskVo;
import com.taotao.cloud.job.biz.schedule.service.ScheduledJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("task")
@Tag(name = "任务管理")
@RequestMapping("/job/schedule")
public class ScheduledJobLogController {

	@Resource
	private ScheduledJobService scheduledJobService;

	/**
	 * 查询任务列表
	 *
	 * @return
	 */
	@GetMapping("/list")
	@Operation(summary = "任务列表")
	public Object taskList() {
		return scheduledJobService.taskList();
	}

	/**
	 * 新增任务
	 *
	 * @param param
	 */
	@PostMapping("/add")
	@Operation(summary = "新增任务")
	public void addTask(@RequestBody TaskParam param) {
		scheduledJobService.addTask(param);
	}

	/**
	 * 更新任务
	 *
	 * @param param
	 */
	@PutMapping("/update")
	@Operation(summary = "更新任务")
	public void updateTask(@RequestBody TaskParam param) {
		scheduledJobService.updateTask(param);
	}

	/**
	 * 删除任务
	 *
	 * @param id 任务id
	 */
	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除任务")
	public void deleteTask(@PathVariable("id") String id) {
		scheduledJobService.deleteTask(id);
	}


	/**
	 * 暂停任务
	 *
	 * @param id 任务id
	 */
	@PostMapping("stop/{id}")
	@Operation(summary = "暂停任务")
	public void stopTask(@PathVariable("id") String id) {
		scheduledJobService.stopTask(id);
	}


	/**
	 * 执行任务
	 *
	 * @param id 任务id
	 */
	@PostMapping("invoke/{id}")
	@Operation(summary = "执行任务")
	public void invokeTask(@PathVariable("id") String id) {
		scheduledJobService.invokeTask(id);
	}


	/**
	 * 查询详情
	 *
	 * @param id 任务id
	 * @return
	 */
	@GetMapping("info/{id}")
	@Operation(summary = "查询详情")
	public TaskVo getTaskById(@PathVariable("id") String id) {
		return scheduledJobService.getTaskById(id);
	}

}

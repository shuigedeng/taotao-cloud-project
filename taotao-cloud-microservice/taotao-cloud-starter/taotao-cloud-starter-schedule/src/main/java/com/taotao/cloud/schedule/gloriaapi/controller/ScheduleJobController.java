package com.taotao.cloud.schedule.gloriaapi.controller;

import com.gloria.schedule.annotation.SysLog;
import com.gloria.schedule.common.utils.PageUtils;
import com.gloria.schedule.common.vo.CommonResult;
import com.gloria.schedule.entity.ScheduleJobEntity;
import com.gloria.schedule.service.ScheduleJobService;
import com.gloria.schedule.utils.ValidatorUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


@RestController
@RequestMapping("/sys/schedule")
@AllArgsConstructor
@Slf4j
public class ScheduleJobController {

	private final ScheduleJobService scheduleJobService;
	
	/**
	 * 定时任务列表
	 */
	@RequestMapping("/list")
	public CommonResult<Map<String, Object>> list(@RequestParam Map<String, Object> params){
		PageUtils page = scheduleJobService.queryPage(params);

		return CommonResult.put("page", page);
	}
	
	/**
	 * 定时任务信息
	 */
	@RequestMapping("/info/{jobId}")
	public CommonResult<Map<String, Object>> info(@PathVariable("jobId") Long jobId){
		ScheduleJobEntity schedule = scheduleJobService.getById(jobId);
		
		return CommonResult.put("schedule", schedule);
	}
	
	/**
	 * 保存定时任务
	 */
	@SysLog("保存定时任务")
	@PostMapping("/save")
	public CommonResult<Map<String, Object>> save(@RequestBody ScheduleJobEntity scheduleJob){
		ValidatorUtils.validateEntity(scheduleJob);
		
		scheduleJobService.saveJob(scheduleJob);
		
		return CommonResult.success();
	}
	
	/**
	 * 修改定时任务
	 */
	@SysLog("修改定时任务")
	@PostMapping("/update")
	public CommonResult<Object> update(@RequestBody ScheduleJobEntity scheduleJob){
		ValidatorUtils.validateEntity(scheduleJob);
				
		scheduleJobService.update(scheduleJob);

		return CommonResult.success();
	}
	
	/**
	 * 删除定时任务
	 */
	@SysLog("删除定时任务")
	@PostMapping("/delete")
	public CommonResult<Object> delete(@RequestBody Long[] jobIds){
		scheduleJobService.deleteBatch(jobIds);
		
		return CommonResult.success();
	}
	
	/**
	 * 立即执行任务
	 */
	@SysLog("立即执行任务")
	@PostMapping("/run")
	public CommonResult<Object> run(@RequestBody Long[] jobIds){
		scheduleJobService.run(jobIds);
		return CommonResult.success();
	}
	
	/**
	 * 暂停定时任务
	 */
	@SysLog("暂停定时任务")
	@PostMapping("/pause")
	public CommonResult<Map<String, Object>> pause(@RequestBody Long[] jobIds){
		log.info(Arrays.toString(jobIds));
		scheduleJobService.pause(jobIds);
		return CommonResult.success();
	}
	
	/**
	 * 恢复定时任务
	 */
	@SysLog("恢复定时任务")
	@PostMapping("/resume")
	public CommonResult<Map<String, Object>> resume(@RequestBody Long[] jobIds){
		scheduleJobService.resume(jobIds);
		return CommonResult.success();
	}

}

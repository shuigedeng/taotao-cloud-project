package com.taotao.cloud.schedule.gloriaapi.controller;

import com.gloria.schedule.common.utils.PageUtils;
import com.gloria.schedule.common.vo.CommonResult;
import com.gloria.schedule.entity.ScheduleJobLogEntity;
import com.gloria.schedule.service.ScheduleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController {

	@Autowired
	private ScheduleJobLogService scheduleJobLogService;
	
	/**
	 * 定时任务日志列表
	 */
	@RequestMapping("/list")
	public CommonResult<Map<String, Object>> list(@RequestParam Map<String, Object> params){
		PageUtils page = scheduleJobLogService.queryPage(params);
		
		return CommonResult.put("page", page);
	}
	
	/**
	 * 定时任务日志信息
	 */
	@RequestMapping("/info/{logId}")
	public CommonResult<Map<String, Object>> info(@PathVariable("logId") Long logId){
		ScheduleJobLogEntity log = scheduleJobLogService.getById(logId);
		
		return CommonResult.put("log", log);
	}
}

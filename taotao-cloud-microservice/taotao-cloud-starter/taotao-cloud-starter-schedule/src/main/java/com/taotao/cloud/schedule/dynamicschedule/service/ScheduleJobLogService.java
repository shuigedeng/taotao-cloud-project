package com.taotao.cloud.schedule.dynamicschedule.service;

import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 定时任务日志
 *
 */
public interface ScheduleJobLogService {

	PageInfo queryPage(Map<String, Object> params);

}

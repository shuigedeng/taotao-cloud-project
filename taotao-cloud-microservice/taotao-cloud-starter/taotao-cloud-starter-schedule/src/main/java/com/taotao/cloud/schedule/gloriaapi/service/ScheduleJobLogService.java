package com.taotao.cloud.schedule.gloriaapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gloria.schedule.common.utils.PageUtils;
import com.gloria.schedule.entity.ScheduleJobLogEntity;


import java.util.Map;


public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
}


package com.taotao.cloud.schedule.dynamicschedule.service.impl;

import com.example.dynamicschedule.bean.ScheduleJobLog;
import com.example.dynamicschedule.bean.ScheduleJobLogExample;
import com.example.dynamicschedule.dao.ScheduleJobLogMapper;
import com.example.dynamicschedule.service.ScheduleJobLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl implements ScheduleJobLogService {


	@Autowired
	private ScheduleJobLogMapper scheduleJobLogMapper;

	@Override
	public PageInfo queryPage(Map<String, Object> params) {
		Object jobId = params.get("jobId");
		int page = Integer.parseInt(params.getOrDefault("page", "1").toString());
		int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10").toString());

		PageHelper.startPage(page,pageSize);
		ScheduleJobLogExample scheduleJobLogExample = new ScheduleJobLogExample();
		ScheduleJobLogExample.Criteria criteria = scheduleJobLogExample.createCriteria();
		if (Objects.nonNull(jobId)) {
			criteria.andJobIdEqualTo(Long.parseLong(jobId.toString()));
		}

		List<ScheduleJobLog> scheduleJobLogs = scheduleJobLogMapper.selectByExample(scheduleJobLogExample);
		PageInfo pageInfo = new PageInfo<>(scheduleJobLogs);
		return pageInfo;
	}

}

package com.taotao.cloud.job.biz.schedule.record;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.job.biz.schedule.entity.ScheduledJob;
import com.taotao.cloud.job.biz.schedule.mapper.ScheduledJobLogMapper;
import com.taotao.cloud.job.schedule.model.ScheduledTask;
import com.taotao.cloud.job.schedule.task.ScheduleTaskRecord;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTaskRecordImpl implements ScheduleTaskRecord {

	@Autowired
	private ScheduledJobLogMapper scheduledJobLogMapper;

	@Override
	public ScheduledTask selectTaskById(String id) {
		ScheduledJob scheduledJob = scheduledJobLogMapper.selectById(id);
		return BeanUtil.copyProperties(scheduledJob, ScheduledTask.class);
	}

	@Override
	public void update(ScheduledTask task) {
		scheduledJobLogMapper.updateById(BeanUtil.copyProperties(task, ScheduledJob.class));
	}

	@Override
	public List<ScheduledTask> taskList() {
		List<ScheduledJob> scheduledJobs = scheduledJobLogMapper.selectList(
			new LambdaQueryWrapper<>());
		return scheduledJobs.stream()
			.map(task -> {
				return BeanUtil.copyProperties(task, ScheduledTask.class);
			}).collect(Collectors.toList());
	}
}

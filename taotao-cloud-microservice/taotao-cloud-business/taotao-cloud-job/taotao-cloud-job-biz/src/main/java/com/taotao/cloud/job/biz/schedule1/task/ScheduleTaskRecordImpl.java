package com.taotao.cloud.job.biz.schedule1.task;

import com.taotao.cloud.job.biz.schedule1.mapper.TaskMapper;
import com.taotao.cloud.job.schedule.schedule1.model.ScheduledTask;
import com.taotao.cloud.job.schedule.schedule1.task.ScheduleTaskRecord;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTaskRecordImpl implements ScheduleTaskRecord {

	@Autowired
	private TaskMapper taskMapper;

	@Override
	public ScheduledTask selectTaskById(String id) {
		//return taskMapper.selectTaskById(id);
		return null;
	}

	@Override
	public void update(ScheduledTask task) {

	}

	@Override
	public List<ScheduledTask> taskList() {
		return null;
	}
}

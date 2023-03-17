package com.taotao.cloud.job.biz.schedule.record;

import com.taotao.cloud.job.biz.schedule.entity.ScheduledJobLog;
import com.taotao.cloud.job.biz.schedule.service.ScheduledJobService;
import com.taotao.cloud.job.schedule.task.ScheduleTaskLogRecord;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTaskLogRecordImpl implements ScheduleTaskLogRecord {

	private final Logger log = LoggerFactory.getLogger(ScheduleTaskLogRecordImpl.class);

	@Autowired
	private ScheduledJobService scheduledJobService;

	@Override
	public void recordTaskLog(String taskId, long currentTime, Exception e) {
		long recordTime = System.currentTimeMillis();
		//正常
		int status = 0;
		String exceptionInfo = "";

		if (e != null) {
			//异常
			status = 1;
			exceptionInfo =
				e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage();
		}

		//记录日志
		ScheduledJobLog scheduledJobLog = new ScheduledJobLog();
		scheduledJobLog.setId(UUID.randomUUID().toString());
		scheduledJobLog.setTaskId(taskId);
		scheduledJobLog.setStatus(status);
		if (currentTime > 0) {
			scheduledJobLog.setTime(Long.toString(recordTime - currentTime));
		}
		scheduledJobLog.setExceptionInfo(exceptionInfo);
		scheduledJobLog.setCreateTime(LocalDateTime.now());

		//插入记录
		scheduledJobService.insertTaskLog(scheduledJobLog);
	}
}

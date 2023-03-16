package com.taotao.cloud.job.biz.schedule1.task;

import com.taotao.cloud.job.biz.schedule1.model.TaskLog;
import com.taotao.cloud.job.biz.schedule1.service.TaskService;
import com.taotao.cloud.job.schedule.schedule1.task.ScheduleTaskLogRecord;
import com.taotao.cloud.job.schedule.schedule1.task.TaskManager;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTaskLogRecordImpl implements ScheduleTaskLogRecord {

	private final Logger log = LoggerFactory.getLogger(TaskManager.class);

	@Autowired
	private TaskService taskService;

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
		TaskLog taskLog = new TaskLog();
		taskLog.setId(UUID.randomUUID().toString());
		taskLog.setTaskId(taskId);
		taskLog.setStatus(status);
		if (currentTime > 0) {
			taskLog.setTime(Long.toString(recordTime - currentTime));
		}
		taskLog.setExceptionInfo(exceptionInfo);
		taskLog.setCreateTime(new Date());

		//插入记录
		taskService.insertTaskLog(taskLog);
	}
}

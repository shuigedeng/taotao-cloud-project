package com.taotao.cloud.schedule.task;

import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.schedule.model.entity.TaskLog;
import com.taotao.cloud.schedule.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

public class TaskLogRecord {

    private final Logger log = LoggerFactory.getLogger(TaskManager.class);

    public static void recordTaskLog(String taskId, long currentTime, Exception e) {
        long recordTime = System.currentTimeMillis();
        //正常
        int status = 0;
        String exceptionInfo = "";

        if (e != null) {
            //异常
            status = 1;
            exceptionInfo = e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage();
        }

        //记录日志
        TaskLog taskLog = new TaskLog();
        taskLog.setId( UUID.randomUUID().toString());
        taskLog.setTaskId(taskId);
        taskLog.setStatus(status);
        if (currentTime > 0) {
            taskLog.setTime(Long.toString(recordTime - currentTime));
        }
        taskLog.setExceptionInfo(exceptionInfo);
        taskLog.setCreateTime(new Date());

        //插入记录
        ContextUtils.getBean(TaskService.class).insertTaskLog(taskLog);
    }
}

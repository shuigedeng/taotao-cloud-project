/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.task.job.schedule.record;

import com.taotao.boot.job.schedule.task.ScheduleTaskLogRecord;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ScheduleTaskLogRecordImpl
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Component
public class ScheduleTaskLogRecordImpl implements ScheduleTaskLogRecord {

    private final Logger log = LoggerFactory.getLogger(ScheduleTaskLogRecordImpl.class);

    @Autowired
    private ScheduledJobLogService scheduledJobLogService;

    @Override
    public void recordTaskLog( String taskId, long currentTime, Exception e ) {
        long recordTime = System.currentTimeMillis();
        // 正常
        int status = 0;
        String exceptionInfo = "";

        if (e != null) {
            // 异常
            status = 1;
            exceptionInfo = e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage();
        }

        // 记录日志
        ScheduledJobLog scheduledJobLog = new ScheduledJobLog();
        scheduledJobLog.setId(UUID.randomUUID().toString());
        scheduledJobLog.setTaskId(taskId);
        scheduledJobLog.setStatus(status);
        if (currentTime > 0) {
            scheduledJobLog.setTime(Long.toString(recordTime - currentTime));
        }
        scheduledJobLog.setExceptionInfo(exceptionInfo);
        scheduledJobLog.setCreateTime(LocalDateTime.now());

        // 插入记录
        scheduledJobLogService.insertTaskLog(scheduledJobLog);
    }
}

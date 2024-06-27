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

package com.taotao.cloud.sys.biz.job.schedule.task;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.schedule.model.ScheduledTask;
import com.taotao.cloud.job.schedule.task.TaskManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 测试任务
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:33
 */
@Component
public class TestTask {

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private TaskManager taskManager;

    // @Scheduled(cron = "0 0/30 * * * ?")
    public void robReceiveExpireTask() {
        LogUtils.info(Thread.currentThread().getName() + "------------测试测试");
        LogUtils.info(df.format(LocalDateTime.now()) + "测试测试");

        Map<String, ScheduledTask> taskMap = taskManager.getTaskMap();
        LogUtils.info(taskMap.toString());

        // List<String> runScheduledName = taskManager.getRunScheduledName();
        // LogUtils.info(runScheduledName.toString());
        //
        // List<String> allSuperScheduledName = taskManager.getAllSuperScheduledName();
        // LogUtils.info(allSuperScheduledName.toString());
    }

    // @Scheduled(cron = "0 */1 * * * ?")
    @SchedulerLock(name = "scheduledController_notice", lockAtLeastFor = "PT30S", lockAtMostFor = "PT10M")
    public void notice() {
        try {
            LogUtils.info(Thread.currentThread().getName() + "- 执行定时器 scheduledController_notice");
        } catch (Exception e) {
            LogUtils.error("异常信息:", e);
        }
    }

    /** 每分钟执行一次 [秒] [分] [小时] [日] [月] [周] [年] */
    // @Scheduled(cron = "1 * * * * ?")
    @SchedulerLock(name = "synchronousSchedule")
    public void synchronousSchedule() {
        LogUtils.info("Start run schedule to synchronous data:" + new Date());
    }
}

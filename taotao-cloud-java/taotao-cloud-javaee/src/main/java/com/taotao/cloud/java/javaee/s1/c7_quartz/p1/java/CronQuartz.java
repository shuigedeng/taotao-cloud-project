package com.taotao.cloud.java.javaee.s1.c7_quartz.p1.java;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.GregorianCalendar;

public class CronQuartz {
    public static void main(String[] args) throws SchedulerException {
        // quartz API
        // 1. 调度器Scheduler
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 2. 触发器：
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/2 6/2 11,13,16 9 5 ?")) // 13:13:00 16:15:02
                .build();

        // 3. JobDetail
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();

        // 4. 将 JobDetail 和触发器 增加到调度器中
        scheduler.scheduleJob(jobDetail,trigger);

        // 5. 启动，调度器开始工作
        scheduler.start();
    }
}

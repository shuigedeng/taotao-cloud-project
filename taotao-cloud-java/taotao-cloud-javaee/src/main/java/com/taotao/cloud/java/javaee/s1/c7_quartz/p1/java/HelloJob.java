package com.taotao.cloud.java.javaee.s1.c7_quartz.p1.java;

import org.quartz.*;

import java.util.Date;

// Job 任务
public class HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobKey key = jobDetail.getKey();
        System.out.println(key.getName());
        System.out.println(key.getGroup());
        System.out.println("hello job exec "+new Date());
    }
}

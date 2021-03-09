package com.taotao.cloud.java.javaee.s1.c7_quartz.p2.java;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("job 执行了");
        System.out.println(new Date());
    }
}

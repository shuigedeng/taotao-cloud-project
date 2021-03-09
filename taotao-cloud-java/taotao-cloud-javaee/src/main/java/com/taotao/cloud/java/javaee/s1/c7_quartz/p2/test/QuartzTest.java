package com.taotao.cloud.java.javaee.s1.c7_quartz.p2.test;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuartzTest {
    public static void main(String[] args) throws InterruptedException, SchedulerException {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Thread.sleep(5000);
        StdScheduler stdScheduler = (StdScheduler) context.getBean("scheduler");
        // 任务删除
        // stdScheduler.deleteJob(JobKey.jobKey("job1","job_group1"));
        // 任务暂停
//        stdScheduler.pauseJob(JobKey.jobKey("job1","job_group1"));
//        Thread.sleep(3000);
//        stdScheduler.resumeJob(JobKey.jobKey("job1","job_group1"));
        // 批量操作
        stdScheduler.pauseJobs(GroupMatcher.groupEquals("job_group1"));
        Thread.sleep(3000);
        stdScheduler.resumeJobs(GroupMatcher.groupEquals("job_group1"));
    }
}

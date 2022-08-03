package com.taotao.cloud.sys.biz.api.controller.tools.quartz.service;

import java.util.*;

import com.taotao.cloud.sys.biz.api.controller.tools.classloader.ClassloaderService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Namespace;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuartzService {
    @Autowired
    private ClassloaderService classloaderService;
    @Autowired
    private QuartzServiceNew quartzServiceNew;

    /**
     * 编辑或者添加一个 job
     * @param editJobParam
     */
    public void editJob(String connName, Namespace namespace, EditJobParam editJobParam) throws Exception {
        Scheduler scheduler = quartzServiceNew.loadScheduler(connName,namespace);
        JobDetail jobDetail = scheduler.getJobDetail(editJobParam.getJobKey());
        // 创建 job
        JobKey jobKey = editJobParam.getJobKey();
        if (jobDetail != null){
            scheduler.deleteJob(jobKey);
        }
        ClassLoader classloader = classloaderService.getClassloader(editJobParam.getClassloaderName());
        Class<? extends Job> jobClass = (Class<? extends Job>) classloader.loadClass(editJobParam.getClassName());
        jobDetail = JobBuilder.newJob(jobClass).withIdentity(editJobParam.getJobKey()).withDescription(editJobParam.getDescription()).build();
        jobDetail.getJobDataMap().put("jobMethodName", editJobParam.getJobMethodName());

        // 创建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(editJobParam.getCron());
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger" + jobKey.getName(), jobKey.getGroup());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startNow().withSchedule(cronScheduleBuilder).build();
        Date scheduleJob = scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 触发一个任务
     * @param connName
     * @param jobKey
     * @throws SchedulerException
     */
    @InvokeClassLoader
    public void trigger(String connName,Namespace namespace,JobKey jobKey) throws Exception {
        Scheduler scheduler = quartzServiceNew.loadScheduler(connName,namespace);
        scheduler.triggerJob(jobKey);
    }

    /**
     * 暂停一个任务
     * @param connName
     * @param jobKey
     * @throws SchedulerException
     */
    @InvokeClassLoader
    public void pause(String connName,Namespace namespace,JobKey jobKey) throws Exception {
        Scheduler scheduler = quartzServiceNew.loadScheduler(connName,namespace);
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个任务
     * @param connName
     * @param jobKey
     */
    @InvokeClassLoader
    public void resume(String connName,Namespace namespace,JobKey jobKey) throws Exception {
        Scheduler scheduler = quartzServiceNew.loadScheduler(connName,namespace);
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个任务
     * @param connName
     * @param triggerKey
     */
    @InvokeClassLoader
    public void remove(String connName,Namespace namespace,TriggerKey triggerKey,JobKey jobKey) throws Exception {
        Scheduler scheduler = quartzServiceNew.loadScheduler(connName,namespace);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);

        // 停止任务调度
        scheduler.unscheduleJob(triggerKey);

        // 删除任务
        scheduler.deleteJob(jobKey);
    }
}

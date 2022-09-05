package com.taotao.cloud.quartz.quartz.utils;

import com.taotao.cloud.quartz.quartz.entity.ScheduleJobEntity;
import com.taotao.cloud.quartz.quartz.enums.ScheduleConcurrentEnum;
import com.taotao.cloud.quartz.quartz.enums.ScheduleStatusEnum;
import java.rmi.ServerException;
import org.quartz.*;

/**
 * 定时任务工具类
 *
 * @author 阿沐 babamu@126.com
 */
public class ScheduleUtils {
    private final static String JOB_NAME = "TASK_NAME_";
    /**
     * 任务调度参数key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    /**
     * 获取quartz任务类
     */
    public static Class<? extends Job> getJobClass(ScheduleJobEntity scheduleJob) {
        if(scheduleJob.getConcurrent().equals(ScheduleConcurrentEnum.NO.getValue())){
            return ScheduleDisallowConcurrentExecution.class;
        }else {
            return ScheduleConcurrentExecution.class;
        }
    }
    
    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(ScheduleJobEntity scheduleJob) {
        return TriggerKey.triggerKey(JOB_NAME + scheduleJob.getId(), scheduleJob.getJobGroup());
    }

    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(ScheduleJobEntity scheduleJob) {
        return JobKey.jobKey(JOB_NAME + scheduleJob.getId(), scheduleJob.getJobGroup());
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, ScheduleJobEntity scheduleJob) {
        try {
            // job key
            JobKey jobKey = getJobKey(scheduleJob);
        	// 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getJobClass(scheduleJob)).withIdentity(jobKey).build();

            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJob))
                    .withSchedule(scheduleBuilder).build();

            // 放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(JOB_PARAM_KEY, scheduleJob);

            scheduler.scheduleJob(jobDetail, trigger);

            // 判断是否存在
            if (scheduler.checkExists(jobKey)){
                // 防止创建时存在数据问题，先移除，然后再执行创建操作
                scheduler.deleteJob(jobKey);
            }

            // 判断任务是否过期
            if (CronUtils.getNextExecution(scheduleJob.getCronExpression()) != null){
                // 执行调度任务
                scheduler.scheduleJob(jobDetail, trigger);
            }

            // 暂停任务
            if (scheduleJob.getStatus().equals(ScheduleStatusEnum.PAUSE.getValue())){
                scheduler.pauseJob(jobKey);
            }
        } catch (SchedulerException e) {
            throw new ServerException("创建定时任务失败", e);
        }
    }



    /**
     * 立即执行任务
     */
    public static void run(Scheduler scheduler, ScheduleJobEntity scheduleJob) {
        try {
        	// 参数
        	JobDataMap dataMap = new JobDataMap();
        	dataMap.put(JOB_PARAM_KEY, scheduleJob);

            JobKey jobKey = getJobKey(scheduleJob);
            if (scheduler.checkExists(jobKey)) {
                scheduler.triggerJob(jobKey, dataMap);
            }
        } catch (SchedulerException e) {
            throw new ServerException("执行定时任务失败", e);
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, ScheduleJobEntity scheduleJob) {
        try {
            scheduler.pauseJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new ServerException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, ScheduleJobEntity scheduleJob) {
        try {
            scheduler.resumeJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new ServerException("恢复定时任务失败", e);
        }
    }

    /**
     * 更新定时任务
     */
    public static void updateSchedulerJob(Scheduler scheduler, ScheduleJobEntity scheduleJob) {
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(scheduleJob);

        try {
            // 防止创建时存在数据问题，先移除，然后再执行创建操作
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
            }
        }catch (SchedulerException e){
            throw new ServerException("更新定时任务失败", e);
        }

        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, ScheduleJobEntity scheduleJob) {
        try {
            scheduler.deleteJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new ServerException("删除定时任务失败", e);
        }
    }
}

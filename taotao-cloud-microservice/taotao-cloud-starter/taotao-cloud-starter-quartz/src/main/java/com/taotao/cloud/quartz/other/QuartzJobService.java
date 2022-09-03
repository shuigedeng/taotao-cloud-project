package com.taotao.cloud.quartz.other;

import java.util.Map;

public interface QuartzJobService {
    /**
     * 添加任务可以传参数
     *
     * @param clazzName 继承自Job的类，例如：work.lichong.quartzdemo.solution.one.job.CustomJob
     * @param jobName   任务名称
     * @param groupName 任务组
     * @param cronExp   cron表达式
     * @param param     JobDataMap的参数
     */
    void addJob(String clazzName, String jobName, String groupName, String cronExp, Map<String, Object> param);

    /**
     * 暂停任务
     *
     * @param jobName   任务名称
     * @param groupName 任务组
     */
    void pauseJob(String jobName, String groupName);

    /**
     * 恢复任务
     *
     * @param jobName   任务名称
     * @param groupName 任务组
     */
    void resumeJob(String jobName, String groupName);

    /**
     * 立即运行一次定时任务
     *
     * @param jobName   任务名称
     * @param groupName 任务组
     */
    void runOnce(String jobName, String groupName);

    /**
     * 更新任务
     *
     * @param jobName   任务名称
     * @param groupName 任务组
     * @param cronExp   cron表达式
     * @param param     JobDataMap的参数
     */
    void updateJob(String jobName, String groupName, String cronExp, Map<String, Object> param);

    /**
     * 删除任务
     *
     * @param jobName   任务名称
     * @param groupName 任务组
     */
    void deleteJob(String jobName, String groupName);

    /**
     * 启动所有任务
     */
    void startAllJobs();

    /**
     * 暂停所有任务
     */
    void pauseAllJobs();

    /**
     * 恢复所有任务
     */
    void resumeAllJobs();

    /**
     * 关闭所有任务
     */
    void shutdownAllJobs();
}

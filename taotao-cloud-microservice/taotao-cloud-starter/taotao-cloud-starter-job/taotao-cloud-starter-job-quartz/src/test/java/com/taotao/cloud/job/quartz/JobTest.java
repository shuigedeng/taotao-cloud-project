package com.taotao.cloud.job.quartz;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

// @Component
public class JobTest extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext)
		throws JobExecutionException {
		// 获取参数
		JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
		// 业务逻辑 ...
		LogUtils.info("------springbootquartzonejob执行" + jobDataMap.get("name").toString()
			+ "###############" + jobExecutionContext.getTrigger());

	}

	public void jobTest() {
		LogUtils.info("**********************************");
	}

	// /**
	//  * 定时任务1：
	//  * 同步用户信息Job（任务详情）
	//  */
	// @Bean
	// public JobDetail syncUserJobDetail()
	// {
	// 	JobDetail jobDetail = JobBuilder.newJob(SyncUserJob.class)
	// 		.withIdentity("syncUserJobDetail") //给JobDetail起个名字
	// 		.storeDurably() //即使没有Trigger关联时，也不需要删除该JobDetail
	// 		.build();
	// 	return jobDetail;
	// }
	//
	// /**
	//  * 定时任务1：
	//  * 同步用户信息Job（触发器）
	//  */
	// @Bean
	// public Trigger syncUserJobTrigger() {
	// 	//每隔5秒执行一次
	// 	CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
	//
	// 	//创建触发器
	// 	Trigger trigger = TriggerBuilder.newTrigger()
	// 		.forJob(syncUserJobDetail())//关联上述的JobDetail
	// 		.withIdentity("syncUserJobTrigger")//给Trigger起个名字
	// 		.withSchedule(cronScheduleBuilder)
	// 		.build();
	// 	return trigger;
	// }
}

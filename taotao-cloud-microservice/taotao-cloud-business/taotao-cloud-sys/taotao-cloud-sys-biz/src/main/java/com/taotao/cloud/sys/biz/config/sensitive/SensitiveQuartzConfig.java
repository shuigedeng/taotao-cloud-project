//package com.taotao.cloud.sys.biz.config.sensitive;
//
//import org.quartz.JobBuilder;
//import org.quartz.JobDetail;
//import org.quartz.SimpleScheduleBuilder;
//import org.quartz.Trigger;
//import org.quartz.TriggerBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 定时执行配置
// *
// * @author shuigedeng
// * @version 2022.03
// * @since 2022-03-25 15:01:33
// */
//@Configuration
//public class SensitiveQuartzConfig {
//
//	@Bean
//	public JobDetail sensitiveQuartzDetail() {
//		return JobBuilder.newJob(SensitiveWordsJob.class)
//			.withIdentity("sensitiveQuartz")
//			.storeDurably()
//			.build();
//	}
//
//	@Bean
//	public Trigger sensitiveQuartzTrigger() {
//		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//			.withIntervalInSeconds(3600)
//			.repeatForever();
//
//		return TriggerBuilder.newTrigger()
//			.forJob(sensitiveQuartzDetail())
//			.withIdentity("sensitiveQuartz")
//			.withSchedule(scheduleBuilder)
//			.build();
//	}
//
//}

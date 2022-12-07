//package com.taotao.cloud.data.sync.batch;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * @author : dylanz
// * @since : 08/25/2020
// */
//@Component
//public class SpringScheduledConfig {
//
//	@Autowired
//	private Job singleStepJob;
//	@Autowired
//	private JobLauncher jobLauncher;
//
//	@Scheduled(cron = "0/5 * * * * ?")
//	public void demoScheduled() throws Exception {
//		JobParameters jobParameters = new JobParametersBuilder()
//			.addLong("time", System.currentTimeMillis())
//			.toJobParameters();
//		jobLauncher.run(singleStepJob, jobParameters);
//	}
//}

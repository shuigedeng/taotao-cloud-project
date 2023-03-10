package com.taotao.cloud.data.sync.other.task;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Component
public class Task {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Scheduled(cron = "0 3 1 * * *")
	public void task1() {

		try {
			
			 //计时
	        StopWatch stopWatch = new StopWatch();
	        stopWatch.start(job.getName()); 
			
			String dateParam = new Date().toString();
			JobParameters param = new JobParametersBuilder().addString("date", dateParam).toJobParameters();
			jobLauncher.run(job, param);
			
			
			stopWatch.stop();
			
		} catch (Exception e) {
		}  

	}
}

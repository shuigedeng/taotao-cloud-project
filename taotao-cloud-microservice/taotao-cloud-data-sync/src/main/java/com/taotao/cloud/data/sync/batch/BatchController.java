package com.taotao.cloud.data.sync.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : dylanz
 * @since : 08/25/2020
 */
@RestController
public class BatchController {

	@Autowired
	private Job singleStepJob;
	@Autowired
	private Job multiBoundStepsJob;

	@Autowired
	private JobLauncher jobLauncher;

	@GetMapping("/job/step")
	public String invokeStep() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("time", System.currentTimeMillis())
			.toJobParameters();

		jobLauncher.run(singleStepJob, jobParameters);
		return "The job is proceed.";
	}

	@GetMapping("/job/steps")
	public String invokeSteps() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("time", System.currentTimeMillis())
			.toJobParameters();
		jobLauncher.run(multiBoundStepsJob, jobParameters);
		return "The multi bound steps job is proceed.";
	}
}

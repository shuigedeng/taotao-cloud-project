package com.taotao.cloud.data.sync.batch.multi;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author : dylanz
 * @since : 08/25/2020
 */
@Configuration
@EnableBatchProcessing
public class MultiBatchConfig {

	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private PlatformTransactionManager platformTransactionManager;


	@Bean
	public Job multiBoundStepsJob() {
		return new JobBuilder("multiBoundStepsJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.listener(new MyJobListener())
			.start(uppercaseStep())
			.next(addMessageStep())
			.build();
	}

	@Bean
	public Step uppercaseStep() {
		return new StepBuilder("uppercaseStep", jobRepository)
			.<String, String>chunk(1, platformTransactionManager)
			.reader(new MultiReaderService1())
			.processor(new MultiProcessorService1())
			.writer(new MultiWriterService())
			.build();
	}

	@Bean
	public Step addMessageStep() {
		return new StepBuilder("addMessageStep", jobRepository)
			.<String, String>chunk(1, platformTransactionManager)
			.reader(new MultiReaderService2())
			.processor(new MultiProcessorService2())
			.writer(new MultiWriterService())
			.build();
	}

}

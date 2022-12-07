package com.taotao.cloud.data.sync.batch.single;

import com.taotao.cloud.data.sync.batch.multi.MyJobListener;
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
public class SingleBatchConfig {

	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Bean
	public Job singleStepJob() {
		return new JobBuilder("singleStepJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.listener(new MyJobListener())
			.start(uppercaseStep())
			.build();
	}

	@Bean
	public Step uppercaseStep() {
		return new StepBuilder("uppercaseStep", jobRepository)
			.<String, String>chunk(1, platformTransactionManager)
			.reader(new SingleReaderService())
			.processor(new SingleProcessorService())
			.writer(new SingleWriterService())
			.build();
	}

}

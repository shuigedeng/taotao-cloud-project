package com.taotao.cloud.data.sync.other.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.open.capacity.batch.dao.DeliverPostRowMapper;
import com.open.capacity.batch.entity.DeliverPost;
import com.open.capacity.batch.item.DeliverPostProcessorItem;
import com.open.capacity.batch.item.DeliverPostWriterItem;
import com.open.capacity.batch.support.BatchIncrementer;
import com.open.capacity.batch.support.JobListener;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Resource
	private DataSource dataSource;

	@Resource
	private JobBuilderFactory jobBuilderFactory;

	@Resource
	private StepBuilderFactory stepBuilderFactory;

	@Resource
	private JobExplorer jobExplorer;

	@Resource
	private ApplicationContext applicationContext;

	@Resource
	private DeliverPostWriterItem deliverPostWriterItem;

	@Bean
	public Job job(@Qualifier("masterStep") Step masterStep) {
		return jobBuilderFactory.get("endOfDayjob").start(masterStep).incrementer(new BatchIncrementer())
				.listener(new JobListener()).build();
	}

	@Bean("masterStep")
	public Step masterStep(@Qualifier("slaveStep") Step slaveStep, DataSource dataSource) {

		TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
		partitionHandler.setGridSize(5);
		partitionHandler.setTaskExecutor(new SimpleAsyncTaskExecutor());
		partitionHandler.setStep(slaveStep);

		return stepBuilderFactory.get("masterStep").partitioner(slaveStep.getName(), new ColumnRangePartitioner())
				.step(slaveStep).partitionHandler(partitionHandler).build();
	}

	@Bean
	@StepScope
	public JdbcCursorItemReader<DeliverPost> JdbcCursorItemReader(
			@Value("#{stepExecutionContext['current_thread']}") Long current_thread,
			@Value("#{stepExecutionContext['total_thread']}") Long total_thread) {
		System.err.println("接收到分片参数[" + total_thread + "->" + current_thread + "]");

		JdbcCursorItemReader<DeliverPost> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(this.dataSource); // 设置数据源
		reader.setFetchSize(100); // 设置一次最大读取条数
		reader.setRowMapper(new DeliverPostRowMapper()); // 把数据库中的每条数据映射到DeliverPost对中
//		5,1
//		5,2
//		5,3
//		5,4
//		5,5
		reader.setSql("select order_id , post_id ,isArrived from oc_deliver_post_t where post_id is not null and post_id <> '0' and mod( order_id ,? )= ( ? -1 )");

		reader.setPreparedStatementSetter(new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setLong(1, total_thread);
				preparedStatement.setLong(2, current_thread);
			}
		});

		return reader;
	}

	@Bean("slaveStep")
	public Step slaveStep(DeliverPostProcessorItem processorItem, JdbcCursorItemReader reader) {
		CompositeItemProcessor itemProcessor = new CompositeItemProcessor();
		List<ItemProcessor> processorList = new ArrayList<>();
		processorList.add(processorItem);
		itemProcessor.setDelegates(processorList);
		return stepBuilderFactory.get("slaveStep").<DeliverPost, DeliverPost>chunk(1000)// 事务提交批次
				.reader(reader).processor(itemProcessor).writer(deliverPostWriterItem).build();
	}

}

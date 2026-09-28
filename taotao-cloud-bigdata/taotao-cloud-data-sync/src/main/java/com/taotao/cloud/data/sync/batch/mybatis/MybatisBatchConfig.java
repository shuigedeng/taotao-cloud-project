/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.data.sync.batch.mybatis;

import com.taotao.boot.common.utils.log.LogUtils;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class MybatisBatchConfig {

    @Autowired private JobRepository jobRepository;
    @Autowired private SqlSessionFactory sqlSessionFactory;
    @Autowired private PlatformTransactionManager platformTransactionManager;

    /**
     * JobRepository定义：Job的注册容器以及和数据库打交道（事务管理等）
     */
    @Bean
    public JobRepository myJobRepository(
            DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDatabaseType("mysql");
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.setDataSource(dataSource);
        return jobRepositoryFactoryBean.getObject();
    }

    /**
     * jobLauncher定义： job的启动器,绑定相关的jobRepository
     */
    @Bean
    public JobLauncher myJobLauncher(
            DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        // 设置jobRepository
        jobLauncher.setJobRepository(myJobRepository(dataSource, transactionManager));
        return jobLauncher;
    }

    // Spring Batch提供了一个特殊的bean scope类（StepScope:作为一个自定义的Spring bean scope）。
    // 这个step scope的作用是连接batches的各个steps。
    // 这个机制允许配置在Spring的beans当steps开始时才实例化并且允许你为这个step指定配置和参数。
    @Bean
    @StepScope
    public MyBatisCursorItemReader<BlogInfo> itemReader(
            @Value("#{jobParameters[authorId]}") String authorId) {

        LogUtils.info("开始查询数据库");

        MyBatisCursorItemReader<BlogInfo> reader = new MyBatisCursorItemReader<>();
        reader.setQueryId("com.example.batchdemo.mapper.BlogMapper.queryInfoById");
        reader.setSqlSessionFactory(sqlSessionFactory);
        Map<String, Object> map = new HashMap<>();

        map.put("authorId", Integer.valueOf(authorId));
        reader.setParameterValues(map);
        return reader;
    }

    /**
     * ItemWriter定义：指定datasource，设置批量插入sql语句，写入数据库
     */
    @Bean
    public ItemWriter<BlogInfo> itemWriter(DataSource dataSource) {
        // 使用jdbcBcatchItemWrite写数据到数据库中
        JdbcBatchItemWriter<BlogInfo> writer = new JdbcBatchItemWriter<>();
        // 设置有参数的sql语句
        writer.setItemSqlParameterSourceProvider(
                new BeanPropertyItemSqlParameterSourceProvider<BlogInfo>());
        String sql =
                "insert into bloginfonew "
                        + " (blogAuthor,blogUrl,blogTitle,blogItem) "
                        + " values(:blogAuthor,:blogUrl,:blogTitle,:blogItem)";
        writer.setSql(sql);
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public ItemProcessor<BlogInfo, BlogInfo> itemProcessor() {
        MybatisProcessorNew csvItemProcessor = new MybatisProcessorNew();
        // 设置校验器
        csvItemProcessor.setValidator(new MybatisBeanValidator<>());
        return csvItemProcessor;
    }

    @Bean
    public Step mybatisStep(
            MyBatisCursorItemReader<BlogInfo> itemReader,
            ItemWriter<BlogInfo> itemWriter,
            ItemProcessor<BlogInfo, BlogInfo> itemProcessor) {

        return new StepBuilder("stepNew", jobRepository)
                .listener(new MybatisStepExecutionListener())
                // Chunk的机制(即每次读取一条数据，再处理一条数据，累积到一定数量后再一次性交给writer进行写入操作)
                .<BlogInfo, BlogInfo>chunk(65000, platformTransactionManager)
                .reader(itemReader)
                .faultTolerant()
                .retryLimit(3)
                .retry(Exception.class)
                .skip(Exception.class)
                .skipLimit(10)
                .listener(new MybatisReadListener())
                .processor(itemProcessor)
                .writer(itemWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(2)
                .listener(new MybatisWriteListener())
                .build();
    }

    @Bean
    public Job mybatisJob(Step mybatisStep) {
        return new JobBuilder("myJobNew", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(mybatisStep)
                .end()
                .listener(new MybatisJobListener())
                .build();
    }
}
